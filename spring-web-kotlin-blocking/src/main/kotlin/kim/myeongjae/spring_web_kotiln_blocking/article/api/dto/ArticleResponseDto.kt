package kim.myeongjae.spring_web_kotiln_blocking.article.api.dto

import com.fasterxml.jackson.annotation.JsonView
import kim.myeongjae.common.jsonview.Views
import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.Article
import java.time.ZonedDateTime

@JsonView(Views.All::class)
data class ArticleResponseDto(
    val id: Long,
    @JsonView(Views.Public::class)
    val createdAt: ZonedDateTime,
    @JsonView(Views.Public::class)
    val updatedAt: ZonedDateTime,
    @JsonView(Views.Public::class)
    val title: String,
    @JsonView(Views.Public::class)
    val content: String,
    val slug: String,
    val published: Boolean,
) {
    companion object {
        fun from(article: Article): ArticleResponseDto = ArticleResponseDto(
            id = article.id!!,
            createdAt = article.createdAt!!,
            updatedAt = article.updatedAt!!,
            title = article.title,
            content = article.content,
            slug = article.slug,
            published = article.published,
        )
    }
}
