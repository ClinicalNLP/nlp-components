

/* First created by JCasGen Fri Dec 12 23:00:14 CST 2014 */
package clinicalnlp.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.cleartk.ne.type.NamedEntityMention;


/** 
 * Updated by JCasGen Fri Dec 12 23:00:14 CST 2014
 * XML source: C:/WKT/git/BiomedicalNLP/nlp-components/component-negation/src/main/resources/descriptors/NegExTypeSystem.xml
 * @generated */
public class NegatableNamedEntityMention extends NamedEntityMention {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(NegatableNamedEntityMention.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected NegatableNamedEntityMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public NegatableNamedEntityMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public NegatableNamedEntityMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public NegatableNamedEntityMention(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: polarity

  /** getter for polarity - gets 
   * @generated
   * @return value of the feature 
   */
  public int getPolarity() {
    if (NegatableNamedEntityMention_Type.featOkTst && ((NegatableNamedEntityMention_Type)jcasType).casFeat_polarity == null)
      jcasType.jcas.throwFeatMissing("polarity", "clinicalnlp.type.NegatableNamedEntityMention");
    return jcasType.ll_cas.ll_getIntValue(addr, ((NegatableNamedEntityMention_Type)jcasType).casFeatCode_polarity);}
    
  /** setter for polarity - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPolarity(int v) {
    if (NegatableNamedEntityMention_Type.featOkTst && ((NegatableNamedEntityMention_Type)jcasType).casFeat_polarity == null)
      jcasType.jcas.throwFeatMissing("polarity", "clinicalnlp.type.NegatableNamedEntityMention");
    jcasType.ll_cas.ll_setIntValue(addr, ((NegatableNamedEntityMention_Type)jcasType).casFeatCode_polarity, v);}    
  }

    