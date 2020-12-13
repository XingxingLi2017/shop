package com.shop.user.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_provinces")
public class Provinces implements Serializable{

	@Id
    @Column(name = "provinceid")
	private String provinceid;//省份ID

    @Column(name = "province")
	private String province;//省份名称



	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}


}
