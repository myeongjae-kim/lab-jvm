package kim.myeongjae.common.domain.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@MappedSuperclass
abstract class BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected val _id: Long = 0L

    /**
     * created_at과 updated_at은 DB에서 아래 schema로 자동으로 설정하기 때문에 애플리케이션에서 따로 set을 하지 않는다.
     *   created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP (6),
     *   updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP (6) ON UPDATE CURRENT_TIMESTAMP (6),
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
}
