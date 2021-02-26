package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail extends Criteria {

    public long userId;
    public String zipcode;
    public String address;
    public String addressDetail;
    public String phoneNumber;
    public String receiveSms;


}
