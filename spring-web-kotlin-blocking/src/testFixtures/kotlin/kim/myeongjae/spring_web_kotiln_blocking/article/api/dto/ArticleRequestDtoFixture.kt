package kim.myeongjae.spring_web_kotiln_blocking.article.api.dto

class ArticleRequestDtoFixture {
    companion object {
        fun create(): ArticleRequestDto = ArticleRequestDto(title = "title", content = "content")
    }
}