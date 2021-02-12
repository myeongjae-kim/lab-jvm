package kim.myeongjae.spring_web_kotiln_blocking.article.api

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

class ArticleControllerDescriptors {
    companion object {
        val requestFields: List<FieldDescriptor>
        val responseFieldsExternal: List<FieldDescriptor>
        val responseFields: List<FieldDescriptor>

        init {
            requestFields = listOf(
                PayloadDocumentation.fieldWithPath("title").description("제목"),
                PayloadDocumentation.fieldWithPath("content").description("내용"),
            )

            responseFieldsExternal = requestFields + listOf(
                PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
                PayloadDocumentation.fieldWithPath("updatedAt").description("갱신일시")
            )

            responseFields = responseFieldsExternal + listOf(
                PayloadDocumentation.fieldWithPath("id").description("Id"),
                PayloadDocumentation.fieldWithPath("slug").description("슬러그"),
                PayloadDocumentation.fieldWithPath("published").description("발행 여부"),
            )
        }
    }
}
