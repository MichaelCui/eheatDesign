package net.laochu.design.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.laochu.design.util.HttpUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;


/**
 * 个人页面的Controller
 */
@Controller
public class UserOperationController {
	private static final Logger logger = LoggerFactory.getLogger(UserOperationController.class);
	
	@Autowired
    private HttpUtils httpUtils;
	/**
	 * 进入个人设计方案页
	 * @param houseid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userOperation.htm", method = RequestMethod.POST)
	public ModelAndView userLogin(String phone,String code,String pwd,Integer cmd,ModelMap model,HttpSession session,HttpServletResponse response) {
		JSONObject jobject=new JSONObject();
		jobject.put("phone", phone);
		jobject.put("pwd", DigestUtils.md5Hex(pwd+"&*^"));
		jobject.put("cmd", cmd);
		jobject.put("code", code);
		String resultStr=httpUtils.sendHttpPost("validSmsCode", jobject.toJSONString());
		JSONObject result=JSONObject.parseObject(resultStr);
		if(result.getBoolean("succ")){
			response.setHeader("Set-Cookie", "phone="+phone+";Path=/design/;HttpOnly;");
		}
		model.put("reval", resultStr);
		return new ModelAndView("result");
	}
	
	
	@RequestMapping(value = "/userReg.htm", method = RequestMethod.POST)
	public ModelAndView userReg(String phone,String code,ModelMap model,HttpServletResponse response) {
		JSONObject jobject=new JSONObject();
		jobject.put("phone", phone);
		jobject.put("cmd", 0);//注册默认使用操作命令为0
		jobject.put("code", code);
		String resultStr=httpUtils.sendHttpPost("validSmsCode", jobject.toJSONString());
		JSONObject result=JSONObject.parseObject(resultStr);
		Boolean b=result.getBoolean("succ");
		if(b){
			response.setHeader("Set-Cookie", "phone="+phone+";Path=/;HttpOnly;");
		}
		model.put("reval", resultStr);
		return new ModelAndView("result");
	}
}
