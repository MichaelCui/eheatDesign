package net.laochu.design.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.laochu.design.model.Shape;


public class Tools {
	public static double PI=Math.PI;
	//三角形
	public static Map<String,Double> getTriangle(Double[] pos,Double dx,Double dy,Double scale,Double[] controller){
		Double A = pos[0];
		Double B = pos[1];
		Double x0,y0,x1,y1,x2,y2;
		x0=0.0;
		y0=600.0;
		x1=B/scale;
		y1=y0;
		x2=0.0;
		y2=600-A/scale;
		Map<String,Double> m=new HashMap<String,Double>();
		m.put("x0", x0+dx);
		m.put("x1", x1+dx);
		m.put("x2", x2+dx);
		
		m.put("y0", y0+dy);
		m.put("y1", y1+dy);
		m.put("y2", y2+dy);
		m.put("radian", 0.0);
		m.put("conX", controller[0]/scale+dx);
		m.put("conY", 600-controller[1]/scale+dy);
		return m;
	}
	//矩形
	public static  Map<String,Double> getRect(Double[] pos,Double dx,Double dy,Double scale,Double[] controller){
		Double A = pos[0];
		Double B = pos[1];
		Double x0,y0,x1,y1,x2,y2,x3,y3;
		x0=0.0;
		y0=600.0;
		x1=B/scale;
		y1=600.0;
		x2=B/scale;
		y2=600-A/scale;
		x3=0.0;
		y3=600-A/scale;
		Map<String,Double> m=new HashMap<String,Double>();
		m.put("x0", x0+dx);
		m.put("x1", x1+dx);
		m.put("x2", x2+dx);
		m.put("x3", x3+dx);		
		m.put("y0", y0+dy);
		m.put("y1", y1+dy);
		m.put("y2", y2+dy);
		m.put("y3", y3+dy);		
		m.put("radian", 0.0);
		m.put("conX", controller[0]/scale+dx);
		m.put("conY", 600-controller[1]/scale+dy);
		return m;
	}
	//梯形
	public static  Map<String,Double> getTrapezoid(Double[] pos,Double dx,Double dy,Double scale,Double[] controller){
		
		Double A = pos[0];
		Double B = pos[1];
		Double C = pos[2];
		Double x0,y0,x1,y1,x2,y2,x3,y3;
		x0=0.0;
		y0=600.0;
		x1=C/scale;
		y1=600.0;
		x2=A/scale;
		y2=600-B/scale;
		x3=0.0;
		y3=600-B/scale;
		Map<String,Double> m=new HashMap<String,Double>();
		m.put("x0", x0+dx);
		m.put("x1", x1+dx);
		m.put("x2", x2+dx);
		m.put("x3", x3+dx);		
		m.put("y0", y0+dy);
		m.put("y1", y1+dy);
		m.put("y2", y2+dy);
		m.put("y3", y3+dy);
		m.put("radian", 0.0);
		m.put("conX", controller[0]/scale+dx);
		m.put("conY", 600-controller[1]/scale+dy);
		return m;
	}
	//L型
	public static  Map<String,Double> getLpolygon(Double[] pos,Double dx,Double dy,Double scale,Double[] controller){
		Double A = pos[0];
		Double B = pos[1];
		Double C = pos[2];
		Double D = pos[3];
		Double x0,x1,x2,x3,x4,x5,y0,y1,y2,y3,y4,y5;
		x0=0.0;
		y0=600.0;
		x1=B/scale;
		y1=600.0;
		x2=B/scale;
		y2=600-(A/scale-C/scale);
		x3=(B/scale-D/scale);
		y3=600-(A/scale-C/scale);
		x4=(B/scale-D/scale);
		y4=600-A/scale;
		x5=0.0;
		y5=600-A/scale;
		
		
		Map<String,Double> m=new HashMap<String,Double>();
		m.put("x0", x0+dx);
		m.put("x1", x1+dx);
		m.put("x2", x2+dx);
		m.put("x3", x3+dx);
		m.put("x4", x4+dx);
		m.put("x5", x5+dx);
		
		m.put("y0", y0+dy);
		m.put("y1", y1+dy);
		m.put("y2", y2+dy);
		m.put("y3", y3+dy);
		m.put("y4", y4+dy);
		m.put("y5", y5+dy);
		
		m.put("radian", 0.0);
		m.put("conX", controller[0]/scale+dx);
		m.put("conY", 600-controller[1]/scale+dy);
		return m;
	}
	//弧形
	public static  Map<String,Double> getCircle(Double[] pos,Double dx,Double dy,Double scale,Double[] controller){
		
		double w= pos[0];
		double h= pos[1];
				
		double x=w/2;//圆心X
		double y=h-getR(w,h);//圆心Y
		double x0=0.0;
		double y0=0.0;
		double x1=w;
		double y1=0.0;
		double r=getR(w,h);
		Map<String,Double> m=new HashMap<String,Double>();
		Double ae=2*Math.PI-getDirRad(x1-x,y1-y);//起始弧度
		Double ab=2*Math.PI-getDirRad(x0-x,y0-y);//终止弧度
		ae= ae<ab?ae+2*Math.PI:ae;
		
		m.put("ab", ab);
		m.put("ae", ae);
	    m.put("r", r/scale);
	    
	    m.put("x2", x/scale+dx);
	    m.put("x0", x0/scale+dx);
	    m.put("x1", x1/scale+dx);
	    
	    m.put("y2", 600-(y/scale)+dy);
		m.put("y1", 600-(y1/scale)+dy);
		m.put("y0", 600-(y0/scale)+dy);
		
		m.put("radian", 0.0);
		m.put("conX", controller[0]/scale+dx);
		m.put("conY", 600-controller[1]/scale+dy);
		return m;
	}
	/**
	 * 计算弓形所处圆的半径
	 * @param w 弦长
	 * @param h 拱高
	 * @return 半径
	 */
	public static  double getR(double w,double h){
		double r=(4*h*h+w*w)/(8*h);
		return r;
		
	}
	
	public static  String format(Double d){
		DecimalFormat df = new DecimalFormat("#.00");   
		return df.format(d);
		
	}
	
	public static String formatStr(String d){
		DecimalFormat df = new DecimalFormat("#.00");   
		return df.format(Double.parseDouble(d));
	}
	
	public static long timeMillis(){
		return System.currentTimeMillis();
	}
	
	   public static   double getDirRad(double angX,double angY) {
	    	double agl=0.0;
	    	double x=angX;
	    	double y=angY;
	    	double t=Math.atan(Math.abs(angY)/Math.abs(angX));
	    	if(x<0){//二，三象限
	    		if(y>0){//2
	    			
	    			agl = PI - t;
	    		}else if(y<0){//3
	    			agl = PI + t;
	    		}else{//-x
	    			agl = PI;
	    		}
	    	}else if(x>0){//一，四象限
	    		if(y>0){//1
	    			agl = t;
	    		}else if(y<0){//4
	    			agl = 2 * PI - t;
	    		}
	    	}else{//x=0的情况,Y轴
	    		if(y>0){
	    			agl=PI/2;
	    		}else if(y<0){
	    			agl=3*PI/2;
	    		}
	    	}
	    	return agl;
	    }
	   
	   public static double getScale(List<Shape> list,double scaleX,double scaleY){
		   double A=0,B=0,C=0;
		   List<Double> scaleList=new ArrayList<Double>();
		   for(Shape shape:list){
			   switch(shape.shape_type){
			   case 0:
				   A=shape.sides[0];
				   B=shape.sides[1];
				   scaleList.add(A/scaleY>B/scaleX?A/scaleY:B/scaleX);
				   break;
			   case 1:
				   A=shape.sides[0];
				   B=shape.sides[1];
				   scaleList.add(A/scaleY>B/scaleX?A/scaleY:B/scaleX);
				   break;
			   case 2:
				   A=shape.sides[0];
				   B=shape.sides[1];
				   C=shape.sides[2];
				   Double temp=A>C?A:C;
				   scaleList.add(B/scaleY>temp/scaleX?B/scaleY:temp/scaleX);
				   break;
			   case 3:
				   A=shape.sides[0];
				   B=shape.sides[1];
				   scaleList.add(A/scaleX>B/scaleY?A/scaleX:B/scaleY);
				   break;
			   case 4:
				   A=shape.sides[0];
				   B=shape.sides[1];
				   scaleList.add(A/scaleY>B/scaleX?A/scaleY:B/scaleX);
				   break;
			   }
		   }
		   Collections.sort(scaleList);
		   Collections.reverse(scaleList);
			if(scaleList.size()>0){
				return scaleList.get(0);
			}
		   return 1.0;
	   }
}
