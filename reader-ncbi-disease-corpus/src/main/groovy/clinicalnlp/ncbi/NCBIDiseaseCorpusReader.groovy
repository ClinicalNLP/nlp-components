package clinicalnlp.ncbi
import clinicalnlp.dsl.UIMAUtil
import org.apache.uima.collection.CollectionException
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException
import org.apache.uima.resource.ResourceSpecifier
import org.apache.uima.util.Progress
import org.cleartk.ne.type.NamedEntityMention
import org.cleartk.token.type.Sentence
/**
 * Created by WKT on 12/21/15.
 */
class NCBIDiseaseCorpusReader extends JCasCollectionReader_ImplBase {

    class TextFileFilter implements FilenameFilter {
        @Override
        boolean accept(File dir, String name) {
            return (name.endsWith('.txt') ? true : false)
        }
    }

    public static final String PARAM_CORPUS_DIR = "corpusDir"

    @ConfigurationParameter(name = "corpusDir", mandatory = true, description = "Directory containing corpus files")
    private String corpusDir

    private int counter = 0
    private ArrayList<File> files;

    @Override
    boolean initialize(ResourceSpecifier aSpecifier, Map<String, Object> aAdditionalParams) throws ResourceInitializationException {
        super.initialize(aSpecifier, aAdditionalParams)

        File dir = new File(this.corpusDir)
        if (!dir.exists()) { return false }
        this.files = dir.listFiles(new TextFileFilter())
        return true
    }

    @Override
    void getNext(JCas jCas) throws IOException, CollectionException {
        UIMAUtil.jcas = jCas

        ////JCas goldView = jCas.createView('GOLD')
        File textfile = this.files[this.counter++]
        String text = textfile.text
        jCas.setDocumentText(text)
        UIMAUtil.create(type:Sentence.class, begin:0, end:UIMAUtil.jcas.documentText.length())

        File goldfile = new File(textfile.getAbsolutePath().replaceAll(/\.txt/, '.ann'))
        assert goldfile.exists()
        this.parseGoldfile(jCas, goldfile)
    }

    @Override
    boolean hasNext() throws IOException, CollectionException {
        return (this.counter < this.files.size() ? true : false)
    }

    @Override
    Progress[] getProgress() {
        return new Progress[0]
    }

    private void parseGoldfile(JCas jCas, File goldfile) {
        goldfile.readLines().each { String line ->
            Collection<String> parts = line.split(/\t/)
            UIMAUtil.create(type:NamedEntityMention, begin:parts[0].toInteger(),
                    end:parts[1].toInteger(), mentionType:parts[3], mentionId:parts[4])
        }
    }
}
