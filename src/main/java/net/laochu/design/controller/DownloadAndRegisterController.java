package net.laochu.design.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.laochu.design.model.SolutionInfo;
import net.laochu.design.util.HttpUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DownloadAndRegisterController {
	
	private static final Logger logger = LoggerFactory.getLogger(DownloadAndRegisterController.class);
	
	@Value("#{configProperties['picUrl']}")  
	private String picUrl;  
	@Value("#{configProperties['downUrl']}")  
	private String downUrl;  
	@Autowired
    private HttpUtils httpUtils;
	
	@RequestMapping(value = "/download.htm", method = RequestMethod.GET)
	public ModelAndView home(String house_id, ModelMap model ,HttpServletRequest request) {
		JSONObject params=new JSONObject();
		params.put("house_id", house_id);
		String resultString=httpUtils.sendHttpPost("getHousePic", params.toJSONString());
		JSONObject resultObject=JSON.parseObject(resultString);
		Boolean success=(Boolean) resultObject.get("succ");
		if(success){
			String picName=resultObject.getString("result");
			model.put("HousePic", picUrl+picName);
			String phone=httpUtils.getCookieValueByName(request,"phone");
			model.put("phone", phone);
			if(StringUtils.isNotBlank(phone)){
				JSONObject getUserInfoparams=new JSONObject();
				getUserInfoparams.put("phone", phone);
				String resultStr=httpUtils.sendHttpPost("getUserInfo", getUserInfoparams.toJSONString());
				JSONObject jobject=JSON.parseObject(resultStr);
				if(jobject.getBoolean("succ")){
					model.put("UserInfo",jobject.getJSONObject("result") );
				}
			}
		}else{
			return new ModelAndView("redirect:index.htm");
		}
		model.put("house_id",house_id);
		return new ModelAndView("down");
	}
	
	@RequestMapping(value = "/smsCode.htm", method = RequestMethod.POST)
	public ModelAndView smsCode(String phone,ModelMap model,HttpSession session,String code){
		if(!code.equalsIgnoreCase(session.getAttribute("code").toString())){
			JSONObject jobject=new JSONObject();
			jobject.put("succ", false);
			jobject.put("msg", "验证码错误");
			model.put("reval", jobject.toString());
			return new ModelAndView("result");
		}
		JSONObject params=new JSONObject();
		params.put("phone", phone);
		String resultString=httpUtils.sendHttpPost("sendSmsCode", params.toJSONString());
		model.put("reval", resultString);
		return new ModelAndView("result");//ajax请求，不用@ResponseBody的情况下，使用的返回方式，model里需要put key为reval的Json字符串
	}
	
	@RequestMapping(value = "/validSmsCode.htm", method = RequestMethod.POST)
	public ModelAndView validSmsCode(String house_id,String phone,String code,ModelMap model){
		JSONObject params=new JSONObject();
		params.put("house_id", house_id);
		params.put("phone", phone);
		params.put("code", code);
		String resultString=httpUtils.sendHttpPost("validSmsCode", params.toJSONString());
		model.put("reval", resultString);
		return new ModelAndView("result");
	}
	
	
	@RequestMapping(value = "/getSolutionPDF.htm", method = RequestMethod.POST)
	public ModelAndView getSolutionPDF(@CookieValue("phone") String phone,String house_id,String solution_name,String company,String author,ModelMap model){
		JSONObject params=new JSONObject();
		if(StringUtils.isNotBlank(phone)){
			params.put("house_id", house_id);
			params.put("phone", phone);
			SolutionInfo sinfo=new SolutionInfo();
			sinfo.setAuthor(author);
			sinfo.setCompany(company);
			sinfo.setSolution_name(solution_name);
			params.put("solution_info", sinfo);
			String resultString=httpUtils.sendHttpPost("getSolutionPDF", params.toJSONString());
			model.put("reval", resultString);
			return new ModelAndView("result");
		}
		params.put("succ", false);
		params.put("msg", "数据异常");
		model.put("reval", params.toJSONString());
		return new ModelAndView("result");
	}
	
	
	
  @RequestMapping("/downpdf.htm")  
    public ResponseEntity<byte[]> download(String filename,@CookieValue("phone") String phone) {  
        try {
			String path=downUrl+filename;
			File file=new File(path);
			HttpHeaders headers = new HttpHeaders();  
			String name="户型"+System.currentTimeMillis()+".pdf";
			String fileName=new String(name.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
			headers.setContentDispositionFormData("attachment", fileName); 
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); 
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException",e);
		} catch (IOException e) {
			logger.error("IO error",e);
		}  
        return null;
    }  

}
