package clinicalnlp.dsl

import org.apache.uima.cas.text.AnnotationFS
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.TOP
import org.apache.uima.jcas.tcas.Annotation
import org.cleartk.ne.type.NamedEntityMention

import java.util.regex.Matcher
import java.util.regex.Pattern

import static org.apache.uima.fit.util.JCasUtil.select
import static org.apache.uima.fit.util.JCasUtil.selectAll
import static org.apache.uima.fit.util.JCasUtil.contains

/**
 * UIMAUtil implements the UIMA DSL
 * @author Will Thompson
 * TODO: add these methods to JCas interface
 */
class UIMAUtil extends Script {
    @Override
    Object run() {
        super.run()
    }

    // ------------------------------------------------------------------------
    // Setup
    // ------------------------------------------------------------------------

    // the JCas instance to be used for annotation creation and selection
    static JCas jcas

    // static initialization
    static {
        // add a named group index to the Matcher class
        Pattern.metaClass.matcher = { CharSequence input ->
            Matcher retval = input =~ delegate

            Pattern metpat = ~/\((\?(<(\w+?)>))/
            Matcher m = delegate.toString() =~ metpat
            Matcher.metaClass.namedGroupIndex = [:]
            int groupCnt = 0
            m.each {
                groupCnt += 1
                if (m.group(3) != null) {
                    retval.namedGroupIndex[m.group(3)] = groupCnt
                }
            }
            return retval
        }

        // add a start(groupName) method to the Matcher class
        Matcher.metaClass.start = { String groupName ->
            Integer groupNum = delegate.namedGroupIndex[groupName]
            return (groupNum ? delegate.start(groupNum): null)
        }

        // add an end(groupName) method to the Matcher class
        Matcher.metaClass.end = { String groupName ->
            Integer groupNum = delegate.namedGroupIndex[groupName]
            return (groupNum ? delegate.end(groupNum): null)
        }
        
        // return an AnnotationMatcher when passed a Map argument
        Pattern.metaClass.matcher = { Map args ->
            Annotation coveringAnn = (args.coveringAnn ?: jcas.documentAnnotationFs)
            Boolean includeText = (args.includeText == false ? false : true)
            java.util.List<Class<Annotation>> types = (args.types ?: [])
            return new AnnotationMatcher(jcas, coveringAnn, types, delegate, includeText)
        }
    }

    /**
     * Set the JCas to use
     * @param jcas
     */
    static void setJCas(JCas jcas) {
        UIMAUtil.jcas = jcas
    }

    /**
     * 
     * @param typeName
     * @return
     */
    static Class getAnnotationClass(String typeName) {
        Class typeClass = null
        switch (typeName) {
            case 'NamedEntityMention':
                typeClass = NamedEntityMention
                break
            default:
                throw new IllegalArgumentException("Invalid type in dictionary entry: $typeName")
        }
        return typeClass
    }

    // ------------------------------------------------------------------------
    // Annotation creation functions
    // ------------------------------------------------------------------------

    /**
     * Create UIMA annotation from set of attributes
     * @param attrs
     * @return
     */
    static TOP create(Map attrs) {
        TOP a = attrs.type.newInstance(jcas)
        attrs.each { k, v ->
            if (a.metaClass.hasProperty(a, k)) {
                if (k != 'type') {
                    a."${k}" = v
                }
            }
        }
        a.addToIndexes()
        return a
    }

    /**
     * Apply a set of regex patterns to a collection of annotations. For each match, apply
     * the specified closure
     */
    static applyPatterns = { Collection<Annotation> anns, java.util.List<Pattern> patterns, Closure action ->
        anns.each { ann ->
            patterns.each { p ->
                AnnotationMatcher m = p.matcher(coveringAnn:ann); m.each { action.call(m) }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Annotation selection functions
    // ------------------------------------------------------------------------

    static select(Map args) {
        Class type = args.type
        Closure filter = args.filter

        Collection<AnnotationFS> annotations = (type != null ? select(jcas, type) : selectAll(jcas))

        if (filter) {
            Collection<AnnotationFS> filtered = []
            annotations.each {
                if (filter.call(it) == true) { filtered << it }
            }
            annotations = filtered
        }

        annotations
    }


    // ------------------------------------------------------------------------
    // Selection predicates
    // TODO: check UIMAFit and RUTA for more complete set
    // ------------------------------------------------------------------------

    static not = { Closure pred ->
        { TOP ann ->
            !pred.call(ann)
        }
    }

    static and = { Closure... preds ->
        { TOP ann ->
            for (Closure pred : preds) {
                if (pred.call(ann) == false) { return false }
            }
            true
        }
    }

    static or = { Closure... preds ->
        { TOP ann ->
            for (Closure pred : preds) {
                if (pred.call(ann) == true) { return true }
            }
            false
        }
    }

    static contains = { Class<? extends Annotation> type ->
        { TOP ann ->
            contains(jcas, ann, type)
        }
    }

    static coveredBy = { TOP coveringAnn ->
        { TOP ann ->
            (ann != coveringAnn &&
                    coveringAnn.begin <= ann.begin &&
                    coveringAnn.end >= ann.end)
        }
    }

    static between = { Integer begin, Integer end ->
        { TOP ann ->
            (begin <= end && begin <= ann.begin && end >= ann.end)
        }
    }

    static before = { Integer index ->
        { TOP ann ->
            ann.end < index
        }
    }

    static after = { Integer index ->
        { TOP ann ->
            ann.begin > index
        }
    }
}