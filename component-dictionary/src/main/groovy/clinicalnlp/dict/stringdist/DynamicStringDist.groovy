package clinicalnlp.dict.stringdist
import java.util.Collection;

public interface DynamicStringDist {
	public void addTextToMatch(final Collection<CharSequence> tokens);
	public Double appendMatchChar(final char c);
	public void removeMatchChar();
	public Collection<Integer[]> getMatches(final Double score)
	public Double getMinScore()
}
