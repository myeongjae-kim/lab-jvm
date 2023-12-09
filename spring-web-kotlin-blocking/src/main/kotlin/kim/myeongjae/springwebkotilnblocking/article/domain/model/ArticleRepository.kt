package kim.myeongjae.springwebkotilnblocking.article.domain.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
    fun findBySlugAndPublishedTrue(slug: String): Article
    fun findBySlug(slug: String): Article

    fun findAllByPublishedTrue(pageable: Pageable?): Page<Article> // mockk써서 nullable argument대신 Pageable사용하기
}
