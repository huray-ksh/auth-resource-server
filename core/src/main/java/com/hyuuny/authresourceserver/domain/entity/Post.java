package com.hyuuny.authresourceserver.domain.entity;

import com.hyuuny.authresourceserver.common.jpa.domain.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode.Include;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Post extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    @Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

}
