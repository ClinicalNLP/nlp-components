package clinicalnlp.dict.trie;

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
    public void insertEntries() {
		Map vocab = [
			'bee':'[BEE]',
			'bees':'[BEES]'
			]
		
		TrieDictionary dict = new TrieDictionary()
		vocab.each { k,v ->
			dict.put(k, v)
		}
		vocab.each { k,v ->
			assert dict.get(k) == v
		}
    }
}
