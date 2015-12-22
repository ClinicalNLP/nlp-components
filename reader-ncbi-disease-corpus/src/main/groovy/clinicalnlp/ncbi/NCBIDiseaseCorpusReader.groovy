package clinicalnlp.ncbi

import clinicalnlp.dsl.UIMAUtil
import org.apache.uima.collection.CollectionException
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase
import org.apache.uima.jcas.JCas
import org.apache.uima.util.Progress
import org.cleartk.token.type.Sentence

import static clinicalnlp.dsl.UIMAUtil.getJcas

/**
 * Created by WKT on 12/21/15.
 */
class NCBIDiseaseCorpusReader extends JCasCollectionReader_ImplBase {
    ArrayList<String> texts = ["This is a test.", "This is another test."]
    int counter = 0

    @Override
    void getNext(JCas jCas) throws IOException, CollectionException {
        jCas.setDocumentText(this.texts.get(this.counter++))
        UIMAUtil.jcas = jCas
        UIMAUtil.create(type:Sentence.class, begin:0, end:jcas.documentText.length())
    }

    @Override
    boolean hasNext() throws IOException, CollectionException {
        return (this.counter < this.texts.size() ? true : false)
    }

    @Override
    Progress[] getProgress() {
        return new Progress[0]
    }
}
