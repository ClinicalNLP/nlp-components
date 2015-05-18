package clinicalnlp.dict;

import java.util.Set;

public interface DictModel {
	
	public DictEntry get (String[] tokens);
	
	public void add (final DictEntry entry);
	
	public Set<LookupMatch> findMatches (final String[] tokens);
	
	public Set<LookupMatch> findMatches (final String[] tokens, Double tolerance);
}
