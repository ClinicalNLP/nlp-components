package clinicalnlp.dict.trie

class TrieDictionary<Value> {
	private static class Node<Value> {
		private Value value
		private Map<Character, Node<Value>> next = new TreeMap<>()
	}
	
	Node<Value> root = new Node<>()
	int numEntries = 0

	public Value get(CharSequence key) {
		Node<Value> node = getNode(root, key, 0)
		return (node == null ? null : node.value)
	}
	
	private Node<Value> getNode(Node<Value> node, CharSequence key, int index) {
		if (index == key.length()) { return node }
		char c = key.charAt(index)
		return (node.next.containsKey(c) ? getNode(node.next[c], key, index+1) : null)
	}
	
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
}
