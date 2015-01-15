package clinicalnlp.brat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.util.ObjectStream;

public class BratDocument {
	private final Map<String, BratAnnotation> annotationMap;
	
	private Map<String, SpanAnnotation> spanAnnotations;
	private Map<String, AttributeAnnotation> attrAnnotations;
	private Map<String, RelationAnnotation> relAnnotations;

	public BratDocument(Collection<BratAnnotation> annotations) {
		
		this.spanAnnotations = null;
		this.attrAnnotations = null;
		this.relAnnotations = null;

		Map<String, BratAnnotation> annMap = new HashMap<String, BratAnnotation>();
		for (BratAnnotation annotation : annotations) {
			annMap.put(annotation.getId(), annotation);
		}

		this.annotationMap = Collections.unmodifiableMap(annMap);
	}

	public BratAnnotation getAnnotation(String id) {
		return annotationMap.get(id);
	}

	public Collection<BratAnnotation> getAnnotations() {
		return annotationMap.values();
	}
	
	public final Map<String, SpanAnnotation> getSpanAnnotations() {
		if (this.spanAnnotations == null) {
			this.spanAnnotations = new HashMap<>();
			for (String key : annotationMap.keySet()) {
				BratAnnotation ann = annotationMap.get(key);
				if (ann instanceof SpanAnnotation) {
					this.spanAnnotations.put(key, (SpanAnnotation) ann);
				}
			}
		}
		return  Collections.unmodifiableMap(this.spanAnnotations);
	}
	
	public final Map<String, AttributeAnnotation> getAttrAnnotations() {
		if (this.attrAnnotations == null) {
			this.attrAnnotations = new HashMap<>();
			for (String key : annotationMap.keySet()) {
				BratAnnotation ann = annotationMap.get(key);
				if (ann instanceof AttributeAnnotation) {
					this.attrAnnotations.put(key, (AttributeAnnotation) ann);
				}
			}
		}
		return  Collections.unmodifiableMap(this.attrAnnotations);
	}

	public final Map<String, RelationAnnotation> getRelAnnotations() {
		if (this.relAnnotations == null) {
			this.relAnnotations = new HashMap<>();
			for (String key : annotationMap.keySet()) {
				BratAnnotation ann = annotationMap.get(key);
				if (ann instanceof RelationAnnotation) {
					this.relAnnotations.put(key, (RelationAnnotation) ann);
				}
			}
		}
		return  Collections.unmodifiableMap(this.relAnnotations);
	}


	public static BratDocument parseDocument(InputStream annIn) throws IOException {

		Collection<BratAnnotation> annotations = new ArrayList<BratAnnotation>();

		ObjectStream<BratAnnotation> annStream = new BratAnnotationStream(annIn);

		BratAnnotation ann;
		while ((ann = annStream.read()) != null) {
			annotations.add(ann);
		}

		return new BratDocument(annotations);
	}
}
