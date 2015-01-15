package clinicalnlp.brat;

public abstract class BratAnnotation {

  private final String id;
  private final String type;
  
  protected BratAnnotation(String id, String type) {
    this.id = id;
    this.type =type;
  }
  
  public String getId() {
    return id;
  }
  
  public String getType() {
    return type;
  }
  
  @Override
  public String toString() {
    return id + " " + type;
  }
}
