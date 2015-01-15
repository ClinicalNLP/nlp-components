package clinicalnlp.brat;

public class RelationAnnotation extends BratAnnotation {

  private final String arg1;
  private final String arg2;
  
  protected RelationAnnotation(String id, String type, String arg1, String arg2) {
    super(id, type);
    this.arg1 = arg1;
    this.arg2 = arg2;
  }
  
  public String getArg1() {
    return arg1;
  }
  
  public String getArg2() {
    return arg2;
  }
  
  @Override
  public String toString() {
    return super.toString() + " arg1:" + getArg1() + " arg2:" + getArg2();
  }
}
