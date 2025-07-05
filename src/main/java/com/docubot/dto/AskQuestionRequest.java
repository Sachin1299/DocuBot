package com.docubot.dto;

public class AskQuestionRequest {
private String documentText;
private String question;
public AskQuestionRequest(String documentText, String question) {
	super();
	this.documentText = documentText;
	this.question = question;
}
public String getDocumentText() {
	return documentText;
}
public String getQuestion() {
	return question;
}

}
