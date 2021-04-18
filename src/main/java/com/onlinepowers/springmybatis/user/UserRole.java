
package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class UserRole extends JpaPaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long userId;

    @OneToOne
    @JoinColumn(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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