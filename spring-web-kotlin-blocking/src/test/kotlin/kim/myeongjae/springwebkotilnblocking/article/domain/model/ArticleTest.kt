package kim.myeongjae.springwebkotilnblocking.article.domain.model

import org.assertj.core.api.BDDAssertions
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils

class ArticleTest {

    @Nested
    inner class Construct {
        @Test
        fun `should pass`() {
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
    }

    @Nested
    inner class Publish {
        @Test
        fun `should pass`() {
            // given
            val article = ArticleFixture.create()
            ReflectionTestUtils.setField(article, "published", false)

            // when
            article.publish()

            // then
            then(article.published).isTrue
        }
    }

    @Nested
    inner class Unpublish {
        @Test
        fun `should pass`() {
            // given
            val article = ArticleFixture.create()
            ReflectionTestUtils.setField(article, "published", true)

            // when
            article.unpublish()

            // then
            then(article.published).isFalse
        }
    }

    @Nested
    inner class Update {
        @Test
        fun `should pass`() {
            // given
            val article = ArticleFixture.create()
            val dto = Article(title = "newTitle", content = "newContent", slug = "redundant")

            BDDAssertions.assertThat(article.title).isNotEqualTo(dto.title)
            BDDAssertions.assertThat(article.content).isNotEqualTo(dto.content)

            // when
            article.update(dto)

            // then
            BDDAssertions.assertThat(article.title).isEqualTo(dto.title)
            BDDAssertions.assertThat(article.content).isEqualTo(dto.content)
        }
    }
}
