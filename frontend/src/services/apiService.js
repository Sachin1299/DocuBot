// src/services/apiService.js

export async function uploadFile(file) {
    const formData = new FormData();
    formData.append("file", file);
  
    const response = await fetch("http://localhost:8080/api/files/upload", {
      method: "POST",
      body: formData,
      headers: {"Access-Control-Allow-Origin":"*"}
    });
  
    if (!response.ok) throw new Error("File upload failed");
    return await response.json(); // contains { content: "..." }
  }
  
  export async function askQuestion(documentText, question) {
    const finalBody = {
      documentText,
      question,
    };
  
    const response = await fetch("http://localhost:8080/api/qa/ask", {
      method: "POST",
      headers: { "Content-Type": "application/json","Access-Control-Allow-Origin":"*" },
      body: JSON.stringify(finalBody),
    });
  
    if (!response.ok) throw new Error("AI request failed");
    return await response.json(); // contains { response: "..." }
  }
  