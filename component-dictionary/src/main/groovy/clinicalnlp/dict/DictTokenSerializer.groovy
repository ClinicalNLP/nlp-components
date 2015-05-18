package clinicalnlp.dict

import opennlp.tools.tokenize.Tokenizer
import opennlp.tools.util.Span

class DictTokenSerializer {
	static public CharSequence TOKEN_SEP = ' '
	
	static public String serialize(final Collection<CharSequence> tokens, boolean wrap = false) {
		StringJoiner joiner;
		if (wrap == true) {
			joiner = new StringJoiner(TOKEN_SEP, TOKEN_SEP, TOKEN_SEP)
		}
		else {
			joiner = new StringJoiner(TOKEN_SEP)
		}
		tokens.each { joiner.add(it) }
		return joiner.toString()
	}
}
