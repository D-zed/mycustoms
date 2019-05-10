package com.jinaup.upcustoms.util;
/**
 * 
 * @ClassName:  CommissionFormulaUtils   
 * @Description:TODO(计算佣金公式)   
 * @author: youle.heng
 * @date:   2019年1月24日 下午5:03:56   
 *     
 * @Copyright: 2019 www.jinaup.com Inc. All rights reserved.
 */

import java.math.BigDecimal;

/**
 * 
 * @ClassName:  CommissionFormulaUtils   
 * @Description:TODO(销售佣金和管理佣金计算)   
 * @author: youle.heng
 * @date:   2019年2月19日 下午7:13:09   
 *     
 * @Copyright: 2019 www.jinaup.com Inc. All rights reserved.
 */
public class CommissionFormulaUtils {

	/**
	 * 
	 * @Title: oneSalesCommission
	 * @Description: TODO(一级销售佣金 = 会员折扣价)   会员折扣价 = (销售-成本-优惠券)*0.85/1.71  前端页面显示适用，实际计算看 OrderEstimatedCommissionMsgProcess；
	 *  销售佣金 只提两级 邮费不计入销售价格中参与分佣计算。 会员价 = 售价 - 会员折扣
	 * @param salesPrice 销售价
	 * @param supplyPrice 成本价
	 * @param commissionAmount 手动设置的分佣金额
	 * @param settlementType 商品 结算方式 0售价 1.成本价 2.扣点
	 * @param points 扣点数
	 * @param discount 单个商品的优惠金额  查询用户使用的优惠券信息 （如果没使用优惠券或者使用了会员优惠券和现金券 则 优惠价格 discount 为0）使用了一般优惠券则 传入的为单个商品分配到的优惠券金额
	 * @return      
	 * @return: BigDecimal      
	 * @throws
	 */
	public static BigDecimal oneSalesCommission(BigDecimal salesPrice,BigDecimal supplyPrice,BigDecimal commissionAmount,Integer settlementType,Integer points,BigDecimal discount) {
			if(salesPrice.compareTo(BigDecimal.ZERO) == 0) {
				return BigDecimal.ZERO;
			}
			
			/**
			 * 商品毛利
			 */
			BigDecimal grossProfit = BigDecimal.ZERO;
			
			/**
			 * 商品按售价结算不处理毛利
			 */
			if(settlementType == 0) {
				return BigDecimal.ZERO;
			}
			/**
			 * 商品按成本价 计算毛利
			 */
			if(settlementType == 1) {
				/**
				 * 售价减成本价 计算毛利
				 */
				grossProfit = (salesPrice.subtract(supplyPrice)).subtract(discount);
				
				if(grossProfit.compareTo(BigDecimal.ZERO) <= 0) {
					grossProfit =  commissionAmount;
				}else {
					grossProfit = grossProfit.add(commissionAmount);
				}
				
			}
			/**
			 * 商品按扣点方式计算毛利
			 */
			if(settlementType == 2) {
				BigDecimal bigDecimalPoints = new BigDecimal(points);
				bigDecimalPoints = bigDecimalPoints.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
				grossProfit = salesPrice.multiply(bigDecimalPoints);
				grossProfit = grossProfit.subtract(discount);
				
				if(grossProfit.compareTo(BigDecimal.ZERO)<=0) {
					grossProfit =  commissionAmount;
				}else {
					grossProfit = grossProfit.add(commissionAmount);
				}
			}
			
			if(grossProfit.compareTo(BigDecimal.ZERO)<=0) {
				return BigDecimal.ZERO;
			}
			return (grossProfit.multiply(new BigDecimal("0.85"))).divide(new BigDecimal("1.71"),2,BigDecimal.ROUND_DOWN);
	}
	
	
	
	/**
	 * 
	 * @Title: secondSalesCommission   
	 * @Description: TODO(二级销售佣金)   一级销售佣金/会员折扣 * 0.25
	 * @param oneSalesCommission
	 * @return      
	 * @return: BigDecimal      
	 * @throws
	 */
	public static BigDecimal secondSalesCommission(BigDecimal oneSalesCommission) {
		return oneSalesCommission.multiply(new BigDecimal("0.25")).setScale(2,BigDecimal.ROUND_DOWN);
	}

	
	public static void main(String[] args) {
		BigDecimal salesPrice = new BigDecimal("33");
		BigDecimal supplyPrice = new BigDecimal("23");
		BigDecimal discount = new BigDecimal("7.96");
		
		System.out.println(CommissionFormulaUtils.oneSalesCommission(salesPrice, supplyPrice, BigDecimal.ZERO, 1, 0, discount));
//		System.out.println(CommissionFormulaUtils.oneSalesCommission(salesPrice, supplyPrice, discount));
//		System.out.println(CommissionFormulaUtils.oneSalesCommission(salesPrice, supplyPrice, discount).multiply(new BigDecimal("0.25")).setScale(2,BigDecimal.ROUND_DOWN));
//		
//		System.out.println((CommissionFormulaUtils.oneSalesCommission(salesPrice, supplyPrice, discount).add(CommissionFormulaUtils.oneSalesCommission(salesPrice, supplyPrice, discount).multiply(new BigDecimal("0.25")).setScale(2,BigDecimal.ROUND_DOWN))).multiply(new BigDecimal("0.20")).setScale(2,BigDecimal.ROUND_DOWN));
	}
}
