package clinicalnlp.dict.stringdist
import groovy.transform.Immutable
import groovy.transform.ToString


public class MinEditDist implements DynamicStringDist {
	
	private static class BackPtr implements Comparable {
		Double score;
		Integer startPos;
		
		@Override
		public int compareTo(Object other) {
			return Double.compare(score, ((BackPtr)other).score) 
		}
		
		@Override
		public String toString() {
			return this.score.toString();
		}
	}
		
	CharSequence text;
	StringBuilder match = new StringBuilder()
	Stack<BackPtr[]> rows = new Stack<>()
	Map<Integer, Integer> tokenPosMap = new TreeMap<>()
	
	@Override
	public Double appendMatchChar(final char c) {
		match.append(c);
		println ("Append: [${match}]")
		BackPtr[] toprow = rows.peek()
		BackPtr[] newrow = new BackPtr[toprow.size()]
		newrow[0] = new BackPtr(score:(toprow[0].score + 1), startPos:0)
		for (int i = 1; i < newrow.length; i++) {
			def bptrs = [
				new BackPtr(startPos:toprow[i-1].startPos, score:(c == text.charAt(i-1) ? toprow[i-1].score : toprow[i-1].score + 1 )),
				new BackPtr(startPos:toprow[i].startPos, score:(toprow[i].score + 1)),
				new BackPtr(startPos:toprow[i-1].startPos, score:(newrow[i-1].score + 1))
				]
			newrow[i] = GroovyCollections.min(bptrs)
		}
		Double minScore = GroovyCollections.min(newrow).score
		
		rows.push(newrow)
		
		rows.each { BackPtr[] row ->
			println row
		}
		
		return 0.0;
	}

	@Override
	public void removeMatchChar() {
		if (match.length() == 0) { return; }
		match.deleteCharAt(match.length()-1)
		assert this.rows.size() > 1
		this.rows.pop()
		println ("Remove: [${match}]")
	}

	@Override
	public void addTextToMatch(final Collection<CharSequence> tokens) {
		if (tokens == null) { throw new NullPointerException() }
		if (tokens.size() == 0) { throw new IllegalArgumentException() }		
		
		StringJoiner joiner = new StringJoiner(' ');
		for (CharSequence token : tokens) {
			joiner.add(token);
		}
		this.text = joiner.toString()
		println this.text
		
		BackPtr[] bottomRow = new BackPtr[this.text.length()+1]
		bottomRow[0] = new BackPtr(score:0, startPos:0)
		this.tokenPosMap[0] = 0
		int strIndex = 1
		int tokIndex = 0
		for (int i = 0; i < text.size(); i++) {
			if (text[i] == ' ') { strIndex = 0; tokIndex++ }
			bottomRow[i+1] = new BackPtr(startPos:i, score:strIndex++)
			this.tokenPosMap[i+1] = tokIndex
		}
		rows.push(bottomRow)
	}
	
	@Override
	public Collection<Integer[]> getMatches(final Double tolerance) {
		Collection<Integer[]> matches = new ArrayList<>()
		BackPtr[] toprow = rows.peek()
		toprow.eachWithIndex { BackPtr bptr, Integer idx ->
			if (bptr.score <= tolerance) {
				println "Match found: ${bptr.startPos}, ${idx}"
				matches << ([
					this.tokenPosMap[bptr.startPos],
					this.tokenPosMap[idx]
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
