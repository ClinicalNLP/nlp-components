import clinicalnlp.dsl.UIMAUtil
import org.cleartk.ne.type.NamedEntityMention
import org.cleartk.token.type.Token

File output = new File('/Users/WKT/Code/Schorndorfer/nlp-components/reader-ncbi-disease-corpus/src/test/output/train.txt')

//Collection<NamedEntityMention> mentions = UIMAUtil.select(type:NamedEntityMention)
//mentions.each {
//    println it.coveredText
//}


Collection<Token> tokens = UIMAUtil.select(type:Token)
tokens.each { Token token ->
    output.append(token.coveredText)
    Collection<NamedEntityMention> b_mentions = UIMAUtil.select(type:NamedEntityMention,
            filter:{it.begin == token.begin})
    Collection<NamedEntityMention> i_mentions = UIMAUtil.select(type:NamedEntityMention,
            filter:and({it.begin < token.begin},
                       {it.end >= token.end}))
    if (b_mentions.size() == 1) {
        output.append(" B-${b_mentions[0].mentionType}")
    }
    else if (i_mentions.size() == 1) {
        output.append(" I-${i_mentions[0].mentionType}")
    }
    else {
        output.append(" O")
    }
    output.append('\n')
}

