package kim.myeongjae.common.domain.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@MappedSuperclass
abstract class BaseEntity<T>(
    @field:Transient
    protected var idConstructor: (id: Long) -> T,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val _id: Long = 0L
    val id: T
        get() = idConstructor(_id)

    /**
     * created_at과 updated_at은 DB에서 아래 schema로 자동으로 설정하기 때문에 애플리케이션에서 따로 set을 하지 않는다.
     *   created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP (6),
     *   updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP (6) ON UPDATE CURRENT_TIMESTAMP (6),
     *
     *  jpa수준에서 값을 설정하는 코드는 [com.deermobility.deerdomain.audit.AuditDateEntity] 참고.
     */
    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)")
    lateinit var createdAt: ZonedDateTime

    @UpdateTimestamp
    @Column(
        name = "updated_at",
        columnDefinition = "DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)",
    )
    lateinit var updatedAt: ZonedDateTime

    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null

    /**
     * 자식 클래스에서 postLoad에서 idConstructor를 넣어줘야 id property를 조회할 때 에러가 발생하지 않는다.
     * 예)
     *   override fun postLoad() {
     *     this.idConstructor = ::ArticleId
     *   }
     */
    @PostLoad
    protected abstract fun postLoad()
}
