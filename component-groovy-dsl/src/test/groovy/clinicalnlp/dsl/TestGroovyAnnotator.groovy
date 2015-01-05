package clinicalnlp.dsl;

import static clinicalnlp.dsl.UIMAUtil.*
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline
import static org.junit.Assert.*

import java.util.regex.Matcher

import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.jcas.JCas
import org.cleartk.ne.type.NamedEntityMention
import org.cleartk.token.type.Sentence
import org.junit.Test

class TestGroovyAnnotator {

    @Test
    public void test() {
        def text = """\
        Patient has fever but no cough and pneumonia is ruled out.
        There is no increase in weakness.
        Patient does not have measles.
        """
        
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(createEngineDescription(GroovyAnnotator, 
                GroovyAnnotator.PARAM_SCRIPT_FILE,
                'groovy/TestSentenceDetector.groovy'))
        }
        AnalysisEngine engine = builder.createAggregate()
        JCas jcas = engine.newJCas()
        UIMAUtil.jcas = jcas
        jcas.setDocumentText(text)
        runPipeline(jcas, engine)
        assert select(type:Sentence).size() == 3
        
        builder = new AggregateBuilder()
        builder.with {
            add(createEngineDescription(GroovyAnnotator,
                GroovyAnnotator.PARAM_SCRIPT_FILE,
                'groovy/TestConceptDetector.groovy'))
        }
        engine = builder.createAggregate()
        jcas = engine.newJCas()
        UIMAUtil.jcas = jcas
        jcas.setDocumentText(text)
        runPipeline(jcas, engine)
        assert select(type:NamedEntityMention).size() == 4
    }
}
