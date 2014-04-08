/**
 * Copyright (c) 2008-2014 FeiLong, Inc. All Rights Reserved.
 * <p>
 * 	This software is the confidential and proprietary information of FeiLong Network Technology, Inc. ("Confidential Information").  <br>
 * 	You shall not disclose such Confidential Information and shall use it 
 *  only in accordance with the terms of the license agreement you entered into with FeiLong.
 * </p>
 * <p>
 * 	FeiLong MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, 
 * 	INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * 	PURPOSE, OR NON-INFRINGEMENT. <br> 
 * 	FeiLong SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * 	THIS SOFTWARE OR ITS DERIVATIVES.
 * </p>
 */
package com.feilong.taglib.common.date;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.commons.core.date.DateUtil;
import com.feilong.taglib.base.AbstractConditionalTag;

/**
 * 判断一个日期,是否在一个时间区间内.<br>
 * 注:
 * <ul>
 * <li>要么设置 beginDate+endDate</li>
 * <li>要么设置 beginDateString+endDateString+pattern</li>
 * </ul>
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2010-2-9 下午03:37:51
 * @version 1.1 Oct 31, 2012 4:06:19 PM
 */
public class IsInTimeTag extends AbstractConditionalTag{

	private static final long	serialVersionUID	= -7116181842162212131L;

	private static final Logger	log					= LoggerFactory.getLogger(IsInTimeTag.class);

	/** 一个时间,如果为null,则使用当前时间. */
	private Date				date				= null;

	// **********************************************************************************
	/** 开始时间,beginDate和 beginDateString 二者只能选择其一. */
	private Date				beginDate;

	/** 结束时间,endDate和 endDateString 二者只能选择其一. */
	private Date				endDate;

	// **********************************************************************************
	/** 开始时间,beginDate和 beginDateString 二者只能选择其一. */
	private String				beginDateString;

	/** 结束时间,endDate和 endDateString 二者只能选择其一. */
	private String				endDateString;

	/** 如果 使用的 beginDateString 和 endDateString ,其format模式. */
	private String				pattern;

	/*
	 * (non-Javadoc)
	 * @see com.feilong.taglib.base.AbstractConditionalTag#condition()
	 */
	@Override
	public boolean condition(){
		// 不用 直接使用 date ，全局变量 一旦赋值 不会变化
		Date _date = null;
		if (null == date){
			_date = new Date();
		}else{
			_date = date;
		}

		// *********************************************************************
		boolean flag = true;
		if (null != beginDate){

			flag = DateUtil.isInTime(_date, beginDate, endDate);
		}else{
			flag = DateUtil.isInTime(_date, beginDateString, endDateString, pattern);
		}

		// if (log.isDebugEnabled()){
		// Enumeration<String> values = getValues();
		//
		// while (values.hasMoreElements()){
		// String value = values.nextElement();
		// log.debug(value);
		// }
		// // TagData
		// // Object[] args = {};
		// // log.debug("the param _date:{}", args);
		// }
		return flag;
	}

	/**
	 * Sets the 一个时间,如果为null,则使用当前时间.
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date){
		this.date = date;
	}

	/**
	 * Sets the 开始时间,beginDate和 beginDateString 二者只能选择其一.
	 * 
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(Date beginDate){
		this.beginDate = beginDate;
	}

	/**
	 * Sets the 结束时间,endDate和 endDateString 二者只能选择其一.
	 * 
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}

	/**
	 * Sets the 开始时间,beginDate和 beginDateString 二者只能选择其一.
	 * 
	 * @param beginDateString
	 *            the beginDateString to set
	 */
	public void setBeginDateString(String beginDateString){
		this.beginDateString = beginDateString;
	}

	/**
	 * Sets the 结束时间,endDate和 endDateString 二者只能选择其一.
	 * 
	 * @param endDateString
	 *            the endDateString to set
	 */
	public void setEndDateString(String endDateString){
		this.endDateString = endDateString;
	}

	/**
	 * Sets the 如果 使用的 beginDateString 和 endDateString ,其format模式.
	 * 
	 * @param pattern
	 *            the pattern to set
	 */
	public void setPattern(String pattern){
		this.pattern = pattern;
	}

}
