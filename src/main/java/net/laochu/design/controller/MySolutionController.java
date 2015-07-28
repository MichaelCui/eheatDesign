package net.laochu.design.controller;

import net.laochu.design.util.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 个人页面的Controller
 */
@Controller
public class MySolutionController {
	private static final Logger logger = LoggerFactory.getLogger(MySolutionController.class);
	
	@Autowired
    private HttpUtils httpUtils;
	/**
	 * 进入个人设计方案页
	 * @param houseid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mySolution.htm", method = RequestMethod.GET)
	public ModelAndView mySolution(String houseid,@CookieValue("phone") String phone,ModelMap model) {
		JSONObject params=new JSONObject();
		params.put("phone", phone);
		String resultStr=httpUtils.sendHttpPost("getUserInfo", params.toJSONString());
		JSONObject jobject=JSON.parseObject(resultStr);
		if(jobject.getBoolean("succ")){
			model.put("UserInfo",jobject.getJSONObject("result") );
		}
		return new ModelAndView("mySolution");
	}
	
	
	@RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
	public ModelAndView modifyUserInfo(String user_name,String company,String title,@CookieValue("phone") String phone,ModelMap model) {
		if(StringUtils.isNotBlank(phone)){
			JSONObject params=new JSONObject();
			params.put("phone", phone);
			JSONObject userInfo=new JSONObject();
			userInfo.put("user_name", user_name);
			userInfo.put("company", company);
			userInfo.put("title", title);
			params.put("user_info", userInfo);
			String resultStr=httpUtils.sendHttpPost("modifyUserInfo", params.toJSONString());
			JSONObject jobject=JSON.parseObject(resultStr);
			if(jobject.getBoolean("succ")){
				model.put("UserInfo",jobject.getJSONObject("result") );
			}
		}
		return new ModelAndView("result");
	}
	
	
	@RequestMapping(value = "/getHouseList")
	public ModelAndView getHouseList(@CookieValue("phone") String phone,ModelMap model) {
		if(StringUtils.isNotBlank(phone)){
			JSONObject params=new JSONObject();
			params.put("phone", phone);
			String resultStr=httpUtils.sendHttpPost("getHouseList", params.toJSONString());
			JSONObject jobject=JSON.parseObject(resultStr);
			if(jobject.getBoolean("succ")){
				model.put("houses",jobject.getJSONArray("result"));
			}
		}
		return new ModelAndView("mySolutionList");
	}
	
	
}
