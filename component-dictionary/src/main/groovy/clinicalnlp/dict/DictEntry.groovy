package clinicalnlp.dict

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import groovy.transform.ToString

@Immutable
@ToString
@EqualsAndHashCode
class DictionaryEntry {
	String vocabulary
	String code
	String[] canonical
	Map<String, String> attrs = new HashMap<>()
}

