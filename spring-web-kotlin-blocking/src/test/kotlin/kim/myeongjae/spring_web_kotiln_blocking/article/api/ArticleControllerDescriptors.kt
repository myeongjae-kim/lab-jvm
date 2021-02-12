package kim.myeongjae.spring_web_kotiln_blocking.article.api

import kim.myeongjae.spring_web_kotiln_blocking.article.api.common.CommonDescriptors
import kim.myeongjae.spring_web_kotiln_blocking.article.api.dto.ArticleRequestDto
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation

class ArticleControllerDescriptors {
    companion object {
        val requestFields: List<FieldDescriptor>
        val responseFieldsExternal: List<FieldDescriptor>
        val responseFields: List<FieldDescriptor>

        init {
            val requestConstraints = CommonDescriptors.ConstrainedFields(ArticleRequestDto::class.java)

            requestFields = listOf(
                requestConstraints.withPath("title").description("제목"),
                requestConstraints.withPath("content").description("내용"),
            )

            responseFieldsExternal = listOf(
                PayloadDocumentation.fieldWithPath("title").description("제목"),
                PayloadDocumentation.fieldWithPath("content").description("내용"),
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
