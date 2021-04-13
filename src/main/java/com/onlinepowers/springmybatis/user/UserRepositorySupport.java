package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport{

    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;

    public UserRepositorySupport(JPAQueryFactory queryFactory, UserRepository userRepository) {
        super(User.class);
        this.queryFactory = queryFactory;
        this.userRepository = userRepository;
    }

    public List<User> findByName(String name) {     //Junit 테스트 성공
        QUser qUser = new QUser("m");

        return queryFactory
                .selectFrom(qUser)	//빨간줄
                .where(qUser.name.eq(name))
                .fetch();
    }

    
    /*
    BooleanBuilder - 쿼리의 조건 설정인 where뒤의 조건을 생성 /복잡하니 BuileanExpression사용한다.
    phoneNumber 1개만 오면 where phoneNumber = phoneNumber
    2개 이상이 오면 모두 포함 where name = name and address = address and phoneNumber = phoneNumber
    */

    /*
    검색조건 예시
    member.username.eq("member1") // username = 'member1'
    member.username.eq("member1").not() // username != 'member1'
    member.username.contains("member") // like '%member%' 검색
    member.username.isNotNull() // username is not null
    */
    public List<User> getUserList(User user, Pageable pageable) {

        QUser qUser = new QUser("a");
        QUserDetail qUserDetail = new QUserDetail("b");
        QUserRole qUserRole = new QUserRole("c");

        return queryFactory
                .selectFrom(qUser)
                .leftJoin(qUserDetail).on(qUser.id.eq(qUserDetail.userId))
                .leftJoin(qUserRole).on(qUser.id.eq(qUserRole.userId))
                .where(
                        eqAll_null(user),   //검색조건 user.searchKeyword가 null일때
                        eqName(user),
                        eqLoginId(user),
                        eqEmail(user),
                        eqZipcode(user),
                        eqAddress(user),
                        eqAddressDetail(user),
                        eqPhoneNumber(user)
                )
                .fetch();

        //.where(qUser.name.contains(user.getSearchKeyword())
        // .list(qUser);
    }

    public Page<User> getUserListPagination(User user, Pageable pageable) {

        QUser qUser = new QUser("a");
        QUserDetail qUserDetail = new QUserDetail("b");
        QUserRole qUserRole = new QUserRole("c");


        QueryResults<User> result = queryFactory
                .selectFrom(qUser)
                .leftJoin(qUserDetail).on(qUser.id.eq(qUserDetail.userId))
                .leftJoin(qUserRole).on(qUser.id.eq(qUserRole.userId))
                .where(
                        eqAll_null(user),   //검색조건 user.searchKeyword가 null일때
                        eqName(user),
                        eqLoginId(user),
                        eqEmail(user),
                        eqZipcode(user),
                        eqAddress(user),
                        eqAddressDetail(user),
                        eqPhoneNumber(user)
                )
                .orderBy(qUser.id.desc())
                .fetchResults();


        // .offset((cri.currentPageNo) * cri.recordsPerPage)
        //                .limit(cri.recordsPerPage)

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());    //result에 9개의 List담김 확인
    }

    //전체, 이름, 아이디, 이메일, 우편번호, 주소, 상세주소, 전화번호
    private BooleanExpression eqAll_null(User user) {   //searchType이 null일떄 1
        QUser qUser = new QUser("a");

        if (StringUtils.isEmpty(user.getSearchType())) {
            return null;
        }

        if (user.getSearchType().equals(null) || user.getSearchType().equals("")) {
            return qUser.name.contains(user.getSearchKeyword())
                    .or(qUser.loginId.contains(user.getSearchKeyword()))
                    .or(qUser.email.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.zipcode.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.address.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.addressDetail.contains(user.getSearchKeyword()))
                    .or(qUser.userDetail.phoneNumber.contains(user.getSearchKeyword()));
        }
        return null;
    }

    private BooleanExpression eqName(User user) {   //searchType이 name일떄 2
        QUser qUser = new QUser("a");

        if ("name".equals(user.getSearchType())) {
            return qUser.name.contains(user.getSearchKeyword());
        }
        return null;
    }

    private BooleanExpression eqLoginId(User user) {    //searchType이 loginId일떄 3
        QUser qUser = new QUser("a");

        if ("loginId".equals(user.getSearchType())) {
            return qUser.loginId.contains(user.getSearchKeyword());
        }
        return null;
    }

    private BooleanExpression eqEmail(User user) {  //searchType이 email일떄 4
        QUser qUser = new QUser("a");

        if ("email".equals(user.getSearchType())) {
            return qUser.email.contains(user.getSearchKeyword());
        }
        return null;
    }

    private BooleanExpression eqZipcode(User user) {  //searchType이 zipcode일떄 5
        QUser qUser = new QUser("a");

        if ("zipcode".equals(user.getSearchType())) {
            return qUser.userDetail.zipcode.contains(user.getSearchKeyword());
        }
        return null;
    }

    private BooleanExpression eqAddress(User user) {  //searchType이 address일떄 6
        QUser qUser = new QUser("a");

        if ("address".equals(user.getSearchType())) {
            return qUser.userDetail.address.contains(user.getSearchKeyword());
        }
        return null;
    }

    private BooleanExpression eqAddressDetail(User user) {  //searchType이 addressDetail일떄 7
        QUser qUser = new QUser("a");

        if ("addressDetail".equals(user.getSearchType())) {
            return qUser.userDetail.addressDetail.contains(user.getSearchKeyword());
        }
        return null;
    }

    private BooleanExpression eqPhoneNumber(User user) {  //searchType이 phoneNumber일떄 8
        QUser qUser = new QUser("a");

        if ("phoneNumber".equals(user.getSearchType())) {
            return qUser.userDetail.phoneNumber.contains(user.getSearchKeyword());
        }
        return null;
    }

    
    //변형하기 전 원래 BooleanExpression 인터넷에 있는것
//    private BooleanExpression eqAddress(String name) {
//        if (StringUtils.isEmpty(name)) {
//            return null;
//        }
//        return user.name.eq(name);
//    }



    //CaseBuilder로 구현하기
//     <when test="'name'.equals(searchType)">
//    NAME LIKE CONCAT('%', #{searchKeyword}, '%')
//     </when>

//    public List<User> searchCase(User user) {
//
//        QUser qUser = new QUser("m");
//        return queryFactory
//                .from(qUser)
//                .select(new CaseBuilder()
//                                .when(user.searchType.eq("name")).then(user.getName().contains(user.searchKeyword))
//                                .when(eq("2")).then()
//                                .otherwise()
//
//    }

}
