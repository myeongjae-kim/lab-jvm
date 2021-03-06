package kim.myeongjae.spring_web_kotiln_blocking.article.domain.model

import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@Transactional
@DataJpaTest
@Sql("/data/test-article.sql")
class ArticleRepositoryTest @Autowired constructor (
    val entityManager: TestEntityManager,
    val articleRepository: ArticleRepository,
) {

    @Nested
    inner class SaveAndFindById {
        @Test
        fun `should pass`() {
            val article = Article(title = "title", content = "content", slug = "slug")
            articleRepository.save(article)

            entityManager.flush()
            entityManager.clear()

            val savedArticle = articleRepository.findById(article.id!!).orElseThrow()

            then(savedArticle).usingRecursiveComparison().isEqualTo(article)
        }
    }

    @Nested
    inner class FindBySlug {
        @Test
        fun `should pass`() {
            val article = articleRepository.findBySlug("slug2")
            then(article).isNotNull
        }
    }

    @Nested
    inner class FindBySlugAndActivatedTrue {
        @Test
        fun `should throw EmptyResultDataAccessException when an article doesn't exist`() {
            val article1 = articleRepository.findBySlugAndPublishedTrue("slug1")
            then(article1).isNotNull

            thenThrownBy {
                articleRepository.findBySlugAndPublishedTrue("slug2")
            }.isInstanceOf(EmptyResultDataAccessException::class.java)
        }
    }

    @Nested
    inner class FindAllByPublishedTrue {
        @Test
        fun `should pass`() {
            val pageable = PageRequest.of(0, 10)

            val articles = articleRepository.findAllByPublishedTrue(pageable)

            then(articles.content).hasSize(1)
        }
    }
}
