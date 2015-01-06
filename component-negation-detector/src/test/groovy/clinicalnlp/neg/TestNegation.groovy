package clinicalnlp.neg;

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
import clinicalnlp.type.NegatableNamedEntityMention

class TestNegation {

    public static class NamedEntityMentionMatcher extends JCasAnnotator_ImplBase {

        @Override
        public void process(JCas jCas) throws AnalysisEngineProcessException {
            Matcher matcher = jcas.documentText =~ /([A-Z].+\.)/
            matcher.each {
                Sentence sent = new Sentence(jCas)
                sent.begin = matcher.start(1)
                sent.end = matcher.end(1)
                sent.addToIndexes()
                println "Sentence: ${sent.coveredText}"
            }

            matcher = jcas.documentText =~ /(?i)(pneumonia|fever|cough|sepsis|weakness|measles)/
            matcher.each {
                NegatableNamedEntityMention nem = new NegatableNamedEntityMention(jCas)
                nem.begin = matcher.start(1)
                nem.end = matcher.end(1)
                nem.polarity = 1
                nem.addToIndexes()
                println "NamedEntityMention: ${nem.coveredText}"
            }
        }
    }

    @Test
    public void testNegationScope() {

        // -------------------------------------------------------------------
        // build and run a pipeline to generate annotations
        // -------------------------------------------------------------------

        def sentence = """\
        Patient has fever but no cough and pneumonia is ruled out.
        There is no increase in weakness.
        Patient does not have measles.
        """
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(createEngineDescription(NamedEntityMentionMatcher))
            add(createEngineDescription(GroovyAnnotator,
                    GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/NegEx.groovy'))
        }
        AnalysisEngine engine = builder.createAggregate()
        JCas jcas = engine.newJCas()
        UIMAUtil.jcas = jcas

        jcas.setDocumentText(sentence)

        runPipeline(jcas, engine)

        // -------------------------------------------------------------------
        // test the results
        // -------------------------------------------------------------------

        assert select(type:NegatableNamedEntityMention).size() == 5
        assert select(type:NegatableNamedEntityMention, 
            filter:and({it.coveredText=='fever'}, {it.polarity==1})).size() == 1
        assert select(type:NegatableNamedEntityMention,
            filter:and({it.coveredText=='cough'}, {it.polarity==-1})).size() == 1
        assert select(type:NegatableNamedEntityMention,
            filter:and({it.coveredText=='pneumonia'}, {it.polarity==-1})).size() == 1
        assert select(type:NegatableNamedEntityMention,
            filter:and({it.coveredText=='weakness'}, {it.polarity==1})).size() == 1
        assert select(type:NegatableNamedEntityMention,
            filter:and({it.coveredText=='measles'}, {it.polarity==-1})).size() == 1

    }
}
