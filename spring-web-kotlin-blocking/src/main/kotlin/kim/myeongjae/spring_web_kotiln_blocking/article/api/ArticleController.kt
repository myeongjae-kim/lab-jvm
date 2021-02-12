package kim.myeongjae.spring_web_kotiln_blocking.article.api

import com.fasterxml.jackson.annotation.JsonView
import kim.myeongjae.common.Constants
import kim.myeongjae.common.jsonview.Views
import kim.myeongjae.spring_web_kotiln_blocking.article.api.dto.ArticleListResponseDto
import kim.myeongjae.spring_web_kotiln_blocking.article.api.dto.ArticleRequestDto
import kim.myeongjae.spring_web_kotiln_blocking.article.api.dto.ArticleResponseDto
import kim.myeongjae.spring_web_kotiln_blocking.article.domain.model.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/articles")
@Transactional(readOnly = true)
class ArticleController @Autowired constructor(private val articleRepository: ArticleRepository) {
    @GetMapping("/{slug}")
    @JsonView(Views.Public::class)
    fun getPublished(@PathVariable slug: String): ArticleResponseDto =
        ArticleResponseDto.from(articleRepository.findBySlugAndPublishedTrue(slug))

    @GetMapping(path = ["/{slug}"], headers = [Constants.HEADER_INTERNAL])
    fun get(@PathVariable slug: String): ArticleResponseDto =
        ArticleResponseDto.from(articleRepository.findBySlug(slug))

    @PostMapping(path = ["/{slug}"], headers = [Constants.HEADER_INTERNAL])
    @Transactional
    fun create(@PathVariable slug: String, @RequestBody @Valid req: ArticleRequestDto) {
        articleRepository.save(req.entity(slug))
    }

    @PutMapping(path = ["/{slug}"], headers = [Constants.HEADER_INTERNAL])
    @Transactional
    fun update(@PathVariable slug: String, @RequestBody @Valid req: ArticleRequestDto) {
        val article = articleRepository.findBySlug(slug)
        article.update(req.entity(slug))

        articleRepository.save(article)
    }

    @GetMapping
    fun getPublishedArticles(@RequestParam page: Int): Page<ArticleListResponseDto> =
        articleRepository.findAllByPublishedTrue(PageRequest.of(page, Constants.PAGE_SIZE))
            .map(ArticleListResponseDto::from)


    @GetMapping(headers = [Constants.HEADER_INTERNAL])
    fun getArticles(@RequestParam page: Int): Page<ArticleListResponseDto> =
        articleRepository.findAll(PageRequest.of(page, Constants.PAGE_SIZE))
            .map(ArticleListResponseDto::from)

    @PutMapping(path = ["/{slug}/publish"], headers = [Constants.HEADER_INTERNAL])
    @Transactional
    fun publish(@PathVariable slug: String) {
        val article = articleRepository.findBySlug(slug)
        article.publish()

        articleRepository.save(article)
    }

    @PutMapping(path = ["/{slug}/unpublish"], headers = [Constants.HEADER_INTERNAL])
    @Transactional
    fun unpublish(@PathVariable slug: String) {
        val article = articleRepository.findBySlug(slug)
        article.unpublish()

        articleRepository.save(article)
    }
}
