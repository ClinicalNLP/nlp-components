package clinicalnlp.dict.phrase;

import groovy.util.logging.Log4j
import opennlp.tools.tokenize.SimpleTokenizer
import opennlp.uima.tokenize.Tokenizer
import clinicalnlp.dict.DictEntry
import clinicalnlp.dict.DictModel
import clinicalnlp.dict.DictModelFactory
import clinicalnlp.dict.TokenMatch
import clinicalnlp.dict.stringdist.DynamicStringDist

import com.wcohen.ss.JaroWinkler
import com.wcohen.ss.SoftTFIDF

@Log4j
public class PhraseDict<Value> implements DictModel<Value> {
		
	private Map<Collection<CharSequence>, Value> entries = new HashMap<>()
	private PhraseTree phrases = new PhraseTree()
	
	private Boolean caseInsensitive;

	public PhraseDict(Boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}
	
	@Override
	public DictEntry get(final Collection<CharSequence> tokens) {
		return this.entries.get(DictModelFactory.join(tokens))
	}
	
	@Override
	public void put (final Collection<CharSequence> tokens, final Value entry) {
		Collection<CharSequence> transformedTokens = this.transform(tokens)
		this.phrases.addPhrase(transformedTokens)
		this.entries.put(DictModelFactory.join(transformedTokens), entry)
	}
		
	@Override
	public Set<TokenMatch> matches(final Collection<CharSequence> tokens) {
		Set<TokenMatch> matches = new HashSet<>()
		
		for (int i = 0; i < tokens.size(); i++) {
			String[] tokensToEnd = tokens[i, tokens.size() - 1]
			tokensToEnd = transform(tokensToEnd)
			Integer endMatchPosition = phrases.getLongestMatch(tokensToEnd)
			if (endMatchPosition != null) {
				String[] matchedTokens = Arrays.copyOfRange(tokensToEnd, 0, endMatchPosition)
				matches << new TokenMatch(
					begin:i,
					end:(i+endMatchPosition),
					entry:entries.get(DictModelFactory.join(matchedTokens))
					)
			}
		}
		
		return matches;
	}
	
	@Override
	public Set<TokenMatch> matches(final Collection<CharSequence> tokens, final DynamicStringDist dist, Double tolerance) {
		Set<TokenMatch> matches = new HashSet<>()
		
		Tokenizer tokenizer = new SimpleTokenizer(false,true);
		SoftTFIDF distance = new SoftTFIDF(tokenizer, new JaroWinkler(), tolerance);
		
		return matches;
	}
	
	private Collection<CharSequence> transform(Collection<CharSequence> tokens) {
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
}
