package com.shop.order.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="tb_preferential")
public class Preferential implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID

    @Column(name = "buy_money")
	private Integer buyMoney;//消费金额

    @Column(name = "pre_money")
	private Integer preMoney;//优惠金额

    @Column(name = "category_id")
	private Long categoryId;//品类ID

    @Column(name = "start_time")
	private Date startTime;//活动开始日期

    @Column(name = "end_time")
	private Date endTime;//活动截至日期

    @Column(name = "state")
	private String state;//状态

    @Column(name = "type")
	private String type;//类型1不翻倍 2翻倍



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(Integer buyMoney) {
		this.buyMoney = buyMoney;
	}
	public Integer getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(Integer preMoney) {
		this.preMoney = preMoney;
	}
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
