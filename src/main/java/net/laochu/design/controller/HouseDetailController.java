package net.laochu.design.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.laochu.design.model.HouseDetailRoom;
import net.laochu.design.model.WireBand;
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
 * 房间详情Controller
 */
@Controller
public class HouseDetailController {
	private static final Logger logger = LoggerFactory.getLogger(HouseDetailController.class);
	@Autowired
    private HttpUtils httpUtils;
	
	/**
	 * 获取房间详情数据
	 * @param house_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getHouseDetail.htm", method = RequestMethod.GET)
	public ModelAndView assemble(String house_id,ModelMap model) {
		Map<String,String> params=new HashMap<String,String>();
		params.put("house_id", house_id);
		String houseDetailStr=httpUtils.sendHttpPost("getHouseDetail",JSON.toJSONString(params));
		String wireBands=httpUtils.sendHttpPost("getWireBands",JSON.toJSONString(params));
		JSONObject resuObject=JSON.parseObject(houseDetailStr);
		Boolean houseDetailSuccess=(boolean)resuObject.get("succ");
		if(houseDetailSuccess){
			JSONObject jobject=resuObject.getJSONObject("result");
			List<HouseDetailRoom> list=JSON.parseArray(jobject.get("rooms").toString(), HouseDetailRoom.class);
			model.put("rooms", list);
			model.put("wire_band", jobject.get("wire_band"));
			model.put("total_area", jobject.get("total_area"));
			model.put("total_power", jobject.get("total_power"));
			model.put("total_price", jobject.get("total_price"));
			
		}
		JSONObject wireBoject=JSON.parseObject(wireBands);
		Boolean wireSuccess=(boolean)wireBoject.get("succ");
		if(wireSuccess){
			List<WireBand> list=JSON.parseArray(wireBoject.get("result").toString(), WireBand.class);
			model.put("wireBands", list);
		}
		model.put("house_id",house_id);
		return new ModelAndView("houseDetail");
	}
	
	
	/**
	 * 选择线缆品牌
	 * @param houseid
	 * @param band_id
	 * @return
	 */
	@RequestMapping(value = "/chooseWireBand.htm", method = RequestMethod.POST)
	@ResponseBody
	public String chooseWireBand(String houseid,String band_id){
		JSONObject reVal = new JSONObject();
		try {
			JSONObject jobject = new JSONObject();
			jobject.put("house_id", houseid);
			jobject.put("band_id", band_id);
			String result = httpUtils.sendHttpPost("chooseWireBand",
					jobject.toJSONString());
			JSONObject resuObject = JSON.parseObject(result);
			Boolean success = (boolean) resuObject.get("succ");
			if (success) {
				Double piceStringt = resuObject.getDouble("result");
				reVal.put("success", true);
				reVal.put("reVal", piceStringt);
			} else {
				String msg = resuObject.getString("msg");
				reVal.put("success", false);
				reVal.put("msg", msg);
			}
			return reVal.toJSONString();
		} catch (Exception e) {
			logger.error("chooseWireBand error",e);
		}
		reVal.put("success", false);
		reVal.put("msg", "服务器通信异常请稍后重试！");
		return reVal.toJSONString();
	}
	
	/**
	 * 修改单位面积功率
	 * @param houseid
	 * @param room_id
	 * @param meter_power
	 * @return
	 */
	@RequestMapping(value = "/modifyPowerPSM.htm", method = RequestMethod.POST)
	@ResponseBody
	public String modifyPowerPSM(String houseid,Integer room_id,Integer meter_power){
		JSONObject reVal=new JSONObject();
		try {
			JSONObject jobject=new JSONObject();
			jobject.put("house_id", houseid);
			jobject.put("room_id", room_id);
			jobject.put("meter_power", meter_power);
			String result=httpUtils.sendHttpPost("modifyPowerPSM",jobject.toJSONString());
			JSONObject resuObject=JSON.parseObject(result);
			Boolean success=(boolean)resuObject.get("succ");
			if(success){
				JSONObject resultObject=resuObject.getJSONObject("result");
				reVal.put("success", true);
				reVal.put("reVal",resultObject);
			}else{
				String msg=resuObject.getString("msg");
				reVal.put("success", false);
				reVal.put("msg",msg);
			}
			return reVal.toJSONString();
		} catch (Exception e) {
			logger.error("modifyPowerPSM error",e);
		}
		reVal.put("success", false);
		reVal.put("msg", "服务器通信异常请稍后重试！");
		return reVal.toJSONString();
	}
	
}
