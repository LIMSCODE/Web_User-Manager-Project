package com.onlinepowers.springmybatis.security;

import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

		User user = userRepository.findByLoginId(loginId);

		if (user == null) {
			throw new UsernameNotFoundException(loginId);
		}

		return new LoginUserDetails(user);
	}

}