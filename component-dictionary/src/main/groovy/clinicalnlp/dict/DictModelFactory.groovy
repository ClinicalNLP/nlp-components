package clinicalnlp.dict

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.util.Span
import clinicalnlp.dict.phrase.PhraseDictModel

class DictModelFactory {
	
	public static String DICT_MODEL_TYPE_PHRASE = "Phrase"
	public static String DICT_MODEL_TYPE_TRIE = "Trie"
	
	static public DictModel make(final String dictModelType, 
		final AbstractionSchema schema, 
		final TokenizerME tokenizer, 
		final Boolean caseInsensitive) {
		
		DictModel model;
		
		switch (dictModelType) {
//			case DICT_MODEL_TYPE_TRIE: 
//			model = new TrieDictModel(caseInsensitive)
//			break;
			
			case DICT_MODEL_TYPE_PHRASE:
			model = new PhraseDictModel(caseInsensitive)
			break
		}
				
		schema.object_values.each { ObjectValue objVal ->
			DictEntry entry = new DictEntry()
			entry.vocab = objVal.vocabulary
			entry.code = objVal.vocabulary_code
			entry.canonical = tokenize(objVal.value, tokenizer)
			objVal.object_value_variants.each { ObjectValueVariant variant ->
				entry.variants << tokenize(variant.value, tokenizer)
			}
			model.add(entry)
		}
		return model
	}
	
	static public String[] tokenize(String phrase, TokenizerME tokenizer) {
		Collection<String> tokens = new ArrayList<>()
		Span[] tokenSpans = tokenizer.tokenizePos(phrase)
		tokenSpans.each { Span span ->
			tokens << phrase.substring(span.getStart(), span.getEnd())
		}
		return tokens
	}
}
