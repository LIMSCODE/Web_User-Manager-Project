package com.onlinepowers.springmybatis.user;

import lombok.EqualsAndHashCode;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositorySupportTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRepositorySupport userRepositorySupport;

	@Test
	public void findDynamicQueryAdvance() {

		//검색조건 설정
		User user = new User();
		user.setName("123ABC");
		user.setSearchType("name");
		user.setSearchKeyword("ABC");

		//qUser.name에서 searchKeyword를 포함한 값을 찾는다.
		//검색 결과 - searchKeyword가 ABC인 값이 담긴다.
		List<User> result = userRepositorySupport.findDynamicQueryAdvance(user);

		//then
		Assertions.assertThat(result.get(0).getName()).isEqualTo("123ABC");

	}

	/*
	@Test
	public void querydsl() {
		//given
		User user = userRepository.findById(1);  //가져와짐

		//when
		List<User> result = userRepositorySupport.findByName(user.getName());

		//then
		Assertions.assertThat(result.get(0).getName()).isEqualTo("1");	//result안에 든 이름이 1인지 확인
																		//findByName함수가 작동하는지 확인

	}
	 */



}