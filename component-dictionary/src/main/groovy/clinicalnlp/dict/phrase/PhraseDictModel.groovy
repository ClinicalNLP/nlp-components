package clinicalnlp.dict.phrase;

import groovy.util.logging.Log4j
import opennlp.tools.tokenize.SimpleTokenizer
import opennlp.uima.tokenize.Tokenizer
import clinicalnlp.dict.DictEntry
import clinicalnlp.dict.DictModel
import clinicalnlp.dict.LookupMatch

import com.wcohen.ss.JaroWinkler
import com.wcohen.ss.SoftTFIDF

@Log4j
public class PhraseDictModel implements DictModel {
		
	private Map<String[], DictEntry> entries = new HashMap<>()
	private PhraseTree phrases = new PhraseTree()
	
	private Boolean caseInsensitive;

	public PhraseDictModel(Boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}
	
	@Override
	public DictEntry get(String[] tokens) {
		return this.entries.get(this.join(tokens))
	}

	@Override
	public void add(final DictEntry entry) {
		this.phrases.addPhrase(this.transformArray(entry.canonical))
		this.entries.put(this.join(entry.canonical), entry)
		entry.variants.each {
			this.phrases.addPhrase(this.transformArray(it))
			this.entries.put(this.join(it), entry)
		}
	}
		
	@Override
	public Set<LookupMatch> findMatches(final String[] tokens) {
		Set<LookupMatch> matches = new HashSet<>()
		
		for (int i = 0; i < tokens.length; i++) {
			String[] tokensToEnd = tokens[i, tokens.length - 1]
			tokensToEnd = transformArray(tokensToEnd)
			Integer endMatchPosition = phrases.getLongestMatch(tokensToEnd)
			if (endMatchPosition != null) {
				String[] matchedTokens = Arrays.copyOfRange(tokensToEnd, 0, endMatchPosition)
				matches << new LookupMatch(
					begin:i,
					end:(i+endMatchPosition),
					entry:entries.get(this.join(matchedTokens))
					)
			}
		}
		
		return matches;
	}
	
	@Override
	public Set<LookupMatch> findMatches(String[] tokens, Double tolerance) {
		Set<LookupMatch> matches = new HashSet<>()
		
		Tokenizer tokenizer = new SimpleTokenizer(false,true);
		SoftTFIDF distance = new SoftTFIDF(tokenizer, new JaroWinkler(), tolerance);
		
		return matches;
	}


	private String[] transformArray(String[] tokens) {
		tokens.eachWithIndex { tok, i ->
			tokens[i] = transform(tok)
		}
		return tokens
	}

	private String transform(String token) {
		if (caseInsensitive) {
			token = token.toLowerCase()
		}
		return token
	}
	
	private String join(String[] tokens, String sep=' ') {
		StringJoiner joiner = new StringJoiner(sep)
		tokens.each {
			joiner.add(this.transform(it))
		}
		return joiner.toString()
	}
}
