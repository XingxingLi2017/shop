package com.shop.content.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="tb_content_category")
public class ContentCategory implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;//类目ID

    @Column(name = "name")
	private String name;//分类名称



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
