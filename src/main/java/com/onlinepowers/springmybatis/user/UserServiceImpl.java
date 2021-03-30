package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.onlinepowers.springmybatis.paging.PaginationInfo;
import com.onlinepowers.springmybatis.util.SHA256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	@Override
	public int getCountByParam(User user) {

		int userCount = userMapper.getCountByParam(user);

		return userCount;
	}

	@Override
	public List<User> getUserList(User user, @ModelAttribute("cri") Criteria cri) {

		List<User> userList = Collections.emptyList();
		int userCount = userMapper.getCountByParam(user);

		PaginationInfo paginationInfo = new PaginationInfo(user);
		paginationInfo.setTotalRecordCount(userCount);
		user.setPaginationInfo(paginationInfo);

		if (userCount > 0) {
			userList = userMapper.getUserList(user);
			Criteria criteria = new Criteria();

			int currentPageNo = cri.getCurrentPageNo(); //현재 페이지
			int totalRecordCount = userMapper.getCountByParam(user);    // 전체 데이터 개수
			int recordsPerPage = criteria.getRecordsPerPage();  //한페이지에 들어가는 데이터 개수
			int totalPageCount = (totalRecordCount) / recordsPerPage + 1;   // 전체 페이지 개수

			for (int i = 0; i < userList.size(); i++) {

				// 해당 페이지에서 가장 큰 번호 구하기
				int start = totalRecordCount - (currentPageNo - 1) * recordsPerPage;
				// 하나씩 빼서 게시글을 뿌린다.
				int num = start - i;

				userList.get(i).setPagingId(num);
			}
		}

		return userList;
	}

	@Override
	public User getUserById(long id) {
		return userMapper.getUserById(id);
	}

	@Override
	public void deleteUserById(long id) {
		userMapper.deleteUserById(id);
	}

	@Transactional
	@Override
	public void insertUser(User user) {

		String password = user.getPassword();
		password = SHA256Util.getEncrypt(password, getMaxPk());
		user.setPassword(password);
		log.debug(password);

		userMapper.insertUser(user);
		userMapper.insertUserDetail(user.userDetail);
		userMapper.insertUserRole(user.userRole);

	}

	@Transactional
	@Override
	public void updateUser(User user) {

		String password = user.getPassword();

		if (!"".equals(password)) {     //비밀번호 공백이 아닐때만 해시로 만든다.
			password = SHA256Util.getEncrypt(password, user.getId());
			user.setPassword(password);
		}

		userMapper.updateUser(user);
		userMapper.updateUserDetail(user.getUserDetail());
		userMapper.updateUserRole(user.getUserRole());

	}

	/**
	 * 페이지 넘버링 (최대 PK값 + 1)
	 * @return
	 */
	@Override
	public int getMaxPk() {
		int maxPk = userMapper.getMaxPk();
		return maxPk + 1;
	}

	@Override
	public int getUserCountByLoginId(String loginId) {
		int userCount = userMapper.getUserCountByLoginId(loginId);
		return userCount;
	}

	//로그인
	public User getUserByLoginId(String loginId) {
		return userMapper.getUserByLoginId(loginId);
	}

}