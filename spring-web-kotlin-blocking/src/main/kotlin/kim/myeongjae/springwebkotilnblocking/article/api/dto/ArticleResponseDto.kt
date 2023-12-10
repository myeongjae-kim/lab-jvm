package kim.myeongjae.springwebkotilnblocking.article.api.dto

import com.fasterxml.jackson.annotation.JsonView
import kim.myeongjae.common.jsonview.Views
import kim.myeongjae.springwebkotilnblocking.article.domain.model.Article
import kim.myeongjae.springwebkotilnblocking.article.domain.model.ArticleId
import java.time.ZonedDateTime

@JsonView(Views.All::class)
data class ArticleResponseDto(
    val id: ArticleId,
    @field:JsonView(Views.Public::class)
    val createdAt: ZonedDateTime,
    @field:JsonView(Views.Public::class)
    val updatedAt: ZonedDateTime,
    @field:JsonView(Views.Public::class)
    val title: String,
    @field:JsonView(Views.Public::class)
    val content: String,
    val slug: String,
    val published: Boolean,
) {
    companion object {
        fun from(article: Article): ArticleResponseDto = ArticleResponseDto(
            id = article.id,
            createdAt = article.createdAt!!,
            updatedAt = article.updatedAt!!,
            title = article.title,
            content = article.content,
            slug = article.slug,
            published = article.published,
        )
    }
}
