package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends Criteria {

    private Integer id ;
    private String userName;
    private String loginId;
    private String userPw;
    private String userEmail;
    private String userDate;

    private long userId;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String phoneNumber;
    private int receiveSms;

}

