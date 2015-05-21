package clinicalnlp.dict

import java.util.Collection;

import opennlp.tools.tokenize.Tokenizer
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.util.Span
import clinicalnlp.dict.phrase.PhraseDict
import clinicalnlp.dict.trie.TrieDict

class DictModelFactory {
	
	static public CharSequence TOKEN_SEP = ' '
	
	public static String DICT_MODEL_TYPE_PHRASE = "Phrase"
	public static String DICT_MODEL_TYPE_TRIE = "Trie"
	
	static public DictModel make(final String dictModelType, 
		final AbstractionSchema schema, 
		final Tokenizer tokenizer) {
		
		DictModel model;
		
		switch (dictModelType) {
			case DICT_MODEL_TYPE_TRIE: 
			model = new TrieDict<DictEntry>()
			break;
			
			case DICT_MODEL_TYPE_PHRASE:
			model = new PhraseDict<DictEntry>()
			break
		}
				
		schema.object_values.each { ObjectValue objVal ->
			DictEntry entry = new DictEntry()
			entry.vocab = objVal.vocabulary
			entry.code = objVal.vocabulary_code
			entry.canonical = tokenize(objVal.value, tokenizer)
			model.put(entry.canonical, entry)
			objVal.object_value_variants.each { ObjectValueVariant variant ->
				model.put(tokenize(variant.value, tokenizer), entry)
				////entry.variants << tokenize(variant.value, tokenizer)
			}

		}
		return model
	}
	
	static public Collection<CharSequence> tokenize(String phrase, TokenizerME tokenizer) {
		Collection<Sequence> tokens = new ArrayList<>()
		Span[] tokenSpans = tokenizer.tokenizePos(phrase)
		tokenSpans.each { Span span ->
			tokens << phrase.substring(span.getStart(), span.getEnd())
		}
		return tokens
	}

	static public String join(final Collection<CharSequence> tokens, boolean wrap = false) {
		return join(tokens as CharSequence[], wrap)
	}

	static public String join(final CharSequence[] tokens, boolean wrap = false) {
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
