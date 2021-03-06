package kim.myeongjae.spring_web_kotiln_blocking.article.domain.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(indexes = [Index(name = "ux_article_slug", columnList = "slug", unique = true)])
class Article(
    title: String,
    content: String,
    slug: String,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null; protected set
    @CreationTimestamp
    var createdAt: ZonedDateTime? = null; protected set
    @UpdateTimestamp
    var updatedAt: ZonedDateTime? = null; protected set

    var title: String = title; protected set
    var content: String = content; protected set
    var slug: String = slug; protected set
    var published = false; protected set

    fun publish() {
        published = true
    }

    fun unpublish() {
        published = false
    }

    fun update(dto: Article) {
        this.title = dto.title
        this.content = dto.content
    }
}
