package com.shop.user.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="tb_address")
public class Address implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//

    @Column(name = "username")
	private String username;//用户名

    @Column(name = "provinceid")
	private String provinceid;//省

    @Column(name = "cityid")
	private String cityid;//市

    @Column(name = "areaid")
	private String areaid;//县/区

    @Column(name = "phone")
	private String phone;//电话

    @Column(name = "address")
	private String address;//详细地址

    @Column(name = "contact")
	private String contact;//联系人

    @Column(name = "is_default")
	private String isDefault;//是否是默认 1默认 0否

    @Column(name = "alias")
	private String alias;//别名



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}


}
