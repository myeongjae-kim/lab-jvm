package kim.myeongjae.spring_web_kotiln_blocking.article.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql("/data/test-article.sql")
@Transactional
class ArticleControllerIntTest @Autowired constructor(val mvc: MockMvc) {

    @Test
    fun getArticle() {
        mvc.perform(MockMvcRequestBuilders.get("/articles/slug1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.published").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isString)
            .andDo(MockMvcResultHandlers.print())
    }
}