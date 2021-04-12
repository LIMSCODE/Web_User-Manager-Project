package com.onlinepowers.springmybatis.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import static com.onlinepowers.springmybatis.user.QUser.user;
import java.util.List;

@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    // queryDsl 예시
//    public List<User> findByWorkGroupNm(String name) {
//        return queryFactory.selectFrom(user)
//                .where(user.name.containsIgnoreCase(name))
//                .fetch();
//    }


    public List<User> findByName(String name) {     //Junit 테스트 성공
        QUser qUser = new QUser("m");

        return queryFactory
                .selectFrom(qUser)	//빨간줄
                .where(qUser.name.eq(name))
                .fetch();
    }

    ////검색조건
    //member.username.eq("member1") // username = 'member1'
    //member.username.eq("member1").not() // username != 'member1'
    //member.username.eq("member1").not() // username != 'member1'
    //member.username.contains("member") // like '%member%' 검색
    //member.username.isNotNull() // username is not null

    //BooleanBuilder - 쿼리의 조건 설정인 where뒤의 조건을 생성해주는 것이라고 생각하면 된다.
    //복잡하니 BuileanExpression사용한다.
    //phoneNumber 1개만 오면 where phoneNumber = phoneNumber
    //2개 이상이 오면 모두 포함 where name = name and address = address and phoneNumber = phoneNumber


    //일단 전체검색 제외하고 각각 검색시에만. (searchType=name일경우 - when으로 걸어야 한다.)
    public List<User> findDynamicQueryAdvance(User user) {
        QUser qUser = new QUser("m");
        return queryFactory
                .selectFrom(qUser)
                .leftJoin(qUser.userDetail)
                .leftJoin(qUser.userRole)
                .where(eqName(user),
                        eqAddress(user))
                .fetch();

        //.where(qUser.name.contains(user.getSearchKeyword())
        // .orderBy(qUser.id.desc())
        // .offset(10).limit(20)
        // .list(qUser);;
    }
    

    private BooleanExpression eqName(User user) {
        QUser qUser = new QUser("m");

        if (StringUtils.isEmpty(user.getName())) {
            return null;
        }

        if ("name".equals(user.searchType)) {
            return qUser.name.contains(user.getSearchKeyword());
        }
        return null;
    }

    private BooleanExpression eqAddress(User user) {
        QUser qUser = new QUser("m");

        if (StringUtils.isEmpty(user.getEmail())) {
            return null;
        }

        if ("email".equals(user.searchType)) {
            return qUser.email.eq(user.getSearchKeyword());
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
