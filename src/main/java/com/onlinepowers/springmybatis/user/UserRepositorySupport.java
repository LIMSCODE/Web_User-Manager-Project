package com.onlinepowers.springmybatis.user;

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

    //From 으로 쿼리Dsl과 연동할 테이블 가져옴
    QUser qUser = new QUser("a");
    QUserDetail qUserDetail = new QUserDetail("b");
    QUserRole qUserRole = new QUserRole("c");


    /**
     * 페이징 적용한 검색쿼리
     * @param user
     * @param pageable
     * @return
     */
    Page<User> getUserListPagination(User user, Pageable pageable) {

        QueryResults<User> result = queryFactory
                .selectFrom(qUser)
                .leftJoin(qUserDetail).on(qUser.id.eq(qUserDetail.userId))
                .leftJoin(qUserRole).on(qUser.id.eq(qUserRole.userId))
                .where(
                        eqAll(user),   //검색조건 user.searchKeyword가 null일때
                        eqName(user),
                        eqLoginId(user),
                        eqEmail(user),
                        eqZipcode(user),
                        eqAddress(user),
                        eqAddressDetail(user),
                        eqPhoneNumber(user)
                )
                .orderBy(qUser.id.desc())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    /**
     * BuileanExpression
     * 쿼리의 where절 생성 (BooleanBuilder 대신 사용)
     * searchType : 전체, 이름, 아이디, 이메일, 우편번호, 주소, 상세주소, 전화번호 각각 적용
     * @param user
     * @return
     */
    BooleanExpression eqAll(User user) {

        if (StringUtils.isEmpty(user.getSearchType())) {    //empty일시 검색안되도록
            return null;
        }
        if ("all".equals(user.getSearchType())) {   //searchType이 전체(all)일때
            return qUser.name.contains(user.getSearchKeyword())     //eq, contains().not(), isNotNull()
                    .or(qUser.loginId.contains(user.getSearchKeyword()))
                    .or(qUser.email.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.zipcode.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.address.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.addressDetail.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.phoneNumber.contains(user.getSearchKeyword()));
        }
        return null;
    }

    BooleanExpression eqName(User user) {

        if ("name".equals(user.getSearchType())) {
            return qUser.name.contains(user.getSearchKeyword());
        }
        return null;
    }

    BooleanExpression eqLoginId(User user) {

        if ("loginId".equals(user.getSearchType())) {
            return qUser.loginId.contains(user.getSearchKeyword());
        }
        return null;
    }

    BooleanExpression eqEmail(User user) {

        if ("email".equals(user.getSearchType())) {
            return qUser.email.contains(user.getSearchKeyword());
        }
        return null;
    }

    BooleanExpression eqZipcode(User user) {

        if ("zipcode".equals(user.getSearchType())) {
            return qUser.userDetail.zipcode.contains(user.getSearchKeyword());
        }
        return null;
    }

    BooleanExpression eqAddress(User user) {

        if ("address".equals(user.getSearchType())) {
            return qUser.userDetail.address.contains(user.getSearchKeyword());
        }
        return null;
    }

    BooleanExpression eqAddressDetail(User user) {

        if ("addressDetail".equals(user.getSearchType())) {
            return qUser.userDetail.addressDetail.contains(user.getSearchKeyword());
        }
        return null;
    }

    BooleanExpression eqPhoneNumber(User user) {

        if ("phoneNumber".equals(user.getSearchType())) {
            return qUser.userDetail.phoneNumber.contains(user.getSearchKeyword());
        }
        return null;
    }


    /**
     * 특정 이름으로 검색 (Junit 테스트 예시)
     * @param name
     * @return
     */
    List<User> findByName(String name) {

        return queryFactory
                .selectFrom(qUser)	//빨간줄
                .where(qUser.name.eq(name))
                .fetch();
    }

}
