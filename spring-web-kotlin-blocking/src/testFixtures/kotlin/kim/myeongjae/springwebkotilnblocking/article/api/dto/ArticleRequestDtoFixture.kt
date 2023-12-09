package kim.myeongjae.springwebkotilnblocking.article.api.dto

class ArticleRequestDtoFixture {
    companion object {
        fun create(): ArticleRequestDto = ArticleRequestDto(title = "title", content = "content")
    }
}
