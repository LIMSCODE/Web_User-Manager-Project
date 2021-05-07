package com.onlinepowers.springmybatis.jwt;

import com.onlinepowers.springmybatis.security.LoginUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {     // JWT토큰 생성 및 유효성을 검증하는 컴포넌트

	public static final String SECRET_KEY = "dsaidmosidqnwpqe";     //secretKey

	public static final Long EXPIRATION_TIME = 15 * 60 * 1000L;     //tokenValidTime

	private final LoginUserDetailsService loginUserDetailsService;


	/**
	 * 객체 초기화, secretKey를 Base64로 인코딩
	 */
	@PostConstruct
	protected void init() {
		String secretKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	}


	/**
	 * JWT 토큰 생성
	 * @param userPk
	 * @param roles
	 * @return
	 */
	public String createToken(String userPk, String roles) {
		Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
		claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims)  // 정보 저장
				.setIssuedAt(now)   // 토큰 발행 시간 정보
				.setExpiration(new Date(now.getTime() + EXPIRATION_TIME)) // set Expire Time
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 사용할 암호화 알고리즘과
				// signature 에 들어갈 secret값 세팅
				.compact();
	}


	/**
	 * JWT 토큰에서 인증 정보 조회
	 * @param token
	 * @return
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = loginUserDetailsService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}


	/**
	 * 토큰에서 회원 정보 추출
	 * @param token
	 * @return
	 */
	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
	}


	/**
	 * Request의 Header에서 token 값을 가져온다. "X-AUTH-TOKEN" : "TOKEN값'
	 * @param request
	 * @return
	 */
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("X-AUTH-TOKEN");
	}


	/**
	 * 토큰의 유효성 + 만료일자 확인
	 * @param jwtToken
	 * @return
	 */
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		}
		catch (Exception e) {
			SecurityContextHolder.clearContext();
			return false;
		}
	}
}
