import static clinicalnlp.dsl.UIMAUtil.*

import java.util.regex.Matcher

import org.cleartk.token.type.Sentence

Matcher m = (jcas.documentText =~ /([A-Z].+\.)/)
m.each {
	println m.group(1)
	create(type:Sentence, begin:m.start(1), end:m.end(1))
}
