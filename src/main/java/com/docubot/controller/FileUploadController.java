package com.docubot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.docubot.dto.DocumentTextResponse;
import com.docubot.service.*;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
	
	@Autowired
	private DocumentProcessingService processingService;
	
	@PostMapping("/upload")
	public ResponseEntity<DocumentTextResponse> uploadfile(@RequestParam MultipartFile file){
		try {
			if(file.isEmpty()||file.getSize()==0) {
				return ResponseEntity.badRequest().body(new DocumentTextResponse("File is Empty"));
			}
			if("text/plain".equals(file.getContentType())) {
				String result = processingService.extractTexttxt(file);
				return ResponseEntity.ok().body(new DocumentTextResponse(result));
			}
		    String result = processingService.extractTextPDF(file);
			System.out.println(file.getContentType()+"file readed successfully");
			return ResponseEntity.ok().body(new DocumentTextResponse(result));
		
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(new DocumentTextResponse("Error while reading file and this is the error: "+e));
		}
	}
	 // Handle cases where request isn't multipart
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartError(MultipartException ex) {
        return ResponseEntity.badRequest()
                .body("Invalid file upload request. Make sure your form is 'multipart/form-data'. Details: " + ex.getMessage());
    }

}
