package com.onlinepowers.springmybatis.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;

    public UserRepositorySupport(JPAQueryFactory queryFactory, UserRepository userRepository) {
        super(User.class);
        this.queryFactory = queryFactory;
        this.userRepository = userRepository;
    }


    /**
     * 페이징 적용한 검색쿼리
     * @param user
     * @param pageable
     * @return
     */
    Page<User> getUserListPagination(User user, Pageable pageable) {

        QUser qUser = QUser.user;
        QUserDetail qUserDetail = QUserDetail.userDetail;
        QUserRole qUserRole = QUserRole.userRole;

        BooleanBuilder builder = new BooleanBuilder();

        if ("all".equals(user.getSearchType())) {   //searchType이 전체(all)일때
            builder.and(qUser.name.contains(user.getSearchKeyword())     //eq, contains().not(), isNotNull()
                    .or(qUser.loginId.contains(user.getSearchKeyword()))
                    .or(qUser.email.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.zipcode.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.address.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.addressDetail.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.phoneNumber.contains(user.getSearchKeyword())));
        }

        if ("name".equals(user.getSearchType())) {
            builder.and(qUser.name.contains(user.getSearchKeyword()));
        }

        if ("loginId".equals(user.getSearchType())) {
            builder.and(qUser.loginId.contains(user.getSearchKeyword()));
        }

        if ("email".equals(user.getSearchType())) {
            builder.and(qUser.email.contains(user.getSearchKeyword()));
        }

        if ("zipcode".equals(user.getSearchType())) {
            builder.and(qUser.userDetail.zipcode.contains(user.getSearchKeyword()));
        }

        if ("address".equals(user.getSearchType())) {
            builder.and(qUser.userDetail.address.contains(user.getSearchKeyword()));
        }

        if ("addressDetail".equals(user.getSearchType())) {
            builder.and(qUser.userDetail.addressDetail.contains(user.getSearchKeyword()));
        }

        if ("phoneNumber".equals(user.getSearchType())) {
            builder.and(qUser.userDetail.phoneNumber.contains(user.getSearchKeyword()));
        }

        QueryResults<User> result = queryFactory
                .selectFrom(qUser)
                .leftJoin(qUserDetail).on(qUser.id.eq(qUserDetail.userId))
                .leftJoin(qUserRole).on(qUser.id.eq(qUserRole.userId))
                .where(builder)
                .orderBy(qUser.id.desc())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
