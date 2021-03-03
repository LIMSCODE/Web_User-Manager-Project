package com.onlinepowers.springmybatis.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class UserMapperTest {

	@Autowired
	UserMapper userMapper;

//	@Test
//	void getCount() {
//		log.info("## Count: {}", userMapper.getCount());
//		assertThat(userMapper.getCount()).isEqualTo(0);
//	}

}