import static clinicalnlp.dsl.UIMAUtil.*

import clinicalnlp.type.Segment

create(type:Segment, begin:0, end:jcas.documentText.length())