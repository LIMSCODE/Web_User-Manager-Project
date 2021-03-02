package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Criteria {

    private long id ;
    private long pagingId;
    private String userName;
    private String loginId;
    private String userPw;
    private String salt;
    private String userEmail;
    private String userDate;

    public UserDetail userDetail;

}