package com.onlinepowers.springmybatis.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRole is a Querydsl query type for UserRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRole extends EntityPathBase<UserRole> {

    private static final long serialVersionUID = 1869981308L;

    public static final QUserRole userRole = new QUserRole("userRole");

    public final StringPath authority = createString("authority");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserRole(String variable) {
        super(UserRole.class, forVariable(variable));
    }

    public QUserRole(Path<? extends UserRole> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRole(PathMetadata metadata) {
        super(UserRole.class, metadata);
    }

}

