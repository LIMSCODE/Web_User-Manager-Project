package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_USER")
public class User extends Criteria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	private long pagingId;

	@NotEmpty(message = "이름 입력해주세요")
	@Size(max = 12, message = "12글자 이하로 입력하세요")
	@Column(name="NAME", nullable=false)
	private String name;

	@NotEmpty(message = "아이디 입력해주세요")
	@Size(max = 12, message = "12글자 이하로 입력하세요")
	@Pattern(regexp = "^[a-z]+[a-z0-9]{5,19}", message = "아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.")
	@Column(name="LOGIN_ID")
	private String loginId;

	@NotEmpty(message = "패스워드 입력해주세요")
	@Column(name="PASSWORD")
	private String password;

	@NotEmpty(message = "이메일 입력해주세요")
	@Size(max = 30, message = "30글자 이하로 입력하세요")
	@Email
	@Column(name="EMAIL")
	private String email;

	@Column(name="CREATED_DATE")
	private String createdDate;

	@Valid
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="USER_ID", insertable = false, updatable = false)
	public UserDetail userDetail;

	@NotNull(message = "직위 체크 해주세요")
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="USER_ID", insertable = false, updatable = false)
	public UserRole userRole;


	public User(User user) {
	}
}
