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
import clinicalnlp.dict.stringdist.DynamicStringDist
import clinicalnlp.dict.stringdist.MinEditDist
import clinicalnlp.dict.trie.TrieDict.TokenMatch
import de.tudarmstadt.ukp.dkpro.core.io.text.*

@Log4j
class TestTrieDict {
	TrieDict<DictEntry> dict;
	Map<String, DictEntry> entries;
	
	@BeforeClass
	static void setupClass() {
		BasicConfigurator.configure()
	}

	
	@Before
	void setup() {
		log.setLevel(Level.INFO)
		
		this.dict = new TrieDict<DictEntry>()		
		this.entries = [
			'bee':(new DictEntry(vocab:'V1', code:'C1', canonical:'bee')),
			'bees':(new DictEntry(vocab:'V1', code:'C2', canonical:'bees'))
			]		
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
		matches.each { log.info it }
		assert matches.size() == 2
		
		matches = dict.findMatches(tokens, dist, 1.0)
		matches.each { log.info it }
		assert matches.size() == 4
	}
}
