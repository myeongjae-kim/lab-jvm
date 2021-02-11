package kim.myeongjae.spring_web_kotiln_blocking.article.domain.model

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class ArticleRepositoryTest @Autowired constructor (
    val entityManager: TestEntityManager,
    val articleRepository: ArticleRepository
) {

    @Test
    fun saveAndFindById() {
        val article = Article(title = "title", content = "content")
        articleRepository.save(article)

        entityManager.flush()
        entityManager.clear()

        val savedArticle = articleRepository.findById(article.id!!).orElseThrow()

        then(savedArticle).usingRecursiveComparison().isEqualTo(article)
    }
}