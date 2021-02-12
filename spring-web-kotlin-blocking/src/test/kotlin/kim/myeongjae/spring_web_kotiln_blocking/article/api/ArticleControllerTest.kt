package kim.myeongjae.spring_web_kotiln_blocking.article.api

import com.fasterxml.jackson.databind.ObjectMapper
import kim.myeongjae.common.Constants
import kim.myeongjae.spring_web_kotiln_blocking.article.api.common.CommonDescriptors
import kim.myeongjae.spring_web_kotiln_blocking.article.api.dto.ArticleRequestDtoFixture
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
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
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
    private val objectMapper: ObjectMapper
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

        @Test
        fun `should respond not found when the requested article is not published`() {
            // given
            val slug = "slug"
            BDDMockito.given(articleRepository.findBySlugAndPublishedTrue(ArgumentMatchers.matches(slug)))
                .will { throw EmptyResultDataAccessException(0) }

            // then
            mvc.perform(RestDocumentationRequestBuilders.get("/articles/{slug}", slug))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
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


    @Nested
    inner class CreateArticle {
        @Test
        fun `should respond ok`() {
            // given
            val slug = "slug"
            val req = ArticleRequestDtoFixture.create()
            val body = objectMapper.writeValueAsString(req)

            // when
            mvc.perform(
                RestDocumentationRequestBuilders.post("/articles/{slug}", slug)
                    .header(Constants.HEADER_INTERNAL, "")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/create",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        HeaderDocumentation.requestHeaders(CommonDescriptors.internalHeaderDescriptor),
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("slug").description("슬러그")
                        ),
                        PayloadDocumentation.requestFields(ArticleControllerDescriptors.requestFields)
                    )
                )

            // then
            BDDMockito.verify(articleRepository).save(ArgumentMatchers.argThat {
                it.slug == slug && it.title == req.title && it.content == req.content
            })
        }
    }
}
