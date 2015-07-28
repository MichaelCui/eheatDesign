package net.laochu.design.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.laochu.design.model.House;
import net.laochu.design.model.Shape;
import net.laochu.design.util.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 设计页使用的Controller
 */
@Controller
public class DesignController {
	
	@Autowired
    private HttpUtils httpUtils;
//	private String[] type={"Triangle","Rect","Trapezoid","Circle","Lpolygon"};
	
	private static final Logger logger = LoggerFactory.getLogger(DesignController.class);
	
	/**
	 * 访问设计页
	 * @param request
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index.htm", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest  request,Locale locale, ModelMap model) {
		try {
			HttpSession session=request.getSession();
			String responseString=httpUtils.sendHttpPost("getHouseID",session.getId());
			JSONObject jobject=JSON.parseObject(responseString);
			Boolean success=(Boolean) jobject.get("succ");
			String house_id="";
			if(success){
				house_id=(String) jobject.get("result");
			}
			model.put("house_id", house_id);
			return new ModelAndView("design");
		} catch (Exception e) {
			logger.error("getHouseID error", e);
		}
		return new ModelAndView("design");
	}
	
	
	/**
	 * 获得登录状态
	 * @param request
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public ModelAndView getLoginStatus(HttpServletRequest  request,Locale locale, ModelMap model) {
		String phone=httpUtils.getCookieValueByName(request,"phone");
		JSONObject result=new JSONObject();
		if(StringUtils.isNotBlank(phone)){
			result.put("succ", true);
		}else{
			result.put("succ", false);
		}
		model.put("reval", result.toJSONString());
		return new ModelAndView("result"); 
	}
	/**
	 * 获得户型设置内容，返回值会直接加载到页面上
	 * @param model
	 * @param bedRoomNum
	 * @param livingRoomNum
	 * @param kitchenNum
	 * @param bathRoomNum
	 * @param balconyNum
	 * @return
	 */
	@RequestMapping(value = "/bedRoom.htm", method = RequestMethod.GET)
	public ModelAndView bedRoom(ModelMap model,Integer bedRoomNum,Integer livingRoomNum,Integer kitchenNum,Integer bathRoomNum,Integer balconyNum) {
		model.put("bedRoomNum", bedRoomNum);
		model.put("livingRoomNum", livingRoomNum);
		model.put("kitchenNum", kitchenNum);
		model.put("bathRoomNum", bathRoomNum);
		model.put("balconyNum", balconyNum);
		return new ModelAndView("rooms");
	}
	
	/**
	 * 通过ajax请求添加或修改一个房间
	 * @param house_id
	 * @param room_id
	 * @param shape_name
	 * @param room_type
	 * @param shape_type
	 * @param sides
	 * @param contor
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addRoom.htm", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String addRoom(String house_id,Integer room_id,String shape_name,Integer room_type,Integer shape_type,Double[] sides,Double[] contor,ModelMap model) {
		try {
			House house=new House();
			Shape s=new Shape();
			s.controller=contor;
			s.shape_name=shape_name;
			s.shape_type=shape_type;
			s.room_type=room_type;
			s.sides=sides;
			house.house_id=house_id;
			house.shape=s;
			String  resultStr="";
			if(null!=room_id){
				house.room_id=room_id;
				resultStr=httpUtils.sendHttpPost("modifyRoom",JSON.toJSONString(house));
			}else{
				resultStr=httpUtils.sendHttpPost("addRoom",JSON.toJSONString(house));
			}
			return resultStr;
		} catch (Exception e) {
			logger.error("addRoom error",e);
		}
		JSONObject re=new JSONObject();
		re.put("succ", false);
		re.put("msg", "服务器通信异常请稍后重试！");
		return re.toJSONString();
	}
	/**
	 * 删除房间
	 * @param house_id
	 * @param room_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/removeRoom.htm", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String roomRoom(String house_id,Integer room_id,ModelMap model) {
		Map<Object,Object> m=new HashMap<Object,Object>();
		m.put("house_id", house_id);
		m.put("room_id",room_id);
		String resultStr=httpUtils.sendHttpPost("removeRoom",JSON.toJSONString(m));
		return resultStr;
	}
	
}
