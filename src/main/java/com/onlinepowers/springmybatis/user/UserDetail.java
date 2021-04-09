package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_USER_DETAIL")
@OnDelete(action = OnDeleteAction.CASCADE)
public class UserDetail extends Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "USER_ID")
    public long userId;

    @NotNull (message="우편번호 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "ZIPCODE")
    public String zipcode;

    @NotNull (message="주소 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "ADDRESS")
    public String address;

    @NotNull (message="상세주소 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "ADDRESS_DETAIL")
    public String addressDetail;

    @NotNull (message="전화번호 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    @Column(name = "PHONE_NUMBER")
    public String phoneNumber;

    @NotNull (message="수신여부 체크해주세요")
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