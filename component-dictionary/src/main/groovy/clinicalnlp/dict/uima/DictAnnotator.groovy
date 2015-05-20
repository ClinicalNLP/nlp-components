package clinicalnlp.dict.uima

import static clinicalnlp.dsl.UIMAUtil.*
import groovy.util.logging.Log4j

import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceInitializationException

import clinicalnlp.dict.DictModel
import clinicalnlp.dict.DictModelPool
import clinicalnlp.dict.TokenMatch
import clinicalnlp.dsl.UIMAUtil

@Log4j
public class DictAnnotator extends JCasAnnotator_ImplBase {
	static {
		DictMatch.metaClass.getMatchedTokens = {
			return (delegate.matched == null ? []:
			org.apache.uima.fit.util.JCasUtil.select(delegate.matched, Annotation))
		}
		DictMatch.metaClass.setMatchedTokens = { anns ->
			if (anns == null) {
				return;
			}
			FSArray array = new FSArray(jcas, anns.size())
			int i = 0
			anns.each {
				array.set(i, it)
				i += 1
			}
			delegate.matched = array
		}
	}

//	final static String DIC_RESROURCE_KEY = "dictResource";
//	@ExternalResource(key = "")
//	private DictionaryResource dictResource;
	
	public static final String PARAM_DICTIONARY_ID = 'dictionaryId'
	@ConfigurationParameter(name='dictionaryId', mandatory=false)
	private Integer dictionaryId

	public static final String PARAM_CONTAINER_CLASS = 'containerClassName'
	@ConfigurationParameter(name='containerClassName', mandatory=true, defaultValue='org.cleartk.token.type.Sentence')
	private String containerClassName

	public static final String PARAM_TOKEN_CLASS = 'tokenClassName'
	@ConfigurationParameter(name='tokenClassName', mandatory=true, defaultValue='org.cleartk.token.type.Token')
	private String tokenClassName

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext)
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		logger.info "Loading dictionary: ${dictionaryId}"
		DictModel dict = DictModelPool.get(dictionaryId)
		if (dict == null) {
			logger.warn "No dictionary available with id: ${dictionaryId}"
			return;
		}
		
		UIMAUtil.jcas = jcas
		
		Class<Annotation> ContainerClass = Class.forName(containerClassName)
		Class<Annotation> TokenClass = Class.forName(tokenClassName)

		select(type:ContainerClass).each { Annotation container ->
			Collection<Annotation> anns = select(type:TokenClass, filter:coveredBy(container))
			Collection<String> tokens = new ArrayList<>()
			anns.each { Annotation ann ->
				tokens << ann.coveredText
			}
			Collection<TokenMatch> matches = dict.matches(tokens)
			matches.each { TokenMatch m ->
				Collection<Annotation> matched = new ArrayList<>()
				for (int i = m.begin; i < m.end; i++) {
					matched << anns.get(i)
				}
//				UIMAUtil.create(type:DictMatch,
//				canonical:m.value.canonical,
//				code:m.entry.code,
//				vocabulary:m.entry.vocabulary,
//				container:container,
//				matchedTokens:matched
//				)
			}
		}
	}
}
