package clinicalnlp.brat;

import opennlp.tools.util.Span;

public class SpanAnnotation extends BratAnnotation {

  private final Span span;
  private final String coveredText;
  
  SpanAnnotation(String id, String type, Span span, String coveredText) {
    super(id, type);
    this.span = span;
    this.coveredText = coveredText;
  }
  
  public Span getSpan() {
    return span;
  }
  
  public String getCoveredText() {
    return coveredText;
  }
  
  @Override
  public String toString() {
    return super.toString() + " " + span.getStart() + " " + span.getEnd() + " " + coveredText;
  }
}
