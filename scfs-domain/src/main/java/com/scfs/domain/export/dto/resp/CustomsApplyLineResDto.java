package com.scfs.domain.export.dto.resp;

import java.math.BigDecimal; 
import com.scfs.common.utils.DecimalUtil; 
import com.scfs.domain.export.entity.CustomsApplyLine;

/**
 * Created by Administrator on 2016年12月6日.
 */
public class CustomsApplyLineResDto extends CustomsApplyLine{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4547183834394025455L;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品编号
     */
    private String goodsNumber;
    /**
     * 商品型号
     */
    private String goodsType;
    
    private String customsNumMax;
	/**单位*/
	private String unit;
    /**
     * 报关含税金额
     */
    @SuppressWarnings("unused")
	private BigDecimal customsAmount;
 

	public String getCustomsNumMax() {
		return customsNumMax;
	}

	public void setCustomsNumMax(String customsNumMax) {
		this.customsNumMax = customsNumMax;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public BigDecimal getCustomsAmount() {
		return DecimalUtil.multiply(null == this.getCustomsNum() ? BigDecimal.ZERO : this.getCustomsNum(), 
				null == this.getCustomsPrice() ? BigDecimal.ZERO : this.getCustomsPrice());
	}
	public void setCustomsAmount(BigDecimal customsAmount) {
		this.customsAmount = customsAmount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
    
    
}

