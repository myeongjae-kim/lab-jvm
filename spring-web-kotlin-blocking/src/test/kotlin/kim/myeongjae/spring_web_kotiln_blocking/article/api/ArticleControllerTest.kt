package kim.myeongjae.spring_web_kotiln_blocking.article.api

import kim.myeongjae.common.Constants
import kim.myeongjae.spring_web_kotiln_blocking.article.api.common.CommonDescriptors
import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.ArticleFixture
import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.ArticleRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.headers.HeaderDocumentation
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
class ArticleControllerTest @Autowired constructor(
    private val mvc: MockMvc,
) {
    @MockBean lateinit var articleRepository: ArticleRepository

    @Nested
    inner class GetPublishedArticle {
        @Test
        fun `should respond ok`() {
            // given
            val slug = "slug"
            val article = ArticleFixture.create()

            BDDMockito.given(articleRepository.findBySlugAndPublishedTrue(ArgumentMatchers.matches(slug)))
                .willReturn(article)

            mvc.perform(RestDocumentationRequestBuilders.get("/articles/{slug}", slug))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/get-published",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("slug").description("슬러그")),
                        PayloadDocumentation.responseFields(ArticleControllerDescriptors.responseFieldsExternal),
                    )
                )
        }
    }

    @Nested
    inner class GetArticle {

        @Test
        fun `should respond ok`() {
            // given
            val slug = "slug"
            val article = ArticleFixture.create()

            BDDMockito.given(articleRepository.findBySlug(ArgumentMatchers.matches(slug)))
                .willReturn(article)

            mvc.perform(
                RestDocumentationRequestBuilders.get("/articles/{slug}", slug).header(Constants.HEADER_INTERNAL, "")
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/get",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        HeaderDocumentation.requestHeaders(CommonDescriptors.internalHeaderDescriptor),
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("slug").description("슬러그")
                        ),
                        PayloadDocumentation.responseFields(ArticleControllerDescriptors.responseFields),
                    )
                )
        }
    }
}
