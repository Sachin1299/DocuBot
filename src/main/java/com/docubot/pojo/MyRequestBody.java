package com.docubot.pojo;

import java.util.List;

public class MyRequestBody {

	private String model;
	private List<MyMessage> message;
	private double temprature;
	public String getModel() {
		return model;
	}
	public List<MyMessage> getMessage() {
		return message;
	}
	public double getTemprature() {
		return temprature;
	}
	public MyRequestBody(String model, List<MyMessage> message, double temprature) {
		super();
		this.model = model;
		this.message = message;
		this.temprature = temprature;
	}
	@Override
	public String toString() {
		return "MyRequestBody [model=" + model + ", message=" + message + ", temprature=" + temprature + "]";
	}
	
}
