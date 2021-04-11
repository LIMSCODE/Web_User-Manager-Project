
package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_USER_ROLE")
@OnDelete(action = OnDeleteAction.CASCADE)
public class UserRole extends Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    public Long userId;

    @Column(name = "AUTHORITY")
    public String authority;

    public String getAuthorityTitle() {
        if (this.authority == null) {
            return "";

        } else {
            return "ROLE_OPMANAGER".equals(this.authority) ? "관리자" : "회원" ;
        }
    }
//
//    @PrePersist
//    public void prePersist() {
//        this.authority = this.authority == null ? "ROLE_USER" : this.authority;
//    }

}