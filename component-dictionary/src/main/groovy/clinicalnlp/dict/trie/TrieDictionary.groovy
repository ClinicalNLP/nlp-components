package clinicalnlp.dict.trie

import clinicalnlp.dict.stringdist.DynamicStringDist

/**
 * 
 * @author Will Thompson
 *
 * @param <Value>
 */
class TrieDictionary<Value> {

	// ------------------------------------------------------------------------
	// Inner Classes
	// ------------------------------------------------------------------------
	
	private static class Node<Value> implements Comparable {
		char c
		Value value
		@SuppressWarnings("rawtypes")
		Node<Value>[] next = new Node<Value>[0]
		
		@Override
		public int compareTo(Object other) {
			if (other instanceof Node<Value>) {
				return Integer.compare(c, ((Node<Value>)other).c)
			}
			else if (other instanceof Character) {
				return Character.compare(c, (Character)other)
			}
		}
	}	
	
	private static class SearchState<Value> {
		Node<Value> node;
		int index = 0;
		SearchState(Node<Value> node) { this.node = node }
	}
	
	public static class TokenMatch<Value> {
		Integer[] tokenIndices;
		Value value;
		
		public TokenMatch(Integer[] tokenIndices, Value value) {
			this.tokenIndices = tokenIndices
			this.value = value
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder()
			builder.append('match tokens: [')
			builder.append(tokenIndices[0])
			builder.append(', ')
			builder.append(tokenIndices[1])
			builder.append('] value:')
			builder.append(value.toString())
			return builder.toString()
		}
	}
	
	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	Node<Value> root = new Node<>()
	int numEntries = 0

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Value get(CharSequence key) {
		Node<Value> node = getNode(root, key, 0)
		return (node == null ? null : node.value)
	}
	
	private Node<Value> getNode(Node<Value> node, CharSequence key, int index) {
		if (index == key.length()) { return node }
		char c = key.charAt(index)
		int idx = Arrays.binarySearch(node.next, c)
		return (idx < 0 ? null : getNode(node.next[idx], key, index+1))
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(CharSequence key, Value value) {
		root = putNode(root, key, value, 0)
	}
			
	private Node<Value> putNode(Node<Value> node, CharSequence key, Value value, int index) {
		if (index == key.length()) {
			if (node.value == null) { this.numEntries++ }
			node.value = value
			return node
		}
		char c = key.charAt(index)
		int idx = Arrays.binarySearch(node.next, c)
		if (idx < 0) {
			Node<Value>[] newNext = Arrays.copyOf(node.next, node.next.length+1)
			newNext[node.next.length] = new Node<Value>(c:c, value:null)
			Arrays.sort(newNext)
			node.next = newNext
			idx = Arrays.binarySearch(node.next, c)
		}
		node.next[idx] = putNode(node.next[idx], key, value, index+1)
		return node
	}	

	/**
	 * 
	 * @param tokens
	 * @param dist
	 * @param tolerance
	 * @return
	 */
	public Collection<TokenMatch<Value>> findMatches(
		final Collection<CharSequence> tokens,
		final DynamicStringDist dist,
		final Double tolerance) {

		// populate stringdist object with tokens
		dist.addTextToMatch(tokens);
		
		// traverse trie to find matches
		Collection<TokenMatch<Value>> matches = new ArrayList<>();
		Stack<SearchState<Value>> agenda = new Stack<>();
		agenda.push(new SearchState<Value>(this.root));
		while (!agenda.isEmpty()) {
			SearchState<Value> ss = agenda.peek();
			if (ss.index >= ss.node.next.length) { dist.removeMatchChar(); agenda.pop(); }
			else {
				// TODO: change this method to reject addtion if min score is less than tolerance
				// TODO: remove the getMinScore() method
				dist.appendMatchChar(ss.node.next[ss.index].c);
				if (dist.getMinScore() > tolerance) {
					dist.removeMatchChar();
				}
				else {
					Node<Value> nextNode = ss.node.next[ss.index]
					agenda.push(new SearchState<Value>(nextNode))
					if (nextNode.value != null) {
						Collection<Integer[]> strm = dist.getMatches(tolerance);
						for (Integer[] tokenPos : strm) {
							matches.add(new TokenMatch<Value>(tokenPos, nextNode.value));
						}
					}
				}
			}
			ss.index++;
		}
		// return matches
		return matches;
	}
}
