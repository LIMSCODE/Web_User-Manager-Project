package com.onlinepowers.springmybatis.jwt;

import com.onlinepowers.springmybatis.security.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private LoginUserDetailsService loginUserDetailsService;


	/**
	 * 요청이 들어올 때 마다 JWT를 인증할 Filter (시큐리티에서 필터처리)
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		int isLoginUrl = request.getRequestURI().indexOf("/login");
		int isCreateUrl = request.getRequestURI().indexOf("/create");
		int isDeleteUrl = request.getRequestURI().indexOf("/delete");
		int isSearchUrl = request.getRequestURI().indexOf("/ajax-list");
		int isEditUrl = request.getRequestURI().indexOf("/edit-detail");
		int isPasswordUrl = request.getRequestURI().indexOf("/password-check");

		if (request.getRequestURI().startsWith("/api") && isLoginUrl == -1 &&
				isCreateUrl == -1 && isDeleteUrl == -1&& isSearchUrl == -1 && isEditUrl == -1
				&& isPasswordUrl == -1) {       //api이고 인증컨트롤러가 아닐때

			try {
				String token = getToken(request);   //아래의 getToken함수 실행
				System.out.println("필터로 토큰 넘어왔는지 확인 ==============" + token);

				if (token == null) {
					response.sendError(401, "권한 없음");
					return;
				}

				//헤더에 포함시킨 Bearer 토큰을 받으면 토큰을 추출하여 올바른 토큰인지 체크
				if (token != null && jwtTokenProvider.validateToken(token)) {
					String username = jwtTokenProvider.getUserPk(token);

					//로그인 객체 가져와서 토큰에 있는 정보(username, role 등) 인증
					UserDetails userDetails = loginUserDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication
							= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					System.out.println("헤더 포함객체 확인 ==============" + authentication.getPrincipal());
					// SecurityContext에 Authentication 객체를 저장합니다.
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			} catch (Exception e) {
				logger.error("Cannot set user authentication: {}", e);
			}

		} else {

		}
			filterChain.doFilter(request, response);
	}


	/**
	 * 헤더에서 Authorization값을 꺼내어 토큰 가져오기
	 * @param request
	 * @return
	 */
	private String getToken(HttpServletRequest request) {

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());    //시작위치 index7부터 자름
		}
		return null;
	}

}
