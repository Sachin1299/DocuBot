package com.docubot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentProcessingService {

	private String extractTextPDF(MultipartFile file) {
		try(InputStream is = file.getInputStream();PDDocument document = PDDocument.load(is)){
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(document);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Failed to extract text from document: " + e.getMessage());
		}
	}
	
	private String extractTextDoc(MultipartFile file) {
		try(InputStream is = file.getInputStream(); XWPFDocument document = new XWPFDocument(is)){
			StringBuilder builder = new StringBuilder();
			for(XWPFParagraph para : document.getParagraphs()) {
				builder.append(para.getText()).append("\n");
			}
			return builder.toString();
		} catch (IOException e) {
			throw new RuntimeException("Failed to extract text from DOCX: " + e.getMessage());
		}
		
	}
	
	private String extractTexttxt(MultipartFile file) {
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
	
	public String extractText(MultipartFile file) {
		String FileName = file.getOriginalFilename();
		if(FileName == null) {
			throw new RuntimeException("File must have a name");
		}
		if(FileName.endsWith(".pdf")) {
			return extractTextPDF(file);
		}
		else if(FileName.endsWith(".txt")) {
			return extractTexttxt(file);
		}
		else if(FileName.endsWith(".docx")) {
			return extractTextDoc(file);
		}
		else {
			throw new RuntimeException("Unsupported File Type");
		}
	}
	
}
