package kim.myeongjae.spring_web_kotiln_blocking.article.domain.model

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils

class ArticleTest {

    @Test
    fun create() {
        // given
        val title = "title"
        val content = "content"
        val slug = "slug"

        // when
        val article = Article(title = title, content = content, slug = "slug")

        // then
        then(article).hasNoNullFieldsOrPropertiesExcept("id", "createdAt", "updatedAt")
        then(article.title).isEqualTo(title)
        then(article.content).isEqualTo(content)
        then(article.published).isEqualTo(false)
        then(article.slug).isEqualTo(slug)
    }

    @Test
    fun publish() {
        // given
        val article = ArticleFixture.create()
        ReflectionTestUtils.setField(article, "published", false)

        // when
        article.publish()

        // then
        then(article.published).isTrue
    }

    @Test
    fun unpublish() {
        // given
        val article = ArticleFixture.create()
        ReflectionTestUtils.setField(article, "published", true)

        // when
        article.unpublish()

        // then
        then(article.published).isFalse
    }
}