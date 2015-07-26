package net.laochu.design.model;

import java.util.Map;

public class RoomShape {
	private Integer pointNum;
	private Map<String, Double> points;
	private String type;
	private Integer typeNum;
	private String roomName;
	private Integer room_ID;
	private Integer shapeID;
	private Double[] controller;

	public RoomShape(Integer pointNum, String type,String roomName,Map<String, Double> points) {
		this.pointNum = pointNum;
		this.points = points;
		this.type=type;
		this.roomName=roomName;
	}
	
	public RoomShape(Double[] controller,Integer room_ID,Integer shapeID,Integer pointNum, Integer typeNum,String roomName,Map<String, Double> points) {
		this.pointNum = pointNum;
		this.points = points;
		this.typeNum=typeNum;
		this.roomName=roomName;
		this.room_ID=room_ID;
		this.shapeID=shapeID;
		this.controller=controller;
	}
	public Integer getTypeNum() {
		return typeNum;
	}

	public void setTypeNum(Integer typeNum) {
		this.typeNum = typeNum;
	}

	public RoomShape(){
		
	}
	
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Integer getPointNum() {
		return pointNum;
	}

	public void setPointNum(Integer pointNum) {
		this.pointNum = pointNum;
	}

	public Map<String, Double> getPoints() {
		return points;
	}

	public void setPoints(Map<String, Double> points) {
		this.points = points;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Integer getRoom_ID() {
		return room_ID;
	}

	public void setRoom_ID(Integer room_ID) {
		this.room_ID = room_ID;
	}

	public Integer getShapeID() {
		return shapeID;
	}

	public void setShapeID(Integer shapeID) {
		this.shapeID = shapeID;
	}

	public Double[] getController() {
		return controller;
	}

	public void setController(Double[] controller) {
		this.controller = controller;
	}

}
