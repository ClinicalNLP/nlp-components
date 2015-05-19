package clinicalnlp.dict.trie

import clinicalnlp.dict.DictModel
import clinicalnlp.dict.DictModelFactory
import clinicalnlp.dict.TokenMatch
import clinicalnlp.dict.stringdist.DynamicStringDist


/**
 * 
 * @author Will Thompson
 *
 * @param <Value>
 */
class TrieDict<Value> implements DictModel<Value> {

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
				return Character.compare(c, ((Node<Value>)other).c)
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
	
	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	Node<Value> root = new Node<>()
	int numEntries = 0

	// ------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------
	
	@Override
	public Integer getNumEntries() { return numEntries; }

	@Override	
	public Value get (final Collection<CharSequence> tokens) {
		return (getNode(root,DictModelFactory.join(tokens), 0))?.value
	}
	
	private Node<Value> getNode(Node<Value> node, CharSequence key, int index) {
		if (index == key.length()) { return node }
		char c = key.charAt(index)
		int idx = Arrays.binarySearch(node.next, c)
		return (idx < 0 ? null : getNode(node.next[idx], key, index+1))
	}
	
	@Override
	public void put(final Collection<CharSequence> keyTokens, final Value value) {
		root = putNode(root, DictModelFactory.join(keyTokens), value, 0)
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
	
	@Override
	public Set<TokenMatch> matches(final Collection<CharSequence> tokens) {
		return null;
	}

	@Override
	public Set<TokenMatch<Value>> matches(final Collection<CharSequence> tokens,
		final DynamicStringDist dist,
		final Double tolerance) {

		Set<TokenMatch<Value>> matches = new HashSet<>();
		
		// initialize string distance instance with tokens
		dist.init(tokens);
		
		// traverse trie to find matches
		Stack<SearchState<Value>> agenda = new Stack<>();
		agenda.push(new SearchState<Value>(this.root));
		while (!agenda.isEmpty()) {
			SearchState<Value> topSS = agenda.peek();
		
			// if the top search state is exhausted, pop it off the agenda
			if (topSS.index >= topSS.node.next.length) { 
				dist.pop(); agenda.pop(); 
			}
			// evaluate top search state
			else if (dist.push(topSS.node.next[topSS.index].c) > tolerance) {
				dist.pop();
			}
			// examine current search state and look for matches
			else {
				Node<Value> nextNode = topSS.node.next[topSS.index]
				agenda.push(new SearchState<Value>(nextNode))
				if (nextNode.value != null) {
					Collection<Integer[]> strm = dist.matches(tolerance);
					for (Integer[] tokenIndices : strm) {
						matches.add(new TokenMatch<Value>(begin:tokenIndices[0], 
							end:tokenIndices[1], value:nextNode.value));
					}
				}
			}
			// advance index to next child state
			topSS.index++;
		}

		// return matches with scores inside tolerance
		return matches;
	}
}
