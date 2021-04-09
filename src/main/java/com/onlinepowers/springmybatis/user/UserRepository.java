package com.onlinepowers.springmybatis.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {


	int countByLoginId(String loginId);

//	@Query("SELECT x FROM User x left join fetch x.userDetail left join fetch x.userRole where x.loginId=?1")
	User findByLoginId(String loginId);



//	@Query(value = "select IFNULL(MAX(id), 0) from op_user", nativeQuery = true)
//	int getMaxPk;

	//  Member findByName(String name);  //By + 해당 엔티티의 필드 이름 -쿼리 Where절 뒤에 이름 전달
//  Page<Member> findByName(String name, Pageable pageable);
//	키워드별로 쿼리문 작성
//	다음과 같은 방식으로 키워드만 있으면, JPA를 통해서 SQL이 자동 생성됩니다.
//	키워드는 다음과 같다. 키워드 : And Or Is Equals Between

}
