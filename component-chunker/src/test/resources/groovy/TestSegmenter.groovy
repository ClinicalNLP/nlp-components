import static clinicalnlp.dsl.UIMAUtil.*

import java.util.regex.Pattern

import clinicalnlp.type.Segment

// Match segment headings
matcher = jcas.documentText =~ ~/(?m)(?s)FINAL DIAGNOSIS:(.+)COMMENT:/
matcher.each {
    create(type:Segment, begin:matcher.start(1), end:matcher.end(1), code:'FINAL_DIAGNOSIS')
}
matcher = jcas.documentText =~ ~/(?m)(?s)COMMENT:(.+)\Z/
matcher.each {
    create(type:Segment, begin:matcher.start(1), end:matcher.end(1), code:'COMMENT')
}
