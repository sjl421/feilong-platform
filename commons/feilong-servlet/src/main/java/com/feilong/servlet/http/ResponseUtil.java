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
package com.feilong.servlet.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.commons.core.enumeration.CharsetType;

/**
 * HttpServletResponse 工具类.
 *
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 2011-11-3 下午02:26:14
 */
public final class ResponseUtil{

	/** The Constant log. */
	private static final Logger	log	= LoggerFactory.getLogger(ResponseUtil.class);

	/**
	 * The Constructor.
	 */
	private ResponseUtil(){}

	/**
	 * 设置不缓存并跳转.
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param url
	 *            跳转路径
	 */
	public static void setNoCacheAndRedirect(HttpServletResponse response,String url){
		setNoCache(response);
		try{
			response.sendRedirect(url);
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * 设置页面不缓存.
	 *
	 * @param response
	 *            HttpServletResponse
	 */
	public static void setNoCache(HttpServletResponse response){
		// 当HTTP1.1服务器指定 CacheControl = no-cache时，浏览器就不会缓存该网页。
		// 旧式 HTTP1.0 服务器不能使用 Cache-Control 标题
		// 为了向后兼容 HTTP1.0 服务器，IE使用Pragma:no-cache 标题对 HTTP提供特殊支持
		// 如果客户端通过安全连接 (https://)/与服务器通讯，且服务器响应中返回 Pragma:no-cache 标题，则 Internet Explorer不会缓存此响应。
		// 注意：Pragma:no-cache 仅当在安全连接中使用时才防止缓存，如果在非安全页中使用，处理方式与 Expires:-1相同，该页将被缓存，但被标记为立即过期
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-control", "no-cache");// Cache-control值为“no-cache”时，访问此页面不会在Internet临时文章夹留下页面备份。
		response.setDateHeader("Expires", 0);
	}

	/**
	 * 以json的方式输出.
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param json
	 *            json字符串
	 */
	public static void ajaxWriteJson(HttpServletResponse response,Object json){
		response.setContentType("application/json;charset=" + CharsetType.UTF8);
		response.setCharacterEncoding(CharsetType.UTF8);
		ajaxWrite(response, json);
	}

	/**
	 * ajax响应.
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param content
	 *            相应内容
	 */
	public static void ajaxWrite(HttpServletResponse response,Object content){
		try{
			PrintWriter printWriter = response.getWriter();
			printWriter.print(content.toString());
			printWriter.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
