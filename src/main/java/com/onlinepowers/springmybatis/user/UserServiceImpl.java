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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public void deleteUserById(long id) {

		userRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void insertUser(User user) {

		String password = user.getPassword();
		password = SHA256Util.getEncrypt(password, getMaxPk());
		user.setPassword(password);
		log.debug(password);

		userRepository.save(user);  //Jpa에서는 user만 save하면 하위테이블도 모두 저장되는지 -된다.
//		userMapper.insertUserDetail(user.userDetail);   //mybatis 에서는 xml에서 insertDetail 따로 처리하기때문에
//		userMapper.insertUserRole(user.userRole);

	}

	@Transactional
	@Override
	public void updateUser(User user) {

		String password = user.getPassword();

		if (!"".equals(password)) {     //비밀번호 공백이 아닐때만 해시로 만든다.
			password = SHA256Util.getEncrypt(password, user.getId());
			user.setPassword(password);
		}

		userRepository.save(user);
//		userRepository.save(user.getUserDetail());
//		userRepository.save(user.getUserRole());

	}

	@Override
	public User getUserByLoginId(String loginId) {
		//return userMapper.getUserByLoginId(loginId);
		return userRepository.findByLoginId(loginId);
	}


	@Override
	public Optional<User> getUserById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public int getUserCountByLoginId(String loginId) {
		//int userCount = userMapper.getUserCountByLoginId(loginId);
		int userCount = userRepository.countByLoginId(loginId);
		return userCount;
	}




	@Override
	public int getMaxPk() {
		int maxPk = userMapper.getMaxPk();
		return maxPk + 1;
	}


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

}