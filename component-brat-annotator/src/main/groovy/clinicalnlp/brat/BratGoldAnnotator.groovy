package clinicalnlp.brat

import static clinicalnlp.dsl.UIMAUtil.*

import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceInitializationException
import org.cleartk.ne.type.NamedEntityMention
import org.cleartk.util.ViewUriUtil

import clinicalnlp.dsl.UIMAUtil
import clinicalnlp.type.Relation

class BratGoldAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_MENTION_TYPE = "mentionType"	
	@ConfigurationParameter(name = "mentionType", mandatory = false, description = "Mention type")
	private String mentionType
	
	public static final String PARAM_ANNOTATION_FILE = "annFileName"
	@ConfigurationParameter(name = "annFileName", mandatory = false, description = "File holding BRAT annotations")
	private String annFileName

	@Override
	public void initialize(UimaContext context)
	throws ResourceInitializationException {
		super.initialize(context);
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		
		UIMAUtil.jcas = jcas
		
		// Load the BRAT document
		BratDocument bratDoc = null;
		// -- if the document was specified in parameter setting, then use that
		if (annFileName != null) {
			bratDoc = BratDocument.parseDocument(BratGoldAnnotator.getResourceAsStream(this.annFileName))
		}
		// -- else lookup URI from JCas
		else {
			File textFile = new File(ViewUriUtil.getURI(jcas));
			String prefix = textFile.getPath().replaceAll('[.]txt$', '');
			File annFile = new File(prefix + ".ann");
			bratDoc = BratDocument.parseDocument(new FileInputStream(annFile))
		}
		
		// Parse named entity mentions
		Map<String, Annotation> annMap = new HashMap<>()
		bratDoc.getSpanAnnotations().each { key, value ->
			mapSpanAnnotation(jcas, value).each { ann ->
				annMap.put(key, ann)
			}
		}
		
		// Parse relations
		bratDoc.getRelAnnotations().values().each { value ->
			NamedEntityMention arg1 = annMap.get(value.arg1)
			NamedEntityMention arg2 = annMap.get(value.arg2)
			if (arg1 != null && arg2 != null) {
				create(type:Relation,arg1:arg1, arg2:arg2)
			}
		}
	}

	List<Annotation> mapSpanAnnotation(JCas jcas, SpanAnnotation span) {
		List<Annotation> anns = []
		if (mentionType == null || mentionType == span.type) {
			NamedEntityMention nem = new NamedEntityMention(jcas, span.span.start, span.span.end)
			nem.mentionType = span.type
			nem.addToIndexes()
			anns << nem
		}
		return anns
	}
}
