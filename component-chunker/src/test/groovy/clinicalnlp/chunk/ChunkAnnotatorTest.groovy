package clinicalnlp.chunk

import static org.junit.Assert.*
import groovy.util.logging.Log4j
import opennlp.uima.util.UimaUtil

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ExternalResourceDescription
import org.apache.uima.resource.metadata.TypeSystemDescription
import org.cleartk.token.type.Sentence
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import clinicalnlp.dsl.GroovyAnnotator
import clinicalnlp.dsl.UIMAUtil
import clinicalnlp.sent.SentenceDetector
import clinicalnlp.type.Segment

import com.google.common.base.Charsets
import com.google.common.io.Resources

@Log4j
class ChunkAnnotatorTest {
    
    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure()
    }
    
    @Before
    public void setUp() throws Exception {
        log.setLevel(Level.INFO)
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void smokeTestOpenNLP() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.sentdetect.SentenceDetector,
                'opennlp.uima.SentenceType', Sentence.name,
                'opennlp.uima.ContainerType', Segment.name)
        ExternalResourceFactory.createDependencyAndBind(desc, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.sentdetect.SentenceModelResourceImpl, 'file:models/sd-med-model.zip')
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null
    }

    @Test
    public void smokeTest() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()
		
		// sentence model resource
		ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
			opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
        
        // create a sentence detector description
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SENT_MODEL_KEY, extDesc)
		
		// create an engine
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-1.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new CAS and seed with a Segment
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Segment, begin:0, end:text.length())

        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 21
        sents.each { println "Sentence: $it.coveredText" }
    }

    @Test
    public void testWithNoOptions() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/TestSegmenter.groovy')
        
		// sentence model resource
		ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
			opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
        
        // create a sentence detector description
        AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SENT_MODEL_KEY, extDesc)
		
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetector)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-2.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 8
        sents.each { println "Sentence: $it.coveredText" }
    }
    
    @Test
    public void testWithSegmentSelected() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/TestSegmenter.groovy')
        
		// sentence model resource
		ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
			opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
		
		// create a sentence detector description
		AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
				SentenceDetector,
				SentenceDetector.SD_SEGMENTS_TO_PARSE, 'groovy/TestSegmentsToParse.groovy',
				SentenceDetector.SENT_MODEL_KEY, extDesc)
		
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetector)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-2.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 6
        sents.each { println "Sentence: $it.coveredText" }
    }

    @Test
    public void testWithSentenceBreakingPattern() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/TestSegmenter.groovy')
		
		// sentence model resource
		ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
			opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
		
		// create a sentence detector description
		AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
				SentenceDetector,
				SentenceDetector.SD_SPLIT_PATTERN, ':',
				SentenceDetector.SENT_MODEL_KEY, extDesc)
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetector)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-2.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 13
        sents.each { println "Sentence: $it.coveredText" }
    }

    @Test
    public void testWithNewlineSentenceBreakingPattern() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy')
        
		// sentence model resource
		ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
			opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
		
		// create a sentence detector description
		AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
				SentenceDetector,
				SentenceDetector.SD_SPLIT_PATTERN, '[\\n\\r:]+',
				SentenceDetector.SENT_MODEL_KEY, extDesc)
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetector)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-1.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 28
        sents.each { println "Sentence: $it.coveredText" }
    }
    
    /**
     * 
     * @param args
     */
    static void main(args) {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

		// sentence model resource
		ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
			opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
		
		// create a sentence detector description
		AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
				SentenceDetector,
				SentenceDetector.SD_SPLIT_PATTERN, '[\\n\\r:]+',
				SentenceDetector.SENT_MODEL_KEY, extDesc)

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy')

        // build the aggregate
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetector)
        }
        AnalysisEngineDescription desc = builder.createAggregateDescription()
        PrintWriter writer = new PrintWriter(new File('src/test/resources/descriptors/SentenceDetector.xml'))
        desc.toXML(writer)
        writer.close()
    }
}
