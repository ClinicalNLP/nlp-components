import static clinicalnlp.dsl.UIMAUtil.*

import org.cleartk.ne.type.NamedEntityMention

import clinicalnlp.type.Relation

NamedEntityMention polypMention = (select(type:NamedEntityMention, filter:{it.coveredText == 'tubular adenoma'})).first()
NamedEntityMention siteMention = (select(type:NamedEntityMention, filter:{it.coveredText == 'sigmoid colon'})).first()

create(type:Relation, arg1:polypMention, arg2:siteMention)
