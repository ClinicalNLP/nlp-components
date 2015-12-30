package clinicalnlp.ncbi
import clinicalnlp.dsl.GroovyAnnotator
import clinicalnlp.token.TokenAnnotator
import groovy.util.logging.Log4j
import org.apache.log4j.BasicConfigurator
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.collection.CollectionReaderDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.CollectionReaderFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.pipeline.SimplePipeline
import org.apache.uima.resource.ExternalResourceDescription
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


@Log4j
class NCBIDiseaseCorpusReaderTest {

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
    public void smokeTest() {

        // Corpus collection reader
        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
                NCBIDiseaseCorpusReader.class,
                NCBIDiseaseCorpusReader.PARAM_CORPUS_DIR,
                '/Users/WKT/Data/NCBI_complete_corpus/trainset')

        // Tokenizer
        ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
                opennlp.uima.tokenize.TokenizerModelResourceImpl.class, "file:models/en-token.bin")
        AnalysisEngineDescription tokenizer = AnalysisEngineFactory.createEngineDescription(
                TokenAnnotator,
                TokenAnnotator.PARAM_CONTAINER_TYPE,
                'org.cleartk.token.type.Sentence',
                TokenAnnotator.TOKEN_MODEL_KEY, extDesc)

        // Printer
        AnalysisEngineDescription printer = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/NCBIDiseaseCorpusWriter.groovy')

        // Run the pipeline
        SimplePipeline.runPipeline(reader, tokenizer, printer);
    }
}
