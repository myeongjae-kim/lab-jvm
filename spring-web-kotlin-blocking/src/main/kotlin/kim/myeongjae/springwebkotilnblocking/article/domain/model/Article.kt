package kim.myeongjae.springwebkotilnblocking.article.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import kim.myeongjae.common.domain.model.BaseEntity

@Entity
@Table(indexes = [Index(name = "ux_article_slug", columnList = "slug", unique = true)])
class Article(
    title: String,
    content: String,
    slug: String,
) : BaseEntity() {
    val id: ArticleId
        get() = ArticleId(this._id)

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
