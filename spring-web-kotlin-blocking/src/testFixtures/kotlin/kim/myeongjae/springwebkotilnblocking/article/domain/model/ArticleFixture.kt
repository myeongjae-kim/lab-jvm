package kim.myeongjae.springwebkotilnblocking.article.domain.model

import org.assertj.core.api.BDDAssertions.then
import org.springframework.test.util.ReflectionTestUtils
import java.time.ZonedDateTime

class ArticleFixture {
    companion object {
        fun create(): Article {
            val article = Article(title = "title", content = "content", slug = "slug")
            ReflectionTestUtils.setField(article, "_id", 1L)
            ReflectionTestUtils.setField(article, "createdAt", ZonedDateTime.now())
            ReflectionTestUtils.setField(article, "updatedAt", ZonedDateTime.now())
            article.publish()

            then(article).hasNoNullFieldsOrPropertiesExcept("deletedAt")

            return article
        }
    }
}
