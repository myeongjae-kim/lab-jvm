package kim.myeongjae.springwebkotilnblocking.article.api.dto

import kim.myeongjae.springwebkotilnblocking.article.domain.model.Article
import javax.validation.constraints.NotBlank

data class ArticleRequestDto(
    @field:NotBlank val title: String,
    @field:NotBlank val content: String,
) {
    fun entity(slug: String): Article = Article(title = title, content = content, slug = slug)
}
