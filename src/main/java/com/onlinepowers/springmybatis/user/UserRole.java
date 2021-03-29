package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends Criteria {

    public long userId;

    public String authority;

    public String getAuthorityTitle() {
        if (this.authority == null) {
            return "";
        } else {
            return "ROLE_OPMANAGER".equals(this.authority) ? "관리자" : "회원" ;
        }
    }

}