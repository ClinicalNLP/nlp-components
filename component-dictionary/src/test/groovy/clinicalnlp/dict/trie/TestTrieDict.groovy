package clinicalnlp.dict.trie;

import static clinicalnlp.dsl.UIMAUtil.*
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline
import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import clinicalnlp.dict.DictEntry
import clinicalnlp.dict.TokenMatch
import clinicalnlp.dict.stringdist.DynamicStringDist
import clinicalnlp.dict.stringdist.MinEditDist

@Log4j
class TestTrieDict {
	TrieDict<DictEntry> dict;
	Map<Collection<CharSequence>, DictEntry> entries;
	
	@BeforeClass
	static void setupClass() {
		BasicConfigurator.configure()
	}

	
	@Before
	void setup() {
		log.setLevel(Level.INFO)
		
		this.dict = new TrieDict<DictEntry>()		
		this.entries = [
			['bee']:(new DictEntry(vocab:'V1', code:'C1', canonical:'bee')),
			['bees']:(new DictEntry(vocab:'V1', code:'C2', canonical:'bees'))
			]		
		this.entries.each { Collection<CharSequence> k, DictEntry v ->
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
		Set<TokenMatch> matches = this.dict.matches(tokens, dist, 0.0)
		matches.each { log.info it }
		assert matches.size() == 2
		
		matches = this.dict.matches(tokens, dist, 1.0)
		matches.each { log.info it }
		assert matches.size() == 4
	}
}
