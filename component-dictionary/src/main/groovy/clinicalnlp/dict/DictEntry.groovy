package clinicalnlp.dict

import groovy.transform.Immutable
import groovy.transform.ToString

@ToString
class DictEntry {
	String vocab
	String code
	String[] canonical
	Map<String, String> attrs = new HashMap<>()
}

