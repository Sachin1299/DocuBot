package com.docubot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentProcessingService {

	public String extractTextPDF(MultipartFile file) {
		try(InputStream is = file.getInputStream();PDDocument document = PDDocument.load(is)){
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(document);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Failed to extract text from document: " + e.getMessage());
		}
	}
	
	public String extractTexttxt(MultipartFile file) {
		try(InputStreamReader input = new InputStreamReader(file.getInputStream()); BufferedReader reader = new BufferedReader(input)){
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
			}
			return builder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
