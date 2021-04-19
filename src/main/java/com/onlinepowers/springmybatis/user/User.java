package com.onlinepowers.springmybatis.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onlinepowers.springmybatis.paging.JpaPaging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "OP_USER")
@DynamicUpdate
public class User extends JpaPaging {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private long pagingId;

	@Valid
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private UserDetail userDetail;

	@NotNull(message = "직위 체크 해주세요")
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private UserRole userRole;

	@NotEmpty(message = "이름 입력해주세요")
	@Size(max = 12, message = "12글자 이하로 입력하세요")
	@Column(nullable=false)
	private String name;

	@NotEmpty(message = "아이디 입력해주세요")
	@Size(max = 12, message = "12글자 이하로 입력하세요")
	@Pattern(regexp = "^[a-z]+[a-z0-9]{5,19}", message = "아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.")
	private String loginId;

	private String password;

	@NotEmpty(message = "이메일 입력해주세요")
	@Size(max = 30, message = "30글자 이하로 입력하세요")
	@Email
	private String email;

	@CreatedDate
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	@Column(nullable = false, updatable = false)
	private String createdDate;

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
		this.userDetail.setUser(this);
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
		this.userRole.setUser(this);
	}

}
