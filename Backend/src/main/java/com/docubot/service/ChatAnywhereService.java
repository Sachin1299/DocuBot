package com.docubot.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.docubot.dto.AskQuestionRequest;
import com.docubot.dto.AskQuestionResponse;
import com.docubot.pojo.MyMessage;
import com.docubot.pojo.MyRequestBody;

@Service
public class ChatAnywhereService {
	
	@Value("${chatanywhere.api.key}")
	private String ApiKey;
	
	private final String URL = "https://api.chatanywhere.tech/v1/chat/completions";
	
//	public AskQuestionResponse getAnswer(AskQuestionRequest request) {
//		String userquery = "Document:\n" + request.getDocumentText() + "\n\nQuestion:\n" + request.getQuestion();
//		MyMessage systemprompt = new MyMessage("system", "You are a document assistant. Answer based only on the provided document.");
//		MyMessage userprompt = new MyMessage("user",userquery);
//		
//		MyRequestBody body = new MyRequestBody("gpt-3.5-turbo", List.of(systemprompt,userprompt), 0.7);
//		
//		HttpHeaders header = new HttpHeaders();
//		header.setContentType(MediaType.APPLICATION_JSON);
//		header.setBearerAuth(ApiKey);
//		header.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
//		System.out.println("Request to AI ------------------------");
//		System.out.println("body"+body.toString());
//		System.out.println("header"+ header);
//		
//		HttpEntity<MyRequestBody> entity = new HttpEntity(body,header);
//		
//		RestTemplate template = new RestTemplate();
//		try {
//		ResponseEntity<Map> response = template.postForEntity(URL, entity, Map.class);
//	    // Extract answer from response
//	    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
//	    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
//	    String answer = (String) message.get("content");
//
//	    return new AskQuestionResponse(answer.trim());
//		}catch (HttpServerErrorException e) {
//		    System.err.println("Error Response Body: " + e.getResponseBodyAsString());
//		    throw e; // rethrow or handle
//		}
//	}
	
	
	public AskQuestionResponse getAnswer(AskQuestionRequest request) {

        // Construct prompt using document text + question
        String systemPrompt = "You are a document assistant. Answer based on the provided document and currentDate is "+ LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        String userPrompt = "Document:\n" + request.getDocumentText() + "\n\nQuestion:\n" + request.getQuestion();

        // Build request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();
        //messages.add(Map.of("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))));
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userPrompt));

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Call API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(URL, entity, Map.class);

        // Parse response
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String answer = (String) message.get("content");

        return new AskQuestionResponse(answer.trim());
    }

}
