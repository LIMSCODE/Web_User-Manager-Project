package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail extends Criteria {

    public long userId;

    @NotNull (message="우편번호 입력해주세요")
    @Size (min = 2, max= 30, message="2글자 이상, 30글자 이하로 입력하세요")
    public String zipcode;

    @NotNull (message="주소 입력해주세요")
    public String address;

    @NotNull (message="상세주소 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    public String addressDetail;

    @NotNull (message="이름 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    public String phoneNumber;

    @NotNull (message="수신여부 체크해주세요")
    public String receiveSms;

    public String getReceiveSmsTitle() {
        if (this.receiveSms == null) {
            return "";
        } else {
            return "1".equals(this.receiveSms) ? "수신" : "수신안함" ;
        }
    }

}