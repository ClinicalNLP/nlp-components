package clinicalnlp.dict

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
public class TokenMatch<Value> {
	Integer startTokenIdx
	Integer endTokenIdx
	Value value;
			
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
		builder.append('match tokens: [')
		builder.append(startTokenIdx)
		builder.append(', ')
		builder.append(endTokenIdx)
		builder.append('] value:')
		builder.append(value.toString())
		return builder.toString()
	}
}