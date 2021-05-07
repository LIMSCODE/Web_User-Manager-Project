package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void deleteUserById(long id) {
		userRepository.deleteById(id);
	}

	@Transactional
	@Override
	public void insertUser(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);  //Jpa에서는 user만 save하면 하위테이블도 모두 저장된다.
	}

	@Transactional
	@Override
	public void updateUser(User user) {

		String password = user.getPassword();

		if (!"".equals(password)) {     //비밀번호 공백이 아닐때만 해시로 만든다.
			user.setPassword(passwordEncoder.encode(password));		//비밀번호 암호화
		}

		user.getUserDetail().setUserId(user.getId());	//하위테이블 수정안되는 현상 해결
		user.getUserRole().setUserId(user.getId());

		if ("".equals(password)) {
			Optional<User> storedUser = userRepository.findById(user.getId());
			user.setPassword(storedUser.get().getPassword());       //공백으로 수정시, 저장된 비번과 같은것으로 인식
		}

		userRepository.save(user);      //공백일 경우, 기존의 값과 같다고 인식되므로, Dynamicupdate적용되서 password제외하고 update 돌아간다.
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
	public Page<User> getUserList(User user, Pageable pageable, @ModelAttribute("jpaPaging")JpaPaging jpaPaging) {

		Page<User> userPage = userRepository.getUserListPagination(user, pageable);
		List<User> results = userPage.getContent().stream().collect(Collectors.toList());

		long totalCount = userPage.getTotalElements();

		log.debug(String.valueOf(totalCount));  //3개 뜸
		log.debug(String.valueOf(userPage.getPageable().getPageNumber()));	//첫번째페이지:0

		for (int i = 0 ; i < results.size() ; i++) {	//results.size() : 검색시 페이지별 limit적용한 값

			// 해당 페이지에서 가장 큰 번호 구하기
			int start = (int) (totalCount - (userPage.getPageable().getPageNumber()) * userPage.getSize());
			// 하나씩 빼서 게시글을 뿌린다.
			int num = start - i;

			userPage.getContent().get(i).setPagingId(num);
		}

		return userPage;
	}


}