package com.onlinepowers.springmybatis.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OP_CATEGORY")
public class Category {     //다대다 - 매핑테이블 있음. (Category-item)

	@Id
	@GeneratedValue
	private Long categoryId;

	private String name;

	@ManyToMany
	@JoinTable(name = "OP_CATEGORY_ITEM",
			joinColumns = @JoinColumn(name = "CATEGORY_ID"),
			inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
	private List<Item> items = new ArrayList<Item>();

	public void addItem(Item item) {
		items.add(item);
	}

}
