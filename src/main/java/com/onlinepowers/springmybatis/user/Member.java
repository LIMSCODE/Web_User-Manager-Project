package com.onlinepowers.springmybatis.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_MEMBER")
public class Member{	//다대일 양방향 매핑

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	public void setTeam(Team team) {
		this.team = team;

		if (!team.getMembers().contains(this)) {
			team.getMembers().add(this);
		}
	}

}