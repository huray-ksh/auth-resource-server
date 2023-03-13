package com.hyuuny.authresourceserver.domain.entity;

import com.hyuuny.authresourceserver.code.ResponseCode;
import com.hyuuny.authresourceserver.common.exception.HttpStatusMessageException;
import com.hyuuny.authresourceserver.common.jpa.converter.UUIDConverter;
import com.hyuuny.authresourceserver.common.jpa.domain.NonCreatedByAuditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends NonCreatedByAuditable {

    @Getter
    @AllArgsConstructor
    public enum Status {
        ACTIVATION("활성화"),
        DEACTIVATION("비활성화"),
        LEAVE("탈퇴");

        public boolean isActiveUser() {
            return this == ACTIVATION;
        }

        private final String title;
    }

    @Getter
    @AllArgsConstructor
    public enum Gender {
        MALE("남"),
        FEMALE("여");

        private final String title;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Default
    @Convert(converter = UUIDConverter.class)
    @Column(unique = true, nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATION;

    @Column(nullable = false)
    private String name;

    @Enumerated
    private Gender gender;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private UserAccount userAccount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Role role;

    public void signUp(Role role, List<Privilege> privileges, PasswordEncoder passwordEncoder) {
        this.userAccount.signUp(passwordEncoder);
        role.addUser(this);
        role.getRolePrivileges().clear();
        privileges.forEach(role::addPrivilege);
    }

    public void assignRole(Role role) {
        if (!isEmpty(this.role)) {
            this.role.getUsers().remove(this);
        }
        this.role = role;
        this.role.getUsers().add(this);
    }

    public List<Privilege.PrivilegeType> getPrivilegeTypes() {
        return this.role.getRolePrivileges().stream()
                .map(RolePrivilege::getPrivilegeType)
                .toList();
    }

    public void signIn(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.userAccount.getPassword())) {
            throw new HttpStatusMessageException(ResponseCode.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

}
