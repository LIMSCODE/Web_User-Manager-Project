
package com.onlinepowers.springmybatis.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.onlinepowers.springmybatis.paging.JpaPaging;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_USER_ROLE")
@OnDelete(action = OnDeleteAction.CASCADE)
@EqualsAndHashCode(exclude="user")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long userId;

    @OneToOne
    @JoinColumn(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private User user;

    public String authority;

    public String getAuthorityTitle() {
        if (this.authority == null) {
            return "";

        } else {
            return "ROLE_OPMANAGER".equals(this.authority) ? "관리자" : "회원" ;
        }
    }

}