package kim.myeongjae.springwebkotilnblocking.article.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(indexes = [Index(name = "ux_article_slug", columnList = "slug", unique = true)])
class Article(
    title: String,
    content: String,
    slug: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private val _id: Long = 0L
    val id: ArticleId
        get() = ArticleId(_id)

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
