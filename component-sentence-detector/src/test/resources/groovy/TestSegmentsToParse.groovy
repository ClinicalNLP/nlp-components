import static clinicalnlp.dsl.UIMAUtil.*
import clinicalnlp.type.Segment

segs = select(type:Segment, filter:{ it.code in ['FINAL_DIAGNOSIS'] })

