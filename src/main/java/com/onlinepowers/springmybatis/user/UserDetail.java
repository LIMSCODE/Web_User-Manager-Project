
package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_USER_DETAIL")
@OnDelete(action = OnDeleteAction.CASCADE)
public class UserDetail extends JpaPaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    public Long userId;

    @NotEmpty(message="우편번호 입력해주세요")// null, 빈 문자열(스페이스 포함X) 불가
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "ZIPCODE")
    public String zipcode;

    @NotEmpty(message="주소 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "ADDRESS")
    public String address;

    @NotEmpty(message="상세주소 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "ADDRESS_DETAIL")
    public String addressDetail;

    @NotEmpty(message="전화번호 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "PHONE_NUMBER")
    public String phoneNumber;

    @NotNull(message="수신여부 체크해주세요")
    @Column(name = "RECEIVE_SMS")
    public String receiveSms;

    public String getReceiveSmsTitle() {
        if (this.receiveSms == null) {
            return "";

        } else {
            return "1".equals(this.receiveSms) ? "수신" : "수신안함" ;
        }
    }

}