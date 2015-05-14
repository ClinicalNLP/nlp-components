package clinicalnlp.dict.trie

import clinicalnlp.dict.stringdist.DynamicStringDist

class TrieDictionary<Value> {
	private static class Node<Value> {
		private Value value
		private Map<Character, Node<Value>> next = new TreeMap<>()
	}
	
	private static class SearchState<Value> {
		Node<Value> node;
		int index = 0;
		SearchState(Node<Value> node) { this.node = node }
	}
	
	public static class TokenMatch<Value> {
		Integer[] tokenPositions;
		Value value;
		
		public TokenMatch(Integer[] tokenPositions, Value value) {
			this.tokenPositions = tokenPositions
			this.value = value
		}
	}

	
	Node<Value> root = new Node<>()
	int numEntries = 0

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
		return (node.next.containsKey(c) ? getNode(node.next[c], key, index+1) : null)
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
		if (!node.next.containsKey(c)) { node.next[c] = new Node<Value>() }
		node.next[c] = putNode(node.next[c], key, value, index+1)
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
		
		Collection<TokenMatch<Value>> matches = new ArrayList<>();
		
		dist.addTextToMatch(tokens);

		// traverse trie to find matches
		Stack<SearchState<Value>> agenda = new Stack<>();
		agenda.push(new SearchState<Value>(this.root));
		while (!agenda.isEmpty()) {
			SearchState<Value> ss = agenda.peek();
			if (ss.index >= ss.node.next.size()) { dist.removeMatchChar(); agenda.pop(); }
			else {
//				dist.appendMatchChar();
//				if (dist.getMinScore() > tolerance) {
//					dist.removeMatchChar();
//				}
//				else {
//					Node<Value> nextNode = ss.node.next[ss.c];
//					agenda.push(new SearchState<Value>(nextNode));
//					if (nextNode.val != null) {
//						Collection<Integer[]> strm = dist.getMatches(tolerance);
//						for (Integer[] tokenPos : strm) {
//							matches.add(new TokenMatch<Value>(tokenPos, nextNode.val));
//						}
//					}
//				}
			}
			ss.c++;
		}

		return matches;
	}
}
