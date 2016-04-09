package clinicalnlp.dict.stringdist;

import java.util.Collection;

public interface StringDist {
	public void set(final Collection<CharSequence> tokens);
	public Double add(final Collection<CharSequence> tokens);
	public Collection<Integer[]> matches(final Double score);
}
