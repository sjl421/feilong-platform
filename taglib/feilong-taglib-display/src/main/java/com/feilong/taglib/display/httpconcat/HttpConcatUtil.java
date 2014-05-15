/*
 * Copyright (C) 2008 feilong (venusdrogon@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.taglib.display.httpconcat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.commons.core.configure.ResourceBundleUtil;
import com.feilong.commons.core.entity.JoinStringEntity;
import com.feilong.commons.core.log.Slf4jUtil;
import com.feilong.commons.core.text.MessageFormatUtil;
import com.feilong.commons.core.util.CollectionUtil;
import com.feilong.commons.core.util.Validator;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.tools.json.JsonUtil;

/**
 * The Class HttpConcatUtil.
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2014年5月5日 上午11:04:37
 */
public final class HttpConcatUtil{

	/** The Constant log. */
	private static final Logger		log	= LoggerFactory.getLogger(HttpConcatUtil.class);

	/** The Constant TEMPLATE_CSS. */
	private static final String		TEMPLATE_CSS;

	/** The Constant TEMPLATE_JS. */
	private static final String		TEMPLATE_JS;

	/** 是否支持 HTTP_CONCAT. */
	private static final Boolean	HTTP_CONCAT_SUPPORT;

	// XXX 支持多变量
	static{
		HTTP_CONCAT_SUPPORT = ResourceBundleUtil.getValue(
				HttpConcatConstants.CONFIG_FILE,
				HttpConcatConstants.KEY_HTTPCONCAT_SUPPORT,
				Boolean.class);

		if (Validator.isNullOrEmpty(HTTP_CONCAT_SUPPORT)){
			log.warn(
					"can not find key:[{}],pls ensure you have put the correct configuration file path:[{}]",
					HttpConcatConstants.KEY_HTTPCONCAT_SUPPORT,
					HttpConcatConstants.CONFIG_FILE);
		}
	}

	// 加载模板
	static{
		TEMPLATE_CSS = ResourceBundleUtil.getValue(HttpConcatConstants.CONFIG_FILE, HttpConcatConstants.KEY_TEMPLATE_CSS);
		TEMPLATE_JS = ResourceBundleUtil.getValue(HttpConcatConstants.CONFIG_FILE, HttpConcatConstants.KEY_TEMPLATE_JS);
		if (Validator.isNullOrEmpty(TEMPLATE_CSS)){
			String messagePattern = "can not find key:[{}],pls ensure you have put the correct configuration file path:[{}]";
			throw new IllegalArgumentException(Slf4jUtil.formatMessage(
					messagePattern,
					HttpConcatConstants.KEY_HTTPCONCAT_SUPPORT,
					HttpConcatConstants.CONFIG_FILE));

		}
		if (Validator.isNullOrEmpty(TEMPLATE_JS)){
			String messagePattern = "can not find key:[{}],pls ensure you have put the correct configuration file path:[{}]";
			throw new IllegalArgumentException(Slf4jUtil.formatMessage(
					messagePattern,
					HttpConcatConstants.KEY_HTTPCONCAT_SUPPORT,
					HttpConcatConstants.CONFIG_FILE));
		}
	}

	// *****************************************************************************

	/**
	 * Gets the write content.
	 * 
	 * @param httpConcatParam
	 *            the http concat param
	 * @return 如果支持 concat,那么生成concat字符串;如果不支持,那么生成多行js/css字符串
	 */
	public static String getWriteContent(HttpConcatParam httpConcatParam){

		if (log.isDebugEnabled()){
			log.debug(JsonUtil.format(httpConcatParam));
		}

		String type = httpConcatParam.getType();
		String template = getTemplate(type);

		String returnValue = "";
		if (HTTP_CONCAT_SUPPORT){
			// concat
			returnValue = MessageFormatUtil.format(template, getConcatLink(httpConcatParam));
		}else{
			// 本地开发环境支持的.
			StringBuffer sb = new StringBuffer();
			List<String> itemSrcList = httpConcatParam.getItemSrcList();
			for (String itemSrc : itemSrcList){
				if (Validator.isNotNullOrEmpty(itemSrc)){
					sb.append(MessageFormatUtil.format(template, getNoConcatLink(itemSrc, httpConcatParam)));
				}else{
					log.warn("itemSrc isNullOrEmpty");
				}
			}
			returnValue = sb.toString();
		}
		if (log.isDebugEnabled()){
			log.debug("returnValue:[{}]", returnValue);
		}
		return returnValue;
	}

	// *****************************************************************************
	/**
	 * 获得合并的链接.
	 * 
	 * @param httpConcatParam
	 *            the http concat param
	 * @return the link
	 */
	private static String getConcatLink(HttpConcatParam httpConcatParam){
		List<String> itemSrcList = httpConcatParam.getItemSrcList();
		String domain = httpConcatParam.getDomain();
		String root = httpConcatParam.getRoot();
		String version = httpConcatParam.getVersion();

		// **********************************************************************************

		StringBuffer sb = new StringBuffer();
		sb.append(domain);
		if (Validator.isNotNullOrEmpty(root)){
			sb.append(root);
		}
		int size = itemSrcList.size();
		// 只有一条
		if (size == 1){
			sb.append("/");
			sb.append(itemSrcList.get(0));
		}else{
			sb.append("/??");

			JoinStringEntity joinStringEntity = new JoinStringEntity(JoinStringEntity.DEFAULT_CONNECTOR);
			sb.append(CollectionUtil.toString(itemSrcList, joinStringEntity));
		}
		sb.append("?");
		sb.append(version);

		return sb.toString();
	}

	/**
	 * 获得不需要 Concat 的连接
	 * 
	 * @param itemSrc
	 *            the src
	 * @param httpConcatParam
	 *            the http concat param
	 * @return the string
	 */
	private static String getNoConcatLink(String itemSrc,HttpConcatParam httpConcatParam){

		String domain = httpConcatParam.getDomain();
		String root = httpConcatParam.getRoot();
		String version = httpConcatParam.getVersion();

		StringBuffer sb = new StringBuffer();
		sb.append(domain);
		if (Validator.isNotNullOrEmpty(root)){
			sb.append(root);
		}
		sb.append("/");
		sb.append(itemSrc);
		sb.append("?");
		sb.append(version);
		return sb.toString();
	}

	// *****************************************************************************

	/**
	 * 不同的type不同的模板.
	 * 
	 * @param type
	 *            类型 {@link HttpConcatConstants#TYPE_CSS} 以及{@link HttpConcatConstants#TYPE_JS}
	 * @return 目前仅支持 {@link HttpConcatConstants#TYPE_CSS} 以及{@link HttpConcatConstants#TYPE_JS},其余不支持,会抛出
	 *         {@link UnsupportedOperationException}
	 */
	private static String getTemplate(String type){
		if (HttpConcatConstants.TYPE_CSS.equalsIgnoreCase(type)){
			return TEMPLATE_CSS;
		}else if (HttpConcatConstants.TYPE_JS.equalsIgnoreCase(type)){
			return TEMPLATE_JS;
		}
		throw new UnsupportedOperationException("type:[" + type + "] not support!");
	}
}
