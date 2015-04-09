
/* First created by JCasGen Wed Jan 14 16:51:33 CST 2015 */
package clinicalnlp.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Apr 09 14:51:51 CDT 2015
 * @generated */
public class Relation_Type extends TOP_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Relation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Relation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Relation(addr, Relation_Type.this);
  			   Relation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Relation(addr, Relation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Relation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("clinicalnlp.type.Relation");
 
  /** @generated */
  final Feature casFeat_code;
  /** @generated */
  final int     casFeatCode_code;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getCode(int addr) {
        if (featOkTst && casFeat_code == null)
      jcas.throwFeatMissing("code", "clinicalnlp.type.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_code);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCode(int addr, String v) {
        if (featOkTst && casFeat_code == null)
      jcas.throwFeatMissing("code", "clinicalnlp.type.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_code, v);}
    
  
 
  /** @generated */
  final Feature casFeat_system;
  /** @generated */
  final int     casFeatCode_system;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSystem(int addr) {
        if (featOkTst && casFeat_system == null)
      jcas.throwFeatMissing("system", "clinicalnlp.type.Relation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_system);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSystem(int addr, String v) {
        if (featOkTst && casFeat_system == null)
      jcas.throwFeatMissing("system", "clinicalnlp.type.Relation");
    ll_cas.ll_setStringValue(addr, casFeatCode_system, v);}
    
  
 
  /** @generated */
  final Feature casFeat_arg1;
  /** @generated */
  final int     casFeatCode_arg1;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getArg1(int addr) {
        if (featOkTst && casFeat_arg1 == null)
      jcas.throwFeatMissing("arg1", "clinicalnlp.type.Relation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_arg1);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setArg1(int addr, int v) {
        if (featOkTst && casFeat_arg1 == null)
      jcas.throwFeatMissing("arg1", "clinicalnlp.type.Relation");
    ll_cas.ll_setRefValue(addr, casFeatCode_arg1, v);}
    
  
 
  /** @generated */
  final Feature casFeat_arg2;
  /** @generated */
  final int     casFeatCode_arg2;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getArg2(int addr) {
        if (featOkTst && casFeat_arg2 == null)
      jcas.throwFeatMissing("arg2", "clinicalnlp.type.Relation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_arg2);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setArg2(int addr, int v) {
        if (featOkTst && casFeat_arg2 == null)
      jcas.throwFeatMissing("arg2", "clinicalnlp.type.Relation");
    ll_cas.ll_setRefValue(addr, casFeatCode_arg2, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Relation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_code = jcas.getRequiredFeatureDE(casType, "code", "uima.cas.String", featOkTst);
    casFeatCode_code  = (null == casFeat_code) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_code).getCode();

 
    casFeat_system = jcas.getRequiredFeatureDE(casType, "system", "uima.cas.String", featOkTst);
    casFeatCode_system  = (null == casFeat_system) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_system).getCode();

 
    casFeat_arg1 = jcas.getRequiredFeatureDE(casType, "arg1", "uima.tcas.Annotation", featOkTst);
    casFeatCode_arg1  = (null == casFeat_arg1) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_arg1).getCode();

 
    casFeat_arg2 = jcas.getRequiredFeatureDE(casType, "arg2", "uima.tcas.Annotation", featOkTst);
    casFeatCode_arg2  = (null == casFeat_arg2) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_arg2).getCode();

  }
}



    