package clinicalnlp.dict;

import static clinicalnlp.dsl.UIMAUtil.*
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline
import static org.junit.Assert.*

import de.tudarmstadt.ukp.dkpro.core.io.text.*;

import java.util.regex.Matcher

import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.jcas.JCas
import org.cleartk.token.type.Sentence
import org.junit.Test

import clinicalnlp.dsl.GroovyAnnotator
import clinicalnlp.dsl.UIMAUtil

class TestDictionary {

    @Test
    public void smokeTest() {

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
//        builder.with {
//            add(createEngineDescription(GroovyAnnotator,
//                    GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy'))
//        }
//        AnalysisEngine engine = builder.createAggregate()
//        JCas jcas = engine.newJCas()
//        UIMAUtil.jcas = jcas
//
//        jcas.setDocumentText(sentence)
//
//        runPipeline(jcas, engine)

        // -------------------------------------------------------------------
        // test the results
        // -------------------------------------------------------------------

//        assert select(type:Segment, 
//            filter:{it.coveredText==jcas.documentText}).size() == 1
    }
}
