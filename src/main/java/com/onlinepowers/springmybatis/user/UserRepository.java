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

//  Page<Member> findByName(String name, Pageable pageable);

}
