import static clinicalnlp.dsl.UIMAUtil.*

import java.util.regex.Matcher

import org.cleartk.ne.type.NamedEntityMention


Matcher matcher = jcas.documentText =~ /(?i)(pneumonia|fever|cough|sepsis|weakness)/
matcher.each {
    NamedEntityMention nem = new NamedEntityMention(jcas)
    nem.begin = matcher.start(1)
    nem.end = matcher.end(1)
    nem.addToIndexes()
} 

