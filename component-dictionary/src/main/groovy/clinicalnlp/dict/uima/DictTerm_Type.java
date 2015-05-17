
/* First created by JCasGen Sun May 17 16:01:27 CDT 2015 */
package clinicalnlp.dict.uima;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** Annotation for dictionary lookup matches
 * Updated by JCasGen Sun May 17 16:01:27 CDT 2015
 * @generated */
public class DictTerm_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DictTerm_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DictTerm_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DictTerm(addr, DictTerm_Type.this);
  			   DictTerm_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DictTerm(addr, DictTerm_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DictTerm.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("clinicalnlp.dict.uima.DictTerm");
 
  /** @generated */
  final Feature casFeat_canonicalTerm;
  /** @generated */
  final int     casFeatCode_canonicalTerm;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getCanonicalTerm(int addr) {
        if (featOkTst && casFeat_canonicalTerm == null)
      jcas.throwFeatMissing("canonicalTerm", "clinicalnlp.dict.uima.DictTerm");
    return ll_cas.ll_getStringValue(addr, casFeatCode_canonicalTerm);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setCanonicalTerm(int addr, String v) {
        if (featOkTst && casFeat_canonicalTerm == null)
      jcas.throwFeatMissing("canonicalTerm", "clinicalnlp.dict.uima.DictTerm");
    ll_cas.ll_setStringValue(addr, casFeatCode_canonicalTerm, v);}
    
  
 
  /** @generated */
  final Feature casFeat_enclosingSpan;
  /** @generated */
  final int     casFeatCode_enclosingSpan;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getEnclosingSpan(int addr) {
        if (featOkTst && casFeat_enclosingSpan == null)
      jcas.throwFeatMissing("enclosingSpan", "clinicalnlp.dict.uima.DictTerm");
    return ll_cas.ll_getRefValue(addr, casFeatCode_enclosingSpan);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEnclosingSpan(int addr, int v) {
        if (featOkTst && casFeat_enclosingSpan == null)
      jcas.throwFeatMissing("enclosingSpan", "clinicalnlp.dict.uima.DictTerm");
    ll_cas.ll_setRefValue(addr, casFeatCode_enclosingSpan, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public DictTerm_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_canonicalTerm = jcas.getRequiredFeatureDE(casType, "canonicalTerm", "uima.cas.String", featOkTst);
    casFeatCode_canonicalTerm  = (null == casFeat_canonicalTerm) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_canonicalTerm).getCode();

 
    casFeat_enclosingSpan = jcas.getRequiredFeatureDE(casType, "enclosingSpan", "uima.tcas.Annotation", featOkTst);
    casFeatCode_enclosingSpan  = (null == casFeat_enclosingSpan) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_enclosingSpan).getCode();

  }
}



    