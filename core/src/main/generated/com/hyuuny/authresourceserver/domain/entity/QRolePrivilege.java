package com.hyuuny.authresourceserver.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRolePrivilege is a Querydsl query type for RolePrivilege
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRolePrivilege extends EntityPathBase<RolePrivilege> {

    private static final long serialVersionUID = 671786346L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRolePrivilege rolePrivilege = new QRolePrivilege("rolePrivilege");

    public final com.hyuuny.authresourceserver.common.jpa.domain.QAuditable _super = new com.hyuuny.authresourceserver.common.jpa.domain.QAuditable(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPrivilege privilege;

    public final QRole role;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QRolePrivilege(String variable) {
        this(RolePrivilege.class, forVariable(variable), INITS);
    }

    public QRolePrivilege(Path<? extends RolePrivilege> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRolePrivilege(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRolePrivilege(PathMetadata metadata, PathInits inits) {
        this(RolePrivilege.class, metadata, inits);
    }

    public QRolePrivilege(Class<? extends RolePrivilege> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.privilege = inits.isInitialized("privilege") ? new QPrivilege(forProperty("privilege")) : null;
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role")) : null;
    }

}

