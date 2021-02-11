package kim.myeongjae.spring_web_kotiln_blocking.article.api.dto

import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.Article
import java.time.ZonedDateTime

data class ArticleResponseDto(
    val id: Long,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val title: String,
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

