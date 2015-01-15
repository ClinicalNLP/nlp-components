package clinicalnlp.brat;

public class AttributeAnnotation extends BratAnnotation {

  private final String entityId;
  private final String value;
  
  protected AttributeAnnotation(String id, String type, String entityId, String value) {
    super(id, type);
    this.entityId = entityId;
    this.value = value;
  }
  
  public String getEntityId() {
    return this.entityId;
  }
  
  public String getValue() {
    return this.value;
  }
  
  @Override
  public String toString() {
    return super.toString() + " entityId:" + this.getEntityId() + " value:" + this.getValue();
  }
}
