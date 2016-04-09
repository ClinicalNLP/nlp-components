package clinicalnlp.dict.uima;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel

import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ExternalResourceDescription
import org.junit.Test

import clinicalnlp.dict.AbstractionSchema
import clinicalnlp.dict.DictModel
import clinicalnlp.dict.DictModelFactory
import clinicalnlp.dict.DictModelPool
import clinicalnlp.dsl.GroovyAnnotator
import clinicalnlp.dsl.UIMAUtil
import clinicalnlp.sent.SentenceDetector
import clinicalnlp.token.TokenAnnotator

import com.fasterxml.jackson.databind.ObjectMapper

class DictAnnotatorTest {

	@Test
	public void smokeTest() {

		// Sentence detector
		ExternalResourceDescription sentResDesc = ExternalResourceFactory.createExternalResourceDescription(
				opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
		AnalysisEngineDescription sentDesc = AnalysisEngineFactory.createEngineDescription(
				SentenceDetector,
				SentenceDetector.SENT_MODEL_KEY, sentResDesc)
		AnalysisEngine sentDetector = AnalysisEngineFactory.createEngine(sentDesc)
		assert sentDetector != null

		// Tokenizer
		ExternalResourceDescription tokenResDesc = ExternalResourceFactory.createExternalResourceDescription(
				opennlp.uima.tokenize.TokenizerModelResourceImpl.class, "file:models/en-token.bin")
		AnalysisEngineDescription tokenDesc = AnalysisEngineFactory.createEngineDescription(
				TokenAnnotator,
				TokenAnnotator.PARAM_CONTAINER_TYPE,
				'org.cleartk.token.type.Sentence',
				TokenAnnotator.TOKEN_MODEL_KEY, tokenResDesc)
		AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(tokenDesc)
		assert tokenizer != null

		// Dictionary
		AnalysisEngineDescription dictDesc = AnalysisEngineFactory.createEngineDescription(
				DictAnnotator,
				DictAnnotator.PARAM_CONTAINER_CLASS,
				'org.cleartk.token.type.Sentence',
				DictAnnotator.PARAM_TOKEN_CLASS,
				'org.cleartk.token.type.Token',
				DictAnnotator.PARAM_DICTIONARY_ID, 1
				)
		AnalysisEngine dictEngine = AnalysisEngineFactory.createEngine(dictDesc)
		assert dictEngine != null

		// Aggregate pipeline
		AggregateBuilder builder = new AggregateBuilder()
		builder.with {
			add createEngineDescription(GroovyAnnotator,
					GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy')
			add sentDesc
			add tokenDesc
			add dictDesc
		}
		AnalysisEngineDescription desc = builder.createAggregateDescription()
		AnalysisEngine engine = builder.createAggregate()
		assert engine != null


		// Load grammar
		ObjectMapper mapper = new ObjectMapper()
		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null

		TokenizerME tokenizerME = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizerME != null

		DictModel model1 = DictModelFactory.make(DictModelFactory.DICT_MODEL_TYPE_TRIE, schema, tokenizerME)
		assert model1 != null
		assert model1.get(["glioblastoma"]) != null

		DictModel model2 = DictModelFactory.make(DictModelFactory.DICT_MODEL_TYPE_PHRASE, schema, tokenizerME)
		assert model2 != null

		DictModelPool.put(1, model1)
		DictModelPool.put(2, model2)

		// Process text
		String text = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."
		JCas jcas = engine.newJCas()
		jcas.setDocumentText(text)
		engine.process(jcas)
		Collection<DictMatch> matches = UIMAUtil.select(type:DictMatch)
		assert matches.size() == 2
	}
}
