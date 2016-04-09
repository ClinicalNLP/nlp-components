package clinicalnlp.dict;

import clinicalnlp.dict.stringdist.DynamicStringDist
import java.util.Set
import java.util.Collection

public interface DictModel<Value> {
	
	public Integer getNumEntries()
	
	public Value get (final Collection<CharSequence> tokens);
	
	public void put (final Collection<CharSequence> tokens, final Value entry);
	
	public Set<TokenMatch> matches (final Collection<CharSequence> tokens);
	
	public Set<TokenMatch> matches (final Collection<CharSequence> tokens, final DynamicStringDist dist, final Double tolerance);
}
