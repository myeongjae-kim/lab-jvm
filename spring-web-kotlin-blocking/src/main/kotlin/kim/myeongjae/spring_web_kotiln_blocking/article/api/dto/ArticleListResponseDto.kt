package kim.myeongjae.spring_web_kotiln_blocking.article.api.dto

import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.Article
import java.time.ZonedDateTime

data class ArticleListResponseDto(
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val title: String,
    val slug: String,
    val published: Boolean,
) {
    companion object {
        fun from(article: Article): ArticleListResponseDto = ArticleListResponseDto(
            createdAt = article.createdAt!!,
            updatedAt = article.updatedAt!!,
            title = article.title,
            slug = article.slug,
            published = article.published,
        )
    }
}
