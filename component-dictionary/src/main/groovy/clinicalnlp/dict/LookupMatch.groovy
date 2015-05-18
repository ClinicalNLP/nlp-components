package clinicalnlp.dict

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true)
@EqualsAndHashCode
class LookupMatch {
	Integer begin
	Integer end
	DictEntry entry;
}
