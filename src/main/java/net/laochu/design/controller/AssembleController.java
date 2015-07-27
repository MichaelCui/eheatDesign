package net.laochu.design.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.laochu.design.model.RoomShape;
import net.laochu.design.model.Shape;
import net.laochu.design.util.HttpUtils;
import net.laochu.design.util.Tools;

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
 * 拼图页使用到的Controller
 */
@Controller
public class AssembleController {
	private static final Logger logger = LoggerFactory.getLogger(AssembleController.class);
	
	@Autowired
    private HttpUtils httpUtils;
	/**
	 * 跳转进入拼图页
	 * @param houseid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/assemble.htm", method = RequestMethod.GET)
	public ModelAndView assemble(String houseid,ModelMap model) {
		Map<String,String> params=new HashMap<String,String>();
		params.put("house_id", houseid);
		String resultStr=httpUtils.sendHttpPost("getRooms",JSON.toJSONString(params));
		JSONObject jobject=JSON.parseObject(resultStr);
		Boolean success=(Boolean) jobject.get("succ");
		List<Shape> list=new ArrayList<Shape>();
		if(success){
			list=JSON.parseArray(jobject.get("result").toString(), Shape.class);
		}
		//设置比例尺分割基数
		double dx=0;
		double dy=600;
		int size=list.size();
	    double scaleX=800;
	    double scaleY=600;
	    Integer flag=0;
	    if(size<=4){
		   scaleX=800/2;
		   scaleY=600/2;
		   flag=2;
	    }else if(size<=9){
		   scaleX=800/3;
		   scaleY=600/3;
		   flag=3;
	    }else if(size<=16){
		   scaleX=800/4;
		   scaleY=600/4;
		   flag=4;
	    }else if(size<=25){
		   scaleX=800/5;
		   scaleY=600/5;
		   flag=5;
	    }
	    //获得比例尺
		double scale=Tools.getScale(list,scaleX,scaleY);
		List<RoomShape> resultList=new ArrayList<RoomShape>();
		//循环进行绘图数据的添加
		for(int i=0;i<list.size();i++){
			//设置图形的偏移量
			if(i==0){
				dx=0;
				dy=dy-600/flag;
			}else if(i%flag==0 ){
				dx=0;
				dy=dy-600/flag;
			}else{
				dx=dx+800/flag;;
			}
			Shape shape=list.get(i);
			//根据type填充绘图数据
			switch(shape.shape_type){
				case 0:
					resultList.add(new RoomShape(shape.controller,shape.room_id,shape.room_id,3,0,shape.shape_name,Tools.getTriangle(shape.sides,dx,-dy,scale,shape.controller)));//三角形 3点
					break;
				case 1:
					resultList.add(new RoomShape(shape.controller,shape.room_id,shape.room_id,4,1,shape.shape_name,Tools.getRect(shape.sides,dx,-dy,scale,shape.controller)));//矩形  4点
					break;
				case 2:
					resultList.add(new RoomShape(shape.controller,shape.room_id,shape.room_id,4,2,shape.shape_name,Tools.getTrapezoid(shape.sides,dx,-dy,scale,shape.controller)));//梯形4点
					break;
				case 3:
					resultList.add(new RoomShape(shape.controller,shape.room_id,shape.room_id,3,3,shape.shape_name,Tools.getCircle(shape.sides,dx,-dy,scale,shape.controller)));//圆形 3点
					break;
				case 4:
					resultList.add(new RoomShape(shape.controller,shape.room_id,shape.room_id,6,4,shape.shape_name,Tools.getLpolygon(shape.sides,dx,-dy,scale,shape.controller)));//L型 6点
					break;
			
			}
		}

		model.put("shapeList",JSON.toJSONString(resultList));
		model.put("shapeTitleList",resultList);
		model.put("scale",scale);
		model.put("house_id",houseid);
		return new ModelAndView("assemble");
	}
	
	/**
	 * 向服务器发送拼合房间的ajax请求
	 * @param paramsStr
	 * @param houseid
	 * @param scale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/assembleHouse.htm", method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public String assembleHouse(String paramsStr,String houseid,Double scale,ModelMap model){
		logger.info("拼合房间：houseid:"+houseid);
		List<RoomShape> list=JSON.parseArray(paramsStr, RoomShape.class);
		List<Map<Object,Object>> rooms=new ArrayList<Map<Object,Object>>();
		for(RoomShape rs:list){
			double[] roompos=new double[3];
			roompos[0]=rs.getPoints().get("x0")*scale;
			roompos[1]=rs.getPoints().get("y0")*scale;
			roompos[2]=rs.getPoints().get("radian");
			Map<Object,Object> room=new HashMap<Object,Object>();
			room.put("room_id", rs.getRoom_ID());
			room.put("pos", roompos);
			rooms.add(room);
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("house_id", houseid);
		params.put("rooms", rooms);
		String resultStr=httpUtils.sendHttpPost("assembleHouse", JSON.toJSONString(params));
		return JSON.toJSONString(resultStr);
	}
}
