package clinicalnlp.brat;

import static clinicalnlp.dsl.UIMAUtil.*
import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.jcas.JCas
import org.cleartk.ne.type.NamedEntityMention
import org.cleartk.util.ae.UriToDocumentTextAnnotator
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import clinicalnlp.dsl.UIMAUtil
import clinicalnlp.type.Relation

import com.google.common.base.Charsets
import com.google.common.io.Resources

@Log4j
class BratGoldAnnotatorTest {
	
	@BeforeClass
	public static void setupClass() {
		BasicConfigurator.configure()
	}

	@Before
	public void setUp() throws Exception {
		log.setLevel(Level.INFO)
	}

	@Test
	public void smokeTest() {
		// test note
		URL url = Resources.getResource("annotated/path-note-1.txt")
		String text = Resources.toString(url, Charsets.UTF_8)

		// build a BratAnnotator analysis engine
		AnalysisEngineDescription brat = AnalysisEngineFactory.createEngineDescription(
			BratGoldAnnotator,
			BratGoldAnnotator.PARAM_ANNOTATION_FILE, '/annotated/path-note-1.ann'
			)
		AggregateBuilder builder = new AggregateBuilder()
		builder.add(brat)
		AnalysisEngine engine = builder.createAggregate()
		
		// make the JCas and process
		JCas jcas = engine.newJCas()
		UIMAUtil.jcas = jcas
		jcas.setDocumentText(text)		
		engine.process(jcas)
		
		// test the result
		Collection<NamedEntityMention> mentions = select(type:NamedEntityMention)
		assert mentions.size() == 8
		
		Collection<Relation> rels = select(type:Relation)
		assert rels.size() == 3
	}
    
    @Test
    public void pathNoteWithAttribute() {
        // build the  pipeline
		AnalysisEngineDescription brat = AnalysisEngineFactory.createEngineDescription(
			BratGoldAnnotator,
			BratGoldAnnotator.PARAM_ANNOTATION_FILE, '/annotated/path-note-2.ann'
			)
		AggregateBuilder builder = new AggregateBuilder()
		builder.add(brat)
		AnalysisEngine engine = builder.createAggregate()
        
        // make the JCas and process text
        JCas jcas = engine.newJCas()
		UIMAUtil.jcas = jcas
        String text = 'foo' * 500
        jcas.setDocumentText(text)
        engine.process(jcas)
        
        // test the result
        Collection<NamedEntityMention> mentions = select(type:NamedEntityMention)
        assert mentions.size() == 4
        
        Collection<Relation> rels = select(type:Relation)
        assert rels.size() == 1
    }
}
