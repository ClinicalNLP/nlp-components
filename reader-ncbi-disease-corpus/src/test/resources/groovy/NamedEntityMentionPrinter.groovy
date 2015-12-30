import clinicalnlp.dsl.UIMAUtil
import org.cleartk.ne.type.NamedEntityMention

Collection<NamedEntityMention> namedEntityMentions = UIMAUtil.select(type:NamedEntityMention)
namedEntityMentions.each { println "NamedEntityMention: ${it}" }