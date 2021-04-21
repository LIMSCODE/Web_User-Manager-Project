package com.onlinepowers.springmybatis.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizedUserRepository {

	Page<User> getUserListPagination(User user, Pageable pageable);

}
