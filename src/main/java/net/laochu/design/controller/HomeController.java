package net.laochu.design.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.laochu.design.model.House;
import net.laochu.design.model.Shape;
import net.laochu.design.util.HttpUtils;

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
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
    private HttpUtils httpUtils;
//	private String[] type={"Triangle","Rect","Trapezoid","Circle","Lpolygon"};
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/index.htm", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest  request,Locale locale, ModelMap model) {
		try {
			HttpSession session=request.getSession();
			logger.info(session.getId());
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
	
	
	@RequestMapping(value = "/bedRoom.htm", method = RequestMethod.GET)
	public ModelAndView bedRoom(Locale locale, ModelMap model,Integer bedRoomNum,Integer livingRoomNum,Integer kitchenNum,Integer bathRoomNum,Integer balconyNum) {
		model.put("bedRoomNum", bedRoomNum);
		model.put("livingRoomNum", livingRoomNum);
		model.put("kitchenNum", kitchenNum);
		model.put("bathRoomNum", bathRoomNum);
		model.put("balconyNum", balconyNum);
		return new ModelAndView("bedRoom");
	}
	
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
