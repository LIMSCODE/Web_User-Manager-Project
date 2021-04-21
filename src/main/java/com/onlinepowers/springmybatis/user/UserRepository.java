package com.onlinepowers.springmybatis.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomizedUserRepository {

	/**
	 * 아이디 중복 확인
	 * @param loginId
	 * @return
	 */
	int countByLoginId(String loginId);


	/**
	 * LoginId로 User 객체 찾기 (로그인시, 세션저장시)
	 * @param loginId
	 * @return
	 */
	User findByLoginId(String loginId);


	/**
	 * Id로 User객체 찾기 (수정창 띄울때)
	 * @param id
	 * @return
	 */
	User findById(long id);

	//JPQL예시 : @Query("SELECT x FROM User x left join fetch x.userDetail left join fetch x.userRole where x.loginId=?1")

}
