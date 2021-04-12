package com.onlinepowers.springmybatis.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	int countByLoginId(String loginId);

//	@Query("SELECT x FROM User x left join fetch x.userDetail left join fetch x.userRole where x.loginId=?1")
	User findByLoginId(String loginId); //로그인시, 세션저장시
	User findById(long id); //수정창 띄울때

	@Query(value = "SELECT IFNULL(MAX(id), 0) FROM op_user", nativeQuery = true)
	long getMaxPK();

	@Modifying
	@Query(value = "UPDATE op_user set user_id=(SELECT * FROM (SELECT id FROM op_user WHERE id=:id) as A) WHERE id=:id", nativeQuery = true)
	void setUserFK(Long id);
//  Mysql 돌아가는 update쿼리  http://thinkpost-it.blogspot.com/2018/10/mysql.html
//  UPDATE op_user set user_id=(SELECT * FROM (SELECT id FROM op_user WHERE id=1)as A) WHERE id=1;
	//value = "UPDATE op_user user_id=(SELECT id FROM op_user WHERE id=:id)" 오류




	//  Member findByName(String name);  //By + 해당 엔티티의 필드 이름 -쿼리 Where절 뒤에 이름 전달
//  Page<Member> findByName(String name, Pageable pageable);
//	키워드별로 쿼리문 작성
//	다음과 같은 방식으로 키워드만 있으면, JPA를 통해서 SQL이 자동 생성됩니다.
//	키워드는 다음과 같다. 키워드 : And Or Is Equals Between

}
