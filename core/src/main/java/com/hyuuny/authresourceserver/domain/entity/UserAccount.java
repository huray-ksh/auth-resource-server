package com.hyuuny.authresourceserver.domain.entity;

import com.hyuuny.authresourceserver.common.jpa.domain.NonCreatedByAuditable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

import static org.springframework.util.ObjectUtils.isEmpty;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class UserAccount extends NonCreatedByAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String mobilePhoneNumber;

    private LocalDateTime lastLoginAt;

    @OneToOne(mappedBy = "userAccount", fetch = FetchType.LAZY)
    private User user;

    public Long toUserId() {
        return this.user.getId();
    }

    public void signUp(PasswordEncoder passwordEncoder) {
        if (!isEmpty(this.password)) {
            this.password = passwordEncoder.encode(this.password);
        }
    }

}
