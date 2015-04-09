

/* First created by JCasGen Wed Jan 14 16:51:33 CST 2015 */
package clinicalnlp.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Apr 09 14:51:51 CDT 2015
 * XML source: C:/WKT/git/ClinicalNLP/nlp-components/component-relation-extractor/src/main/resources/descriptors/RelationTypeSystem.xml
 * @generated */
public class Relation extends TOP {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Relation.class);
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
  protected Relation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Relation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Relation(JCas jcas) {
    super(jcas);
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
  //* Feature: code

  /** getter for code - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCode() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_code == null)
      jcasType.jcas.throwFeatMissing("code", "clinicalnlp.type.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_code);}
    
  /** setter for code - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCode(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_code == null)
      jcasType.jcas.throwFeatMissing("code", "clinicalnlp.type.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_code, v);}    
   
    
  //*--------------*
  //* Feature: system

  /** getter for system - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSystem() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_system == null)
      jcasType.jcas.throwFeatMissing("system", "clinicalnlp.type.Relation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Relation_Type)jcasType).casFeatCode_system);}
    
  /** setter for system - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSystem(String v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_system == null)
      jcasType.jcas.throwFeatMissing("system", "clinicalnlp.type.Relation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Relation_Type)jcasType).casFeatCode_system, v);}    
   
    
  //*--------------*
  //* Feature: arg1

  /** getter for arg1 - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getArg1() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_arg1 == null)
      jcasType.jcas.throwFeatMissing("arg1", "clinicalnlp.type.Relation");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Relation_Type)jcasType).casFeatCode_arg1)));}
    
  /** setter for arg1 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setArg1(Annotation v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_arg1 == null)
      jcasType.jcas.throwFeatMissing("arg1", "clinicalnlp.type.Relation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Relation_Type)jcasType).casFeatCode_arg1, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: arg2

  /** getter for arg2 - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getArg2() {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_arg2 == null)
      jcasType.jcas.throwFeatMissing("arg2", "clinicalnlp.type.Relation");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Relation_Type)jcasType).casFeatCode_arg2)));}
    
  /** setter for arg2 - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setArg2(Annotation v) {
    if (Relation_Type.featOkTst && ((Relation_Type)jcasType).casFeat_arg2 == null)
      jcasType.jcas.throwFeatMissing("arg2", "clinicalnlp.type.Relation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Relation_Type)jcasType).casFeatCode_arg2, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    