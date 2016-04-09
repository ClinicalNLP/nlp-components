package clinicalnlp.seg

import clinicalnlp.dsl.GroovyAnnotator
import clinicalnlp.dsl.UIMAUtil
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.jcas.JCas
import clinicalnlp.type.Segment


import org.junit.Test

import static clinicalnlp.dsl.UIMAUtil.select
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline

class TestSegmenter {

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
