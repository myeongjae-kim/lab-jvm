package kim.myeongjae.springwebkotilnblocking.article.api.dto

import jakarta.validation.constraints.NotBlank
import kim.myeongjae.springwebkotilnblocking.article.domain.model.Article

data class ArticleRequestDto(
    @field:NotBlank val title: String,
    @field:NotBlank val content: String,
) {
    fun entity(slug: String): Article = Article(title = title, content = content, slug = slug)
}
