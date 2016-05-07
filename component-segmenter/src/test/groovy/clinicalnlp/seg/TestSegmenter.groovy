package clinicalnlp.seg

import static clinicalnlp.dsl.UIMAUtil.select
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline
import groovy.util.logging.Log4j

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import clinicalnlp.dsl.GroovyAnnotator
import clinicalnlp.dsl.UIMAUtil
import clinicalnlp.type.Segment


@Log4j
class TestSegmenter {
	
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
    public void testSegmenter() {

        // -------------------------------------------------------------------
        // build and run a pipeline to generate annotations
        // -------------------------------------------------------------------

        def sentence = """\
        FINDINGS:
        Patient has fever but no cough and pneumonia is ruled out.
        There is no increase in weakness.
        Patient does not have measles.
        IMPRESSION:
        Patient is sick.
        """
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(createEngineDescription(GroovyAnnotator,
                    GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy'))
        }
        AnalysisEngine engine = builder.createAggregate()
        JCas jcas = engine.newJCas()
        UIMAUtil.jcas = jcas

        jcas.setDocumentText(sentence)

        runPipeline(jcas, engine)

        // -------------------------------------------------------------------
        // test the results
        // -------------------------------------------------------------------

        assert select(type:Segment, 
            filter:{it.coveredText==jcas.documentText}).size() == 1
    }
}
