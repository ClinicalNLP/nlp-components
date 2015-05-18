package clinicalnlp.dict;

import java.util.Set;

public interface DictModel<Value> {
	
	public Value get (final String[] tokens);
	
	public void put (final String[] tokens, final Value entry);
	
	public Set<TokenMatch> matches (final String[] tokens);
	
	public Set<TokenMatch> matches (final String[] tokens, Double tolerance);
}
