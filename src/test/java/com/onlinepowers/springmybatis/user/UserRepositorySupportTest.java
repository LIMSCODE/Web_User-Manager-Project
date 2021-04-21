package com.onlinepowers.springmybatis.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositorySupportTest {

	@Autowired
	private UserRepository userRepository;

	/*
	@Test
	public void getUserListPagination() {

		//검색조건 설정
		User user = new User();

		user.setName("123ABC");
		user.setSearchType("name");
		user.setSearchKeyword("ABC");

		//qUser.name에서 searchKeyword를 포함한 값을 찾는다.
		//검색 결과 - searchKeyword가 ABC인 값이 담긴다.

		//pageable있는건 테스트 안되서 user까지만 있는 함수만들어서 테스트해야함
		Page<User> result = userRepositorySupport.getUserListPagination(user, Pageable.unpaged());

		//then
		Assertions.assertThat(result.getContent().get(0).getName()).isEqualTo("123ABC");
	}


	@Test
	public void findByName() {
		//given
		User user = userRepository.findByLoginId("aaa111");  //가져와짐

		//when
		List<User> result = (List<User>) userRepository.findByLoginId(user.getLoginId());

		//then
		Assertions.assertThat(result.get(0).getLoginId()).isEqualTo("aaa111");	//result안에 든 이름이 1인지 확인
																		//findByName함수가 작동하는지 확인
	}

*/


}