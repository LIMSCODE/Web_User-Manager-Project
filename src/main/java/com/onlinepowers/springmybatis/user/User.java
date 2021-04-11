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

	@NotNull(message = "이름 입력해주세요")
	@NotEmpty // null, 빈 문자열(스페이스 포함X) 불가
	@NotBlank // null, 빈 문자열, 스페이스만 포함한 문자열 불가
	@Size(max = 12, message = "12글자 이하로 입력하세요")
	@Column(name="NAME", nullable=false)
	private String name;

	@NotNull(message = "아이디 입력해주세요")
	@Size(max = 12, message = "12글자 이하로 입력하세요")
	@Column(name="LOGIN_ID")
	private String loginId;

	@NotNull(message = "패스워드 입력해주세요")
	@Column(name="PASSWORD")
	private String password;

	@NotNull(message = "이메일 입력해주세요")
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



	@PrePersist
	public void prePersist() {
		this.userRole.authority = this.userRole.authority == null ? "ROLE_USER" : this.userRole.authority;
	}

}
