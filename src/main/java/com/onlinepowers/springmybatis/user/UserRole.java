
package com.onlinepowers.springmybatis.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_USER_ROLE")
@OnDelete(action = OnDeleteAction.CASCADE)
@EqualsAndHashCode(exclude="user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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