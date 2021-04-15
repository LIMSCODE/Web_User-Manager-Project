package com.onlinepowers.springmybatis.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

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


	/**
	 * 현재 DB에서 최대 PK (회원정보 insert시 DB암호화, User테이블의 FK설정시 이용)
	 * @return
	 */
	@Query(value = "SELECT coalesce(MAX(u.id), 0) FROM User u")
	long getMaxPK();


	/**
	 * User테이블의 FK UPDATE (INSERT 후 DB의 최대 PK값으로)
	 * User 엔티티에 user_id 필드가 없어서 DB에있는 컬럼으로 사용하기 위해 nativeQuery사용
	 * @param id
	 */
	@Modifying
	@Query(value = "UPDATE op_user " +
			"set " +
			"user_id = (SELECT * FROM (SELECT id FROM op_user WHERE id = :id) as A) " +
			"WHERE id = :id", nativeQuery = true)
	void setUserFK(Long id);

	/**
	 * UPDATE JOIN쿼리
	 * jpql, queryDsl은 update시 join 제공안해서 nativeQuery사용
	 * @param user
	 */
	@Modifying
	@Query(value = " UPDATE op_user u " +
			"inner join op_user_detail ud on u.id = ud.user_id " +
			"inner join op_user_role ur on u.id = ur.user_id " +
			"set " +
			"u.email = :#{#paramUser.email}, " +
			"ud.zipcode = :#{#paramUser.userDetail.zipcode}, " +
			"ud.address = :#{#paramUser.userDetail.address}, " +
			"ud.address_detail = :#{#paramUser.userDetail.addressDetail}, " +
			"ud.phone_number = :#{#paramUser.userDetail.phoneNumber}, " +
			"ud.receive_sms = :#{#paramUser.userDetail.receiveSms} " +
			"where u.id = :#{#paramUser.id} ", nativeQuery = true)
	void saveWithOldPw(@Param("paramUser") User user);

	@Modifying
	@Query(value = " UPDATE op_user u " +
			"inner join op_user_detail ud on u.id = ud.user_id " +
			"inner join op_user_role ur on u.id = ur.user_id " +
			"set " +
			"u.password = :#{#paramUser.password}, " +
			"u.email = :#{#paramUser.email}, " +
			"ud.zipcode = :#{#paramUser.userDetail.zipcode}, " +
			"ud.address = :#{#paramUser.userDetail.address}, " +
			"ud.address_detail = :#{#paramUser.userDetail.addressDetail}, " +
			"ud.phone_number = :#{#paramUser.userDetail.phoneNumber}, " +
			"ud.receive_sms = :#{#paramUser.userDetail.receiveSms} " +
			"where u.id = :#{#paramUser.id} ", nativeQuery = true)
	void saveWithNewPw(@Param("paramUser") User user);

	//JPQL예시 : @Query("SELECT x FROM User x left join fetch x.userDetail left join fetch x.userRole where x.loginId=?1")

}
