package clinicalnlp.dict.uima

import org.apache.uima.resource.DataResource
import org.apache.uima.resource.ResourceInitializationException
import org.apache.uima.resource.SharedResourceObject

import clinicalnlp.dict.DictModel;

class DictModelResource implements SharedResourceObject {
	
	DictModel model;

	@Override
	public void load(DataResource aData) throws ResourceInitializationException {

	}

	DictModel getModel() { return model }
}
