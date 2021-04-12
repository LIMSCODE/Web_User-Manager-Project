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
	public void searchTest() {

	}
	/*
	@Test
	public void querydsl() {
		//given
		User user = userRepository.findById(1);  //가져와짐

		//when
		List<User> result = userRepositorySupport.findByName(user.getName());

		//then
		Assertions.assertThat(result.get(0).getName()).isEqualTo("1");

	}
	 */



}