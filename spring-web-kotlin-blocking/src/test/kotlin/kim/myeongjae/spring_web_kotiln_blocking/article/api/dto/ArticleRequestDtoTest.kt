package kim.myeongjae.spring_web_kotiln_blocking.article.api.dto

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.test.util.ReflectionTestUtils
import javax.validation.Validation

class ArticleRequestDtoTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun entity() {
        val slug = "slug"
        val dto = ArticleRequestDtoFixture.create()

        assertThat(validator.validate(dto)).isEmpty()

        val article = dto.entity(slug)

        then(article.title).isEqualTo(dto.title)
        then(article.content).isEqualTo(dto.content)
        then(article.slug).isEqualTo(slug)

    }

    @ParameterizedTest
    @CsvSource(value = [
        "title,'',1",
        "content,'',1",
    ])
    fun validate_invalidInput_violation(fieldName: String, value: Any?, numberOfViolations: Int) {
        val dto = ArticleRequestDtoFixture.create()
        ReflectionTestUtils.setField(dto, fieldName, value)

        then(validator.validate(dto)).hasSize(numberOfViolations)
    }
}