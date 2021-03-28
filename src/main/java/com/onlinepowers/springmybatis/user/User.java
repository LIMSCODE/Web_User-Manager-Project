package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Criteria {

	private Long id;
	private long pagingId;

	@NotNull (message = "이름 입력해주세요")
	@NotEmpty // null, 빈 문자열(스페이스 포함X) 불가
	@NotBlank // null, 빈 문자열, 스페이스만 포함한 문자열 불가
	@Size (min = 2, max = 12, message = "2글자 이상, 12글자 이하로 입력하세요")
	private String name;

	@NotNull (message = "아이디 입력해주세요")
	@Size (max = 12, message = "12글자 이하로 입력하세요")
	private String loginId;

	@NotNull (message = "패스워드 입력해주세요")
	@Size (max = 8, message = "8글자 이하로 입력하세요")
	private String password;

	@NotNull (message = "이메일 입력해주세요")
	@Size (max = 30, message = "30글자 이하로 입력하세요")
	@Email
	private String email;

	private String createdDate;

	@Valid
	public UserDetail userDetail;
	@Valid
	public UserRole userRole;

}