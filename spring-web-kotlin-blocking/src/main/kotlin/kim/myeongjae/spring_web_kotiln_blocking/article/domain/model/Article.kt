package kim.myeongjae.spring_web_kotiln_blocking.article.domain.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Article(
        var title: String,
        var content: String,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @CreationTimestamp
    var createdAt: ZonedDateTime? = null
    @UpdateTimestamp
    var updatedAt: ZonedDateTime? = null

    var published = false

    fun publish() {
        published = true
    }

    fun unpublish() {
        published = false
    }
}