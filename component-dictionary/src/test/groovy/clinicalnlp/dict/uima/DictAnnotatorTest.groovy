package clinicalnlp.dict.uima;

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
				'org.cleartk.token.type.Token'
				)
		AnalysisEngine dictEngine = AnalysisEngineFactory.createEngine(dictDesc)
		assert dictEngine != null

		// Aggregate pipeline
		AggregateBuilder builder = new AggregateBuilder()
		builder.with {
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

		DictModel model2 = DictModelFactory.make(DictModelFactory.DICT_MODEL_TYPE_PHRASE, schema, tokenizerME)
		assert model2 != null

		DictModelPool.put(1, model1)
		DictModelPool.put(2, model2)

		// Process text
		String text = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."
		JCas jcas = engine.newJCas()
		jcas.setDocumentText(text)
		engine.process(jcas)
	}
}
