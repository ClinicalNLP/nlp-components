package clinicalnlp.dict.stringdist
import java.util.Collection;

public interface DynamicStringDist {
	public void init(final Collection<CharSequence> tokens);
	public Double push(final char c);
	public void pop();
	public Collection<Integer[]> matches(final Double score)
}
