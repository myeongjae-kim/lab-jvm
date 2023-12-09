package kim.myeongjae.springwebkotilnblocking.article.api

import com.fasterxml.jackson.databind.ObjectMapper
import kim.myeongjae.common.Constants
import kim.myeongjae.common.api.RequestHeaderFixture
import kim.myeongjae.springwebkotilnblocking.article.api.common.CommonDescriptors
import kim.myeongjae.springwebkotilnblocking.article.api.dto.ArticleRequestDtoFixture
import kim.myeongjae.springwebkotilnblocking.article.domain.model.ArticleFixture
import kim.myeongjae.springwebkotilnblocking.article.domain.model.ArticleRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(ArticleController::class)
@AutoConfigureRestDocs
class ArticleControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val objectMapper: ObjectMapper,
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
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("slug").description("슬러그"),
                        ),
                        PayloadDocumentation.responseFields(ArticleControllerDescriptors.responseFieldsExternal),
                    ),
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
                RestDocumentationRequestBuilders.get("/articles/{slug}", slug).headers(RequestHeaderFixture.create()),
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
                            RequestDocumentation.parameterWithName("slug").description("슬러그"),
                        ),
                        PayloadDocumentation.responseFields(ArticleControllerDescriptors.responseFields),
                    ),
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
                    .headers(RequestHeaderFixture.create())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body),
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
                            RequestDocumentation.parameterWithName("slug").description("슬러그"),
                        ),
                        PayloadDocumentation.requestFields(ArticleControllerDescriptors.requestFields),
                    ),
                )

            // then
            BDDMockito.verify(articleRepository).save(
                ArgumentMatchers.argThat {
                    it.slug == slug && it.title == req.title && it.content == req.content
                },
            )
        }

        @Test
        fun `should respond bad request when title is empty`() {
            // given
            val slug = "slug"
            val req = ArticleRequestDtoFixture.create()
            ReflectionTestUtils.setField(req, "title", "")
            val body = objectMapper.writeValueAsString(req)

            // when
            mvc.perform(
                RestDocumentationRequestBuilders.post("/articles/{slug}", slug)
                    .headers(RequestHeaderFixture.create())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body),
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("title must not be blank"))
        }
    }

    @Nested
    inner class UpdateArticle {
        @Test
        fun `should respond ok`() {
            // given
            val slug = "slug"
            val req = ArticleRequestDtoFixture.create()
            ReflectionTestUtils.setField(req, "title", "newTitle")
            ReflectionTestUtils.setField(req, "content", "newContent")

            val body = objectMapper.writeValueAsString(req)

            val article = ArticleFixture.create()
            BDDMockito.given(articleRepository.findBySlug(ArgumentMatchers.matches(slug)))
                .willReturn(article)

            // when
            mvc.perform(
                RestDocumentationRequestBuilders.put("/articles/{slug}", slug)
                    .headers(RequestHeaderFixture.create())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/update",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        HeaderDocumentation.requestHeaders(CommonDescriptors.internalHeaderDescriptor),
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("slug").description("슬러그"),
                        ),
                        PayloadDocumentation.requestFields(ArticleControllerDescriptors.requestFields),
                    ),
                )

            // then
            BDDMockito.verify(articleRepository).save(
                ArgumentMatchers.argThat {
                    it.slug == slug && it.title == req.title && it.content == req.content
                },
            )
        }
    }

    @Nested
    inner class GetPublishedArticles {
        @Test
        fun `should pass`() {
            val pageNo = 0
            val page = PageImpl((1..3).map { ArticleFixture.create() })

            BDDMockito.given(
                articleRepository.findAllByPublishedTrue(
                    ArgumentMatchers.argThat<Pageable> {
                        it.pageNumber == pageNo && it.pageSize == Constants.PAGE_SIZE
                    },
                ),
            ).willReturn(page)

            mvc.perform(RestDocumentationRequestBuilders.get("/articles?page={page}", pageNo))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/get-published-articles",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.requestParameters(
                            RequestDocumentation.parameterWithName("page").description("페이지 번호. Zero based"),
                        ),
                        PayloadDocumentation.responseFields(
                            CommonDescriptors.pageOf(ArticleControllerDescriptors.listResponseFields),
                        ),
                    ),
                )
        }
    }

    @Nested
    inner class GetArticles {
        @Test
        fun `should respond ok`() {
            val pageNo = 0
            val page = PageImpl(
                (1..3).map {
                    val article = ArticleFixture.create()
                    article.unpublish()
                    article
                },
            )

            BDDMockito.given(
                articleRepository.findAll(
                    ArgumentMatchers.argThat<Pageable> {
                        it.pageNumber == pageNo && it.pageSize == Constants.PAGE_SIZE
                    },
                ),
            )
                .willReturn(page)

            mvc.perform(
                RestDocumentationRequestBuilders.get("/articles?page={page}", 0).headers(RequestHeaderFixture.create()),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/get-articles",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        HeaderDocumentation.requestHeaders(CommonDescriptors.internalHeaderDescriptor),
                        RequestDocumentation.requestParameters(
                            RequestDocumentation.parameterWithName("page").description("페이지 번호. Zero based"),
                        ),
                        PayloadDocumentation.responseFields(
                            CommonDescriptors.pageOf(ArticleControllerDescriptors.listResponseFields),
                        ),
                    ),
                )
        }
    }

    @Nested
    inner class PublishArticle {
        @Test
        fun `should respond ok`() {
            // given
            val slug = "slug"
            val article = ArticleFixture.create()

            BDDMockito.given(articleRepository.findBySlug(ArgumentMatchers.matches(slug))).willReturn(article)

            // when
            mvc.perform(
                RestDocumentationRequestBuilders.put("/articles/{slug}/publish", slug)
                    .headers(RequestHeaderFixture.create()),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/publish",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        HeaderDocumentation.requestHeaders(CommonDescriptors.internalHeaderDescriptor),
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("slug").description("슬러그"),
                        ),
                    ),
                )

            // then
            BDDMockito.verify(articleRepository).save(
                ArgumentMatchers.argThat {
                    it.slug == slug && it.published
                },
            )
        }
    }

    @Nested
    inner class UnpublishArticle {
        @Test
        fun `should respond ok`() {
            // given
            val slug = "slug"
            val article = ArticleFixture.create()

            BDDMockito.given(articleRepository.findBySlug(ArgumentMatchers.matches(slug))).willReturn(article)

            // when
            mvc.perform(
                RestDocumentationRequestBuilders.put("/articles/{slug}/unpublish", slug)
                    .headers(RequestHeaderFixture.create()),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andDo(
                    MockMvcRestDocumentation.document(
                        "articles/unpublish",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        HeaderDocumentation.requestHeaders(CommonDescriptors.internalHeaderDescriptor),
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("slug").description("슬러그"),
                        ),
                    ),
                )

            // then
            BDDMockito.verify(articleRepository).save(
                ArgumentMatchers.argThat {
                    it.slug == slug && !it.published
                },
            )
        }
    }
}
