package kim.myeongjae.spring_web_kotiln_blocking.article.api

import kim.myeongjae.spring_web_kotiln_blocking.article.api.dto.ArticleResponseDto
import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticleController @Autowired constructor(private val articleRepository: ArticleRepository) {
    @GetMapping("/{slug}")
    fun get(@PathVariable slug: String): ArticleResponseDto {
        val res = ArticleResponseDto.from(articleRepository.findBySlug(slug))

        return res;
    }
}