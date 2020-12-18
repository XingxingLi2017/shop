package com.shop.order.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="tb_order")
public class Order implements Serializable{

	@Id
    @Column(name = "id")
	private String id;//订单id

    @Column(name = "total_num")
	private Integer totalNum;//数量合计

    @Column(name = "total_money")
	private Integer totalMoney;//金额合计

    @Column(name = "pre_money")
	private Integer preMoney;//优惠金额

    @Column(name = "post_fee")
	private Integer postFee;//邮费

    @Column(name = "pay_money")
	private Integer payMoney;//实付金额

    @Column(name = "pay_type")
	private String payType;//支付类型，1、在线支付、0 货到付款

    @Column(name = "create_time")
	private Date createTime;//订单创建时间

    @Column(name = "update_time")
	private Date updateTime;//订单更新时间

    @Column(name = "pay_time")
	private Date payTime;//付款时间

    @Column(name = "consign_time")
	private Date consignTime;//发货时间

    @Column(name = "end_time")
	private Date endTime;//交易完成时间

    @Column(name = "close_time")
	private Date closeTime;//交易关闭时间

    @Column(name = "shipping_name")
	private String shippingName;//物流名称

    @Column(name = "shipping_code")
	private String shippingCode;//物流单号

    @Column(name = "username")
	private String username;//用户名称

    @Column(name = "buyer_message")
	private String buyerMessage;//买家留言

    @Column(name = "buyer_rate")
	private String buyerRate;//是否评价

    @Column(name = "receiver_contact")
	private String receiverContact;//收货人

    @Column(name = "receiver_mobile")
	private String receiverMobile;//收货人手机

    @Column(name = "receiver_address")
	private String receiverAddress;//收货人地址

    @Column(name = "source_type")
	private String sourceType;//订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面

    @Column(name = "transaction_id")
	private String transactionId;//交易流水号

    @Column(name = "order_status")
	private String orderStatus;//订单状态,0:未完成,1:已完成，2：已退货

    @Column(name = "pay_status")
	private String payStatus;//支付状态,0:未支付，1：已支付，2：支付失败

    @Column(name = "consign_status")
	private String consignStatus;//发货状态,0:未发货，1：已发货，2：已收货

    @Column(name = "is_delete")
	private String isDelete;//是否删除



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Integer totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Integer getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(Integer preMoney) {
		this.preMoney = preMoney;
	}
	public Integer getPostFee() {
		return postFee;
	}

	public void setPostFee(Integer postFee) {
		this.postFee = postFee;
	}
	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getConsignTime() {
		return consignTime;
	}

	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	public String getBuyerRate() {
		return buyerRate;
	}

	public void setBuyerRate(String buyerRate) {
		this.buyerRate = buyerRate;
	}
	public String getReceiverContact() {
		return receiverContact;
	}

	public void setReceiverContact(String receiverContact) {
		this.receiverContact = receiverContact;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getConsignStatus() {
		return consignStatus;
	}

	public void setConsignStatus(String consignStatus) {
		this.consignStatus = consignStatus;
	}
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}


}
