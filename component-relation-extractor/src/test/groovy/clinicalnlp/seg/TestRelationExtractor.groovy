package clinicalnlp.seg;

import static clinicalnlp.dsl.UIMAUtil.*
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline
import static org.junit.Assert.*

import java.util.regex.Matcher

import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.jcas.JCas
import org.cleartk.ne.type.NamedEntityMention
import org.cleartk.token.type.Sentence
import org.junit.Test

import clinicalnlp.dsl.GroovyAnnotator
import clinicalnlp.dsl.UIMAUtil
import clinicalnlp.type.Relation
import de.tudarmstadt.ukp.dkpro.core.io.text.*

class TestRelationExtractor {
	
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
		
					matcher = jcas.documentText =~ /(?i)(tubular adenoma|sigmoid colon)/
					matcher.each {
						NamedEntityMention nem = new NamedEntityMention(jCas)
						nem.begin = matcher.start(1)
						nem.end = matcher.end(1)
						nem.addToIndexes()
						println "NamedEntityMention: ${nem.coveredText}"
					}
				}
			}

    @Test
    public void testRelations() {

        // -------------------------------------------------------------------
        // build and run a pipeline to generate annotations
        // -------------------------------------------------------------------

        def sentence = """\
        FINDINGS:
        Patient has a tubular adenoma in the sigmoid colon
        """
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(createEngineDescription(NamedEntityMentionMatcher))
            add(createEngineDescription(GroovyAnnotator,
                    GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/TestRelationExtractor.groovy'))
        }
        AnalysisEngine engine = builder.createAggregate()
        JCas jcas = engine.newJCas()
        UIMAUtil.jcas = jcas

        jcas.setDocumentText(sentence)

        runPipeline(jcas, engine)

        // -------------------------------------------------------------------
        // test the results
        // -------------------------------------------------------------------

		assert select(type:NamedEntityMention).size() == 2
        assert select(type:Relation).size() == 1
    }
}
