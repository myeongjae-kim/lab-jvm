package kim.myeongjae.spring_web_kotiln_blocking.article.api.common

import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes

class CommonDescriptors {
    companion object {
        val internalHeaderDescriptor = HeaderDocumentation.headerWithName("Mj-Internal").description("관리자용 header")

        fun pageOf(descriptors: List<FieldDescriptor>): List<FieldDescriptor> {
            val content = PayloadDocumentation.applyPathPrefix("content[].", descriptors)
            val page = listOf(
                fieldWithPath("content[]").description("An array of content"),
                fieldWithPath("totalElements").description("전체 element 갯수"),
                fieldWithPath("totalPages").description("전체 페이지 수, 곧 마지막 페이지 번호"),
                fieldWithPath("size").description("페이지당 최대 element 갯수"),
                fieldWithPath("number").description("현재 페이지 번호"),
                fieldWithPath("numberOfElements")
                    .description("현재 페이지의 element 수. 마지막 페이지가 아닌 이상 size와 같음"),
                fieldWithPath("first").description("현재 페이지가 첫 번째 페이지인지"),
                fieldWithPath("last").description("현재 페이지가 마지막 페이지인지"),
                fieldWithPath("empty").description("numberOfElements == 0"),
                fieldWithPath("sort").description("The sorting parameters for the page."),
                fieldWithPath("sort.sorted").description("Whether the current page is sorted"),
                fieldWithPath("sort.unsorted").description("Whether the current page is unsorted"),
                fieldWithPath("sort.empty").description("Whether sort order is empty"),
                fieldWithPath("pageable").ignored().optional(),
                fieldWithPath("pageable.sort").ignored().optional(),
                fieldWithPath("pageable.sort.unsorted").ignored().optional(),
                fieldWithPath("pageable.sort.sorted").ignored().optional(),
                fieldWithPath("pageable.sort.empty").ignored().optional(),
                fieldWithPath("pageable.offset").ignored().optional(),
                fieldWithPath("pageable.pageNumber").ignored().optional(),
                fieldWithPath("pageable.pageSize").ignored().optional(),
                fieldWithPath("pageable.paged").ignored().optional(),
                fieldWithPath("pageable.unpaged").ignored().optional(),
            )

            return listOf(content, page).flatten()
        }
    }

    class ConstrainedFields constructor(input: Class<*>?) {
        private val constraintDescriptions: ConstraintDescriptions = ConstraintDescriptions(input)

        fun withPath(path: String): FieldDescriptor = fieldWithPath(path).attributes(
            Attributes.key("constraints").value(constraintDescriptions.descriptionsForProperty(path).joinToString(". "))
        )
    }
}
