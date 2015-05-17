package clinicalnlp.dict.trie;

import static clinicalnlp.dsl.UIMAUtil.*
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import clinicalnlp.dict.DynamicStringDist;
import clinicalnlp.dict.MinEditDist;
import clinicalnlp.dict.trie.TrieDictionary.TokenMatch
import de.tudarmstadt.ukp.dkpro.core.io.text.*

class TestDictionary {
	TrieDictionary dict;
	Map entries;
	
	@Before
	void setup() {
		this.entries = [
			'bee':'[BEE]',
			'bees':'[BEES]'
			]
		
		this.dict = new TrieDictionary()
		this.entries.each { k,v ->
			dict.put(k, v)
		}
	}

    @Test
    public void smokeTest() {
		assert dict.numEntries == entries.size()
		entries.each { k,v ->
			assert dict.get(k) == v
		}
    }
	
	@Test
	public void findMatches() {
		Collection<CharSequence> tokens = new ArrayList<>()
		tokens << 'bee' << 'bees'
		
		DynamicStringDist dist = new MinEditDist()
		Collection<TokenMatch> matches = dict.findMatches(tokens, dist, 0.0)
		matches.each { println it }
		assert matches.size() == 2
	}
}
