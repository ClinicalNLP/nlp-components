package clinicalnlp.dict.uima

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

class DictAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context)
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
	}

}
