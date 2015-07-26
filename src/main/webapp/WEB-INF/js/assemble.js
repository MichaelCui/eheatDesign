
	function contoller(points){
		square.strokeStyle = "blue";
		square.font = "19px Courier New";
		square.fillStyle = "blue";
		square.strokeRect(points.conX,points.conY,15,15);
		square.fillText("T",points.conX+2,points.conY+13);
	}
	function Triangle(points){
		contoller(points);
		square.moveTo(points.x0,points.y0);
		square.lineTo(points.x1,points.y1);
		square.moveTo(points.x1,points.y1);
		square.lineTo(points.x2,points.y2);
		square.moveTo(points.x2,points.y2);
		square.lineTo(points.x0,points.y0);
	}
	function Rect(points){
		contoller(points);
		square.moveTo(points.x0,points.y0);
		square.lineTo(points.x1,points.y1);
		square.moveTo(points.x1,points.y1);
		square.lineTo(points.x2,points.y2);
		square.moveTo(points.x2,points.y2);
		square.lineTo(points.x3,points.y3);
		square.moveTo(points.x3,points.y3);
		square.lineTo(points.x0,points.y0);
	}
	function Lpolygon(points){
		contoller(points);
		square.moveTo(points.x0,points.y0);
		square.lineTo(points.x1,points.y1);
		square.moveTo(points.x1,points.y1);
		square.lineTo(points.x2,points.y2);
		square.moveTo(points.x2,points.y2);
		square.lineTo(points.x3,points.y3);
		square.moveTo(points.x3,points.y3);
		square.lineTo(points.x4,points.y4);
		square.moveTo(points.x4,points.y4);
		square.lineTo(points.x5,points.y5);
		square.moveTo(points.x5,points.y5);
		square.lineTo(points.x0,points.y0);	
	}
	function Circle(points){
		contoller(points);
	    var circle = {
	        x : points.x2,    //圆心的x轴坐标值
	        y : points.y2,    //圆心的y轴坐标值
	        r : points.r,      //圆的半径
			ab: points.ab,
			ae: points.ae
	    };
	    //沿着坐标点(100,100)为圆心、半径为r的圆的逆时针方向绘制弧线
	    var nae= points.ae<points.ab?points.ae+2*Math.PI:points.ae;
	    square.arc(circle.x, circle.y, circle.r,circle.ab , nae, false);    
	    square.moveTo(points.x0,points.y0);
	    square.lineTo(points.x1,points.y1);
	    //按照指定的路径绘制弧线
	    square.stroke();
	}

	function rotate(x,y,rad,pointNum,points){
		var px=[],py=[];
		for(p in points){
			if(p.indexOf("x") == 0){
				px.push(points[p]);
			}else if(p.indexOf("y") == 0){
				py.push(points[p]);
			}
		}
		for(var i=0;i<pointNum;i++){
			var tx=px[i]-x;
			var ty=py[i]-y;
			var r=Math.sqrt(tx*tx+ty*ty);
			var a=getDirRad(tx,ty);
			px[i]=r*Math.cos(rad+a)+x;
			py[i]=r*Math.sin(rad+a)+y;
			var pointX="x"+i;
			var pointY="y"+i;
			points[pointX]=px[i];
			points[pointY]=py[i];
		}
		
		var cx=points.conX-x;
		var cy=points.conY-y;
		var cr=Math.sqrt(cx*cx+cy*cy);
		var ca=getDirRad(cx,cy);
		points.conX=cr*Math.cos(rad+ca)+x;
		points.conY=cr*Math.sin(rad+ca)+y;
		
		var ae=getDirRad(points.x1-points.x2,points.y1-points.y2);//起始弧度
		var ab=getDirRad(points.x0-points.x2,points.y0-points.y2);//终止弧度
		
		points.ab=ab;
		points.ae=ae;
	}
	
	function getDirRad(angX,angY){
		var agl=0.0;
    	var x=angX;
    	var y=angY;
    	var t=Math.atan(Math.abs(angY)/Math.abs(angX));
		if(x<0){//二，三象限
    		if(y>0){//2
    			agl = Math.PI - t;
    		}else if(y<0){//3
    			agl = Math.PI + t;
    		}else{//-x
    			agl = Math.PI;
    		}
    	}else if(x>0){//一，四象限
    		if(y>0){//1
    			agl = t;
    		}else if(y<0){//4
    			agl = 2 * Math.PI - t;
    		}
    	}else{//x=0的情况,Y轴
    		if(y>0){
    			agl=Math.PI/2;
    		}else if(y<0){
    			agl=3*Math.PI/2;
    		}
    	}
    	return agl;    	
	}
	
	function getR(w,h){
		var r=(4*h*h+w*w)/(8*h);
		return r;
	}