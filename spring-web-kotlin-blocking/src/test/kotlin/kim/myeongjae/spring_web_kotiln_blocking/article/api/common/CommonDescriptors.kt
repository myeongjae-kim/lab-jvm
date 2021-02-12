package kim.myeongjae.spring_web_kotiln_blocking.article.api.common

import org.springframework.restdocs.headers.HeaderDocumentation

class CommonDescriptors {
    companion object {
        val internalHeaderDescriptor = HeaderDocumentation.headerWithName("Mj-Internal").description("관리자용 header")
    }
}
