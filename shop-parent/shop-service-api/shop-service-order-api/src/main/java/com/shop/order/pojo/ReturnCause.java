package com.shop.order.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="tb_return_cause")
public class ReturnCause implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Integer id;//ID

    @Column(name = "cause")
	private String cause;//原因

    @Column(name = "seq")
	private Integer seq;//排序

    @Column(name = "status")
	private String status;//是否启用



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
