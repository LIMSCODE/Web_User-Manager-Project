package com.onlinepowers.springmybatis.user;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

	/**
	 * 검색된 회원 수
	 * @param user
	 * @return
	 */
	int getCountByParam(User user);

	/**
	 * 회원 목록
	 * @param user
	 * @return
	 */
	List<User> getUserList(User user);

	/**
	 * ID로 회원정보 가져오기
	 * @param id
	 * @return
	 */
	User getUserById(long id);

	/**
	 * 회원 삭제
	 * @param id
	 */
	void deleteUserById(long id);

	/**
	 * 회원 등록
	 * @param user
	 */
	void insertUser(User user);
	void insertUserDetail(UserDetail userDetail);
	void insertUserRole(UserRole userRole);

	/**
	 * 회원 정보 수정
	 * @param user
	 */
	void updateUser(User user);
	void updateUserDetail(UserDetail userDetail);
	void updateUserRole(UserRole userRole);

	/**
	 * 아이디 중복 확인시 해당 아이디 몇개인지
	 * @param loginId
	 * @return
	 */
	int getUserCountByLoginId(String loginId);

	/**
	 * 페이지 넘버링 (최대 PK값 + 1)
	 * @return
	 */
	int getMaxPk();

	/**
	 * 로그인시 회원정보 가져오기
	 * @param loginId
	 * @return
	 */
	User getUserByLoginId(String loginId);

}