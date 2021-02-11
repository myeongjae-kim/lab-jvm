package kim.myeongjae.spring_web_kotiln_blocking.article.domain.model

import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Long>