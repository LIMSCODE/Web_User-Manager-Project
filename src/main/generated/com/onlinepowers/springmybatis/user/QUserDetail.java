package com.onlinepowers.springmybatis.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserDetail is a Querydsl query type for UserDetail
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserDetail extends EntityPathBase<UserDetail> {

    private static final long serialVersionUID = 1345901783L;

    public static final QUserDetail userDetail = new QUserDetail("userDetail");

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath receiveSms = createString("receiveSms");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath zipcode = createString("zipcode");

    public QUserDetail(String variable) {
        super(UserDetail.class, forVariable(variable));
    }

    public QUserDetail(Path<? extends UserDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserDetail(PathMetadata metadata) {
        super(UserDetail.class, metadata);
    }

}

