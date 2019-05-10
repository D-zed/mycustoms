package com.jinaup.upcustoms.util;

import java.util.Date;
import java.util.UUID;

public class WorkCatUtil {
			/**
			 * 正式环境：https://openapi.gongmall.com
			 * 测试环境：https://openapi-qa.gongmall.com
			 */
			public static final String domain = "https://openapi.gongmall.com";
			
			/**
			 * 正式环境 https://contract.gongmall.com/url_contract.html?companyId=XPWOeP&positionId=bzlgWM
			 * 测试环境 https://contract-qa.gongmall.com/url_contract.html?companyId=lzDZmP&positionId=ePq32z
			 */
			public static final String electricSign = "https://contract.gongmall.com/url_contract.html?companyId=XPWOeP&positionId=bzlgWM";
			
			
			/**
			 * 员工电签状态查询接口
			 */
			public static final String GetContractStatus = domain + "/api/employee/getContractStatus";
			
			/**
			 * 查询企业当前余额
			 */
			public static final String GetBalance = domain +"/api/company/getBalance";
			
			
			/**
			 * 修改员工银行卡
			 */
			public static final String SyncBankAccount = domain + "/api/employee/syncBankAccount";
			
			/**
			 * 算税信息
			 */
			public static final String GetTaxInfo = domain + "/api/withdraw/getTaxInfo";
			
			/**
			 * 提现
			 */
			public static final String DoWithdraw =  domain + "/api/withdraw/doWithdraw";
			
			/**
			 * 客户按月查看自己提现历史记录。
			 */
			public static final String GetWithdrawList = domain + "/api/withdraw/getWithdrawList";
			
			/**
			 * 查询单笔提现结果
			 */
			public static final String GetWithdrawResult = domain + "/api/withdraw/getWithdrawResult";
			
			/**
			 * 
			 * @Title: getNonce   
			 * @Description: TODO(随机数，请每次随机产生，保证随机数不可预测，不大于32位)   
			 * @param: @return      
			 * @return: String      
			 * @throws
			 */
			public static String getNonce() {
		        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");  
		        return uuid;
			}
			
			public static void main(String[] args) {
				System.out.println(WorkCatUtil.getNonce());
				System.out.println(new Date().getTime());
			}
}
