package kim.myeongjae.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.data.domain.Page

/**
 * JSON serializer for [Page].
 * https://gist.github.com/jsadeli/3cb5c2d788c853099f24c68709931b50
 * https://github.com/spring-projects/spring-data-commons/issues/2987#issuecomment-1829003131
 *
 * @since 2023-11-27
 */
@JsonComponent
class PageJsonSerializer<T> : JsonSerializer<Page<T>>() {

    /**
     * Method that can be called to ask implementation to serialize values of a type this serializer
     * handles. This serializer replicates pre-`Spring Boot 3.2.0` JSON structure.
     *
     * @param page Value to serialize; CANNOT be null.
     * @param gen Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for serializing Objects value
     *   contains, if any.
     */
    override fun serialize(page: Page<T>, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeStartObject() // means start, like '{}'

        gen.writeObjectField("content", page.content)

        gen.writeBooleanField("empty", page.isEmpty)
        gen.writeBooleanField("first", page.isFirst)
        gen.writeBooleanField("last", page.isLast)
        gen.writeNumberField("number", page.number)
        gen.writeNumberField("numberOfElements", page.numberOfElements)
        gen.writeNumberField("size", page.size)
        gen.writeNumberField("totalPages", page.totalPages)
        gen.writeNumberField("totalElements", page.totalElements)

        // mostly duplicate data (pageable.pageSize -> size, pageable.offset -> number, etc.)
        // need special care in `Spring Boot 3.2.0` if `pageable` object is `unpaged`; otherwise error
        if (page.pageable.isUnpaged) {
            gen.writeStringField("pageable", "INSTANCE")
        } else {
            gen.writeObjectField("pageable", page.pageable)
        }

        gen.writeObjectField("sort", page.sort)

        gen.writeEndObject()
    }
}
