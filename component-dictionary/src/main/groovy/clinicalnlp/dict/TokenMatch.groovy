package clinicalnlp.dict

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
public class TokenMatch<Value> {
	Integer begin
	Integer end
	Value value;
			
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
		builder.append('match tokens: [')
		builder.append(begin)
		builder.append(', ')
		builder.append(end)
		builder.append('] value:')
		builder.append(value.toString())
		return builder.toString()
	}
}