package clinicalnlp.token
import clinicalnlp.dsl.UIMAUtil
import com.ibm.icu.text.BreakIterator
import groovy.util.logging.Log4j
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceInitializationException
import org.cleartk.token.type.Token
import org.codehaus.groovy.control.CompilerConfiguration

import static clinicalnlp.dsl.UIMAUtil.select
import static clinicalnlp.dsl.UIMAUtil.create
import static com.ibm.icu.text.BreakIterator.wordInstance

@Log4j
public final class BreakIteratorTokenAnnotator extends JCasAnnotator_ImplBase {
    public static final String PARAM_CONTAINER_TYPE = 'containerTypeName'
    @ConfigurationParameter(name = 'containerTypeName', mandatory = false,
            defaultValue = 'org.apache.uima.jcas.tcas.DocumentAnnotation')
    private String containerTypeName;

    public static final String PARAM_POST_PROCESS_SCRIPT_FILE = 'postProcessScriptFileName'
    @ConfigurationParameter(name = 'postProcessScriptFileName', mandatory = false)
    private String postProcessScriptFileName;


    private Script postProcessScript;
    private Class<Annotation> containerType;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context)
        try {
            this.containerType = Class.forName(this.containerTypeName)
        }
        catch (Exception e) {
            throw new ResourceInitializationException(e)
        }

        // initialize post-processing script
        if (this.postProcessScriptFileName != null) {
            CompilerConfiguration config = new CompilerConfiguration()
            config.setScriptBaseClass('clinicalnlp.dsl.UIMAUtil')
            GroovyShell shell = new GroovyShell(config)
            try {
                String scriptContents = ''
                scriptContents = this.getClass().getResource(this.postProcessScriptFileName).text
                this.postProcessScript = shell.parse(scriptContents)
            } catch (IOException e) {
                throw new ResourceInitializationException()
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        UIMAUtil.jcas = aJCas
        select(type: (this.containerType)).each {
            int offset = it.getBegin()
            BreakIterator boundary = wordInstance
            boundary.setText(it.coveredText)
            int begin = boundary.first()
            int end = boundary.next()
            while (end != BreakIterator.DONE) {
                if (!it.coveredText.substring(begin, end).allWhitespace) {
                    create(type:Token, begin:offset+begin, end:offset+end)
                }
                begin = end
                end = boundary.next()
            }
        }

        if (this.postProcessScript != null) {
            this.postProcessScript.run()
        }
    }


    public static void main(String[] args) {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                BreakIteratorTokenAnnotator)
        PrintWriter writer = new PrintWriter(new File(args[0]))
        desc.toXML(writer)
        writer.close()
    }
}
