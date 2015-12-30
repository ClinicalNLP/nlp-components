package clinicalnlp.token
import clinicalnlp.dsl.UIMAUtil
import groovy.util.logging.Log4j
import org.apache.log4j.BasicConfigurator
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ExternalResourceDescription
import org.cleartk.token.type.Sentence
import org.cleartk.token.type.Token
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

@Log4j
class TokenAnnotatorTest {

    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure()
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBreakIteratorTokenAnnotator() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                BreakIteratorTokenAnnotator,
                BreakIteratorTokenAnnotator.PARAM_CONTAINER_TYPE,
                'org.cleartk.token.type.Sentence')

        AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(desc)
        assert tokenizer != null


        // create a new JCas and seed with sentences
        String text = "There was a tubular adenoma in the sigmoid colon."
        JCas jcas = tokenizer.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Sentence, begin:0, end:text.length())

        // apply the tokenizer
        tokenizer.process(jcas)

        // verify number of BaseTokens
        Collection<Token> tokens = UIMAUtil.select(type:Token)
//        assert tokens.size() == 10
        tokens.each { println "'${it.coveredText}'" }
    }
    
    @Test
    public void testTokenAnnotator() {
		
		ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
			 opennlp.uima.tokenize.TokenizerModelResourceImpl.class, "file:models/en-token.bin")
		
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                TokenAnnotator,
                TokenAnnotator.PARAM_CONTAINER_TYPE,
                'org.cleartk.token.type.Sentence',
				TokenAnnotator.TOKEN_MODEL_KEY, extDesc)
		
        AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(desc)
        assert tokenizer != null
        

        // create a new JCas and seed with sentences
        String text = "There was a tubular adenoma in the sigmoid colon."
        JCas jcas = tokenizer.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Sentence, begin:0, end:text.length())

        // apply the tokenizer
        tokenizer.process(jcas)
        
        // verify number of BaseTokens
        Collection<Token> tokens = UIMAUtil.select(type:Token)
        assert tokens.size() == 10
		tokens.each { println it }
    }
}
