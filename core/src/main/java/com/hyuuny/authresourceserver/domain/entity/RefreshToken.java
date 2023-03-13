package com.hyuuny.authresourceserver.domain.entity;

import com.hyuuny.authresourceserver.code.RefreshTokenType;
import com.hyuuny.authresourceserver.common.jpa.domain.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Audited
@AuditOverride(forClass = Auditable.class)
@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "refresh_token_index_ref_id", columnList = "ref_id"),
                @Index(name = "refresh_token_index_unique_issue_id", columnList = "issue_id", unique = true),
                @Index(name = "refresh_token_index_find", columnList = "type, ref_id, issue_id", unique = true)
        }
)
public class RefreshToken extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RefreshTokenType type;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "issue_id", nullable = false)
    private String issueId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Builder
    public RefreshToken(
            @NonNull RefreshTokenType type,
            @NonNull Long refId,
            @NonNull String issueId,
            @NonNull String refreshToken,
            @NonNull LocalDateTime issuedAt
    ) {
        this.type = type;
        this.refId = refId;
        this.issueId = issueId;
        this.refreshToken = refreshToken;
        this.issuedAt = issuedAt;
    }

}
