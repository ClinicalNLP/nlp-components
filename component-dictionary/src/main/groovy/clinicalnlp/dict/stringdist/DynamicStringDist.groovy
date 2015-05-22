package clinicalnlp.dict.stringdist
import java.util.Collection;

public interface DynamicStringDist extends StringDist {
	public Double push(final char c);
	public void pop();
}
