package clinicalnlp.dict.stringdist
import groovy.transform.Immutable
import groovy.transform.ToString


public class MinEditDist implements DynamicStringDist {
		
	// ------------------------------------------------------------------------
	// Inner Classes
	// ------------------------------------------------------------------------

	private static class BackPtr implements Comparable {
		Double score
		Integer startIndex
		
		@Override
		public int compareTo(Object other) {
			return Double.compare(score, ((BackPtr)other).score) 
		}
		
		@Override
		public String toString() {
			return (this.score.toString() + "(" + this.startIndex + ")")
		}
	}
	
	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	static char TOKEN_SEP_CHAR = ' '
	
	CharSequence text
	StringBuilder dictEntryPrefix = new StringBuilder()
	Stack<BackPtr[]> rows = new Stack<>()
	Map<Integer, Integer> str2tok = new TreeMap<>()
	
	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	@Override
	public void addTextToMatch(final Collection<CharSequence> tokens) {
		if (tokens == null) { throw new NullPointerException() }
		if (tokens.size() == 0) { throw new IllegalArgumentException("must have at least one token to match") }
		
		StringBuilder builder = new StringBuilder()
		builder.append(TOKEN_SEP_CHAR)
		for (CharSequence token : tokens) {
			builder.append(token)
			builder.append(TOKEN_SEP_CHAR)
		}
		this.text = builder.toString()
		
		println "Text added: [${this.text}]"
		
		BackPtr[] bottomRow = new BackPtr[this.text.length()]
		int score = 0
		int tokIdx = -1
		for (int i = 0; i < text.size(); i++) {
			if (text[i] == TOKEN_SEP_CHAR) { score = 0; tokIdx++ }
			else { this.str2tok[i] = tokIdx; }
			bottomRow[i] = new BackPtr(startIndex:i, score:score++)
		}
		rows.push(bottomRow)
	}
	
	@Override
	public Double appendMatchChar(final char c) {
		dictEntryPrefix.append(c)
		println ("Append: [${dictEntryPrefix}]")
		BackPtr[] toprow = rows.peek()
		BackPtr[] newrow = new BackPtr[toprow.length]
		newrow[0] = new BackPtr(score:(toprow[0].score + 1), startIndex:0)
		for (int i = 1; i < newrow.length; i++) {
			def bptrs = [
				new BackPtr(startIndex:toprow[i-1].startIndex, score:(c == text.charAt(i) ? toprow[i-1].score : toprow[i-1].score + 1 )),
				new BackPtr(startIndex:toprow[i].startIndex, score:(toprow[i].score + 1)),
				new BackPtr(startIndex:newrow[i-1].startIndex, score:(newrow[i-1].score + 1))
				]
			newrow[i] = GroovyCollections.min(bptrs)
		}
		Double minScore = GroovyCollections.min(newrow).score
		rows.push(newrow)
		rows.each { BackPtr[] row ->
			println row
		}
		return minScore
	}

	@Override
	public void removeMatchChar() {
		if (dictEntryPrefix.length() == 0) { return }
		dictEntryPrefix.deleteCharAt(dictEntryPrefix.length()-1)
		this.rows.pop()
		println ("Remove: [${dictEntryPrefix}]")
	}
	
	@Override
	public Collection<Integer[]> getMatches(final Double tolerance) {
		Collection<Integer[]> matches = new ArrayList<>()
		BackPtr[] toprow = rows.peek()
		toprow.eachWithIndex { BackPtr bptr, Integer endIndex ->
			if (bptr.score <= tolerance && text.charAt(endIndex+1) == TOKEN_SEP_CHAR) {
				println "Match found: ${bptr.startIndex}, ${endIndex}; substring: ${text.subSequence(bptr.startIndex+1, endIndex+1)}"
				matches << ([
					(text.charAt(bptr.startIndex) == TOKEN_SEP_CHAR ? this.str2tok[bptr.startIndex+1] : this.str2tok[bptr.startIndex]),
					(text.charAt(endIndex) == TOKEN_SEP_CHAR ? this.str2tok[endIndex-1] : this.str2tok[endIndex]),
					] as Integer[])
			}
		}
		return matches
	}

	@Override
	public Double getMinScore() {
		return GroovyCollections.min(rows.peek()).score
	}
}
