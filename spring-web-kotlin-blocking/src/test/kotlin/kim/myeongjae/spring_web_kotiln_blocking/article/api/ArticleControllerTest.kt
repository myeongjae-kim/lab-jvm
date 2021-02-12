package kim.myeongjae.spring_web_kotiln_blocking.article.api

import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.ArticleFixture
import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.ArticleRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ArticleController::class)
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension::class)
class ArticleControllerTest @Autowired constructor(
    private val mvc: MockMvc,
) {
    @MockBean lateinit var articleRepository: ArticleRepository

    @Test
    fun getExternalArticle() {
        // given
        val slug = "slug"
        val article = ArticleFixture.create()

        BDDMockito.given(articleRepository.findBySlug(ArgumentMatchers.matches(slug)))
            .willReturn(article)

        mvc.perform(RestDocumentationRequestBuilders.get("/articles/{slug}", slug))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document(
                "articles/get-external",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("slug").description("슬러그")),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("title").description("제목"),
                    PayloadDocumentation.fieldWithPath("content").description("내용"),
                    PayloadDocumentation.fieldWithPath("createdAt").description("생성일시"),
                    PayloadDocumentation.fieldWithPath("updatedAt").description("갱신일시"),
                ),
            ))

    }
}