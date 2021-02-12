package kim.myeongjae.spring_web_kotiln_blocking.article.api.common

import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes

class CommonDescriptors {
    companion object {
        val internalHeaderDescriptor = HeaderDocumentation.headerWithName("Mj-Internal").description("관리자용 header")
    }

    class ConstrainedFields constructor(input: Class<*>?) {
        private val constraintDescriptions: ConstraintDescriptions = ConstraintDescriptions(input)

        fun withPath(path: String): FieldDescriptor = fieldWithPath(path).attributes(
            Attributes.key("constraints").value(constraintDescriptions.descriptionsForProperty(path).joinToString(". "))
        )
    }
}
