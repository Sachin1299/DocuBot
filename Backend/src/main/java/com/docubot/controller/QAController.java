package com.docubot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docubot.dto.*;
import com.docubot.service.ChatAnywhereService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/qa")
public class QAController {
	
	@Autowired
	private ChatAnywhereService service;

	@PostMapping("/ask")
	public ResponseEntity<AskQuestionResponse> askQuestion(@RequestBody AskQuestionRequest request){
		try {
			AskQuestionResponse response = service.getAnswer(request);
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}

	}
}
