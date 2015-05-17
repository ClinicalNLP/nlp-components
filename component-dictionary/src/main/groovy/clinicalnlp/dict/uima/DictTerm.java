

/* First created by JCasGen Sun May 17 16:01:27 CDT 2015 */
package clinicalnlp.dict.uima;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Annotation for dictionary lookup matches
 * Updated by JCasGen Sun May 17 16:01:27 CDT 2015
 * XML source: C:/WKT/git/ClinicalNLP/nlp-components/component-dictionary/src/main/resources/descriptors/DictTypes.xml
 * @generated */
public class DictTerm extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DictTerm.class);
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
  protected DictTerm() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public DictTerm(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public DictTerm(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public DictTerm(JCas jcas, int begin, int end) {
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
  //* Feature: canonicalTerm

  /** getter for canonicalTerm - gets canonical form
   * @generated
   * @return value of the feature 
   */
  public String getCanonicalTerm() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_canonicalTerm == null)
      jcasType.jcas.throwFeatMissing("canonicalTerm", "clinicalnlp.dict.uima.DictTerm");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_canonicalTerm);}
    
  /** setter for canonicalTerm - sets canonical form 
   * @generated
   * @param v value to set into the feature 
   */
  public void setCanonicalTerm(String v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_canonicalTerm == null)
      jcasType.jcas.throwFeatMissing("canonicalTerm", "clinicalnlp.dict.uima.DictTerm");
    jcasType.ll_cas.ll_setStringValue(addr, ((DictTerm_Type)jcasType).casFeatCode_canonicalTerm, v);}    
   
    
  //*--------------*
  //* Feature: enclosingSpan

  /** getter for enclosingSpan - gets span that this NoTerm is contained within (i.e. its
						sentence)
   * @generated
   * @return value of the feature 
   */
  public Annotation getEnclosingSpan() {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_enclosingSpan == null)
      jcasType.jcas.throwFeatMissing("enclosingSpan", "clinicalnlp.dict.uima.DictTerm");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_enclosingSpan)));}
    
  /** setter for enclosingSpan - sets span that this NoTerm is contained within (i.e. its
						sentence) 
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnclosingSpan(Annotation v) {
    if (DictTerm_Type.featOkTst && ((DictTerm_Type)jcasType).casFeat_enclosingSpan == null)
      jcasType.jcas.throwFeatMissing("enclosingSpan", "clinicalnlp.dict.uima.DictTerm");
    jcasType.ll_cas.ll_setRefValue(addr, ((DictTerm_Type)jcasType).casFeatCode_enclosingSpan, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    