package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import com.onlinepowers.springmybatis.util.SHA256Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserRepositorySupport userRepositorySupport;
	private final UserMapper userMapper;

	@Override
	public void deleteUserById(long id) {

		userRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void insertUser(User user) {

		String password = user.getPassword();
		password = SHA256Util.getEncrypt(password, getMaxPK());
		user.setPassword(password);
		log.debug(password);


		//이렇게하면 안되서 DB에서 PK+1구해서 FK에 넣음
		user.getUserDetail().setUserId(user.getId());
		user.getUserRole().setUserId(user.getId());


		userRepository.save(user);  //Jpa에서는 user만 save하면 하위테이블도 모두 저장된다.
		userRepository.setUserFK(userRepository.getMaxPK());

	}

	@Transactional
	@Override
	public void updateUser(User user) {

		String password = user.getPassword();

		if (!"".equals(password)) {     //비밀번호 공백이 아닐때만 해시로 만든다.
			password = SHA256Util.getEncrypt(password, user.getId());
			user.setPassword(password);
		}

		user.getUserDetail().setUserId(user.getId());	//하위테이블 수정안되는 현상 해결
		user.getUserRole().setUserId(user.getId());

		//userRepository.save(user);
		userRepositorySupport.updateUser(user);

	}

	@Override
	public User getUserByLoginId(String loginId) {

		return userRepository.findByLoginId(loginId);
	}


	@Override
	public User getUserById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public int getUserCountByLoginId(String loginId) {

		int userCount = userRepository.countByLoginId(loginId);
		return userCount;
	}


	@Override
	public long getMaxPK() {
		long maxPk = userRepository.getMaxPK();
		return maxPk + 1;
	}


	@Override
	public Page<User> getUserList(User user, Pageable pageable, @ModelAttribute("jpaPaging")JpaPaging jpaPaging) {

		Page<User> userList = userRepositorySupport.getUserListPagination(user, pageable);
		List<User> results = userList.getContent().stream()
				.collect(Collectors.toList());

		long totalCount = userList.getTotalElements();

		log.debug(String.valueOf(totalCount));  //3개 뜸
		log.debug(String.valueOf(userList.getPageable().getPageNumber()));	//첫번째페이지:0

		for (int i = 0 ; i < results.size() ; i++) {	//results.size() : 검색시 페이지별 limit적용한 값

			// 해당 페이지에서 가장 큰 번호 구하기
			int start = (int) (totalCount - (userList.getPageable().getPageNumber()) * userList.getSize());
			// 하나씩 빼서 게시글을 뿌린다.
			int num = start - i;

			userList.getContent().get(i).setPagingId(num);
		}

		return userList;
	}

}