package com.onlinepowers.springmybatis.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	int countByLoginId(String loginId);     //아이디 중복 확인
	User findByLoginId(String loginId);     //로그인시, 세션저장시
	User findById(long id);     //수정창 띄울때

	@Query(value = "SELECT coalesce(MAX(u.id), 0) FROM User u")
	long getMaxPK();

	//User 엔티티에 user_id 필드가 없어서 DB에있는 컬럼으로 사용하기 위해 nativeQuery사용
	@Modifying
	@Query(value = "UPDATE op_user " +
			"set user_id = (SELECT * FROM (SELECT id FROM op_user WHERE id = :id) as A) " +
			"WHERE id = :id", nativeQuery = true)
	void setUserFK(Long id);

	//jpql, queryDsl은 update시 join 제공안해서 nativeQuery사용
	@Modifying
	@Query(value = " UPDATE op_user u inner join op_user_detail ud on u.id = ud.user_id inner join op_user_role ur on u.id = ur.user_id " +
			"set u.email = :#{#paramUser.email}, " +
			"ud.zipcode = :#{#paramUser.userDetail.zipcode}, " +
			"ud.address = :#{#paramUser.userDetail.address}, " +
			"ud.address_detail = :#{#paramUser.userDetail.addressDetail}, " +
			"ud.phone_number = :#{#paramUser.userDetail.phoneNumber}, " +
			"ud.receive_sms = :#{#paramUser.userDetail.receiveSms} " +
			"where u.id = :#{#paramUser.id} ", nativeQuery = true)
	void saveWithOldPw(@Param("paramUser") User user);

	@Modifying
	@Query(value = " UPDATE op_user u inner join op_user_detail ud on u.id = ud.user_id inner join op_user_role ur on u.id = ur.user_id " +
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
