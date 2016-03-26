import clinicalnlp.dsl.UIMAUtil
import org.cleartk.token.type.Token


Collection<Token> tokens = UIMAUtil.select(type:Token)
tokens.each { println "Token: ${it.coveredText}" }