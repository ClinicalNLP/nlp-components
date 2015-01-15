package clinicalnlp.brat

import static clinicalnlp.dsl.UIMAUtil.*

import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.cleartk.ne.type.NamedEntityMention

import clinicalnlp.dsl.UIMAUtil
import clinicalnlp.type.Relation

class BratAnnotatorImpl extends BratAnnotator {

    static final String SNOMED = 'SNOMED'
    static final String BODY_STRUCTURE = 'T023'
    static final String NEOPLASTIC_PROCESS = 'T191'
    static final String LEFT_COLON = 'C0227388'
    static final String RIGHT_COLON = 'C1305188'
    static final String COLON = 'C0009368'
    static final String ADENOMA = 'C0850572'
    static final String TUBULAR_ADENOMA = 'C0334292'
    static final String VILLOUS_ADENOMA = 'C0149862'
    static final String SERRATED_ADENOMA = 'C3266124'
    static final String HYPERPLASTIC_POLYP = 'C0267364'
    static final String ADENOCARCINOMA = 'C0001418'

    @Override
    protected List<Annotation> mapSpanAnnotation(JCas jcas, SpanAnnotation span) {
        List<Annotation> anns = []

        switch (span.type) {
            case 'Left':
            case 'Rectum':
            case 'Sigmoid':
            case 'Descending':
            case 'Splenic_Flexure':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
            case 'Right':
            case 'Hepatic_Flexure':
            case 'Transverse':
            case 'Ascending':
            case 'Ileum':
            case 'Cecum':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
            case 'Colon':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break;
            case 'Adenoma':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
            case 'Tubular':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
            case 'Tubulovillous':
            case 'Villous':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
            case 'Serrated':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
            case 'Hyperplastic':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
            case 'Adenocarcinoma':
                anns.add(create(type:NamedEntityMention, begin:span.span.start, end:span.span.end))
                break
        }

        return anns
    }

    @Override
    protected Relation createRelation(JCas jcas, Annotation arg1, Annotation arg2, RelationAnnotation rel) {
        UIMAUtil.jcas = jcas
        Relation relation = null;
        switch (rel.type) {
            case 'Finding':
                create(type:Relation, arg1:arg1, arg2:arg2)
                break
        }
        return relation
    }
}
