package com.docubot.pojo;

public class MyMessage {
  private String role;
  private String content;
public MyMessage(String role, String content) {
	super();
	this.role = role;
	this.content = content;
}
public String getRole() {
	return role;
}
public String getContent() {
	return content;
}
@Override
public String toString() {
	return "MyMessage [role=" + role + ", content=" + content + "]";
}
  

}
