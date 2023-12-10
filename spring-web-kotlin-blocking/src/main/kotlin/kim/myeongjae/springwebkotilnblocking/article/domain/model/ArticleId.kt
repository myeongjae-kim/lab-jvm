package kim.myeongjae.springwebkotilnblocking.article.domain.model

@JvmInline
value class ArticleId(private val _value: Long) {
    val value: Long
        get() = _value
}
