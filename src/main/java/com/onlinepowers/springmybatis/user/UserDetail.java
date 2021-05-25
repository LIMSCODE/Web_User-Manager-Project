
package com.onlinepowers.springmybatis.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_USER_DETAIL")
@OnDelete(action = OnDeleteAction.CASCADE)
@EqualsAndHashCode(exclude="user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long userId;

    @OneToOne
    @JoinColumn(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private User user;

    @NotEmpty(message="우편번호 입력해주세요")// null, 빈 문자열(스페이스 포함X) 불가
    @Size (max= 30, message="30글자 이하로 입력하세요")
    public String zipcode;

    @NotEmpty(message="주소 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    public String address;

    @NotEmpty(message="상세주소 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    public String addressDetail;

    @NotEmpty(message="전화번호 입력해주세요")
    @Size (max= 30, message="30글자 이하로 입력하세요")
    public String phoneNumber;


    public String receiveSms;

    public String getReceiveSmsTitle() {
        if (this.receiveSms == null) {
            return "";

        } else {
            return "1".equals(this.receiveSms) ? "수신" : "수신안함" ;
        }
    }


}
