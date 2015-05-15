package clinicalnlp.dict.stringdist
import groovy.transform.Immutable
import groovy.transform.ToString


public class MinEditDist implements DynamicStringDist {
	
	static String TOKEN_SEP_CHAR = ' '
	
	private static class BackPtr implements Comparable {
		Double score
		Integer startRowIndex // bottom row index
		
		@Override
		public int compareTo(Object other) {
			return Double.compare(score, ((BackPtr)other).score) 
		}
		
		@Override
		public String toString() {
			return this.score.toString()
		}
	}
	
	CharSequence text
	StringBuilder match = new StringBuilder()
	Stack<BackPtr[]> rows = new Stack<>()
	
	@Override
	public void addTextToMatch(final Collection<CharSequence> tokens) {
		if (tokens == null) { throw new NullPointerException() }
		if (tokens.size() == 0) { throw new IllegalArgumentException() }
		
		StringBuilder builder = new StringBuilder()
		builder.append(TOKEN_SEP_CHAR)
		for (CharSequence token : tokens) {
			builder.append(token)
			builder.append(TOKEN_SEP_CHAR)
		}
		this.text = builder.toString()
		
//		StringJoiner joiner = new StringJoiner(TOKEN_SEP_CHAR)
//		for (CharSequence token : tokens) {
//			joiner.add(token)
//		}
//		this.text = joiner.toString()
		println "Text added: '${this.text}'"
		
		BackPtr[] bottomRow = new BackPtr[this.text.length()]
//		bottomRow[0] = new BackPtr(score:0, startRowIndex:0)
		int score = 0
		for (int i = 0; i < text.size(); i++) {
			if (text[i] == TOKEN_SEP_CHAR) { score = 0 }
			bottomRow[i] = new BackPtr(startRowIndex:i, score:score++)
		}
		rows.push(bottomRow)
	}
	
	@Override
	public Double appendMatchChar(final char c) {
		match.append(c)
		println ("Append: [${match}]")
		BackPtr[] toprow = rows.peek()
		BackPtr[] newrow = new BackPtr[toprow.length]
		newrow[0] = new BackPtr(score:(toprow[0].score + 1), startRowIndex:0)
		for (int i = 1; i < newrow.length; i++) {
			def bptrs = [
				new BackPtr(startRowIndex:toprow[i-1].startRowIndex, score:(c == text.charAt(i) ? toprow[i-1].score : toprow[i-1].score + 1 )),
				new BackPtr(startRowIndex:toprow[i].startRowIndex, score:(toprow[i].score + 1)),
				new BackPtr(startRowIndex:newrow[i-1].startRowIndex, score:(newrow[i-1].score + 1))
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
		if (match.length() == 0) { return }
		match.deleteCharAt(match.length()-1)
		this.rows.pop()
		println ("Remove: [${match}]")
	}
	
	@Override
	public Collection<Integer[]> getMatches(final Double tolerance) {
		Collection<Integer[]> matches = new ArrayList<>()
		BackPtr[] toprow = rows.peek()
		toprow.eachWithIndex { BackPtr bptr, Integer idx ->
			if (bptr.score <= tolerance && text.charAt(idx+1) == TOKEN_SEP_CHAR) {
				println "Match found: ${bptr.startRowIndex}, ${idx}; substring: ${text.subSequence(bptr.startRowIndex+1, idx+1)}"
				matches << ([
					bptr.startRowIndex,
					idx
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
