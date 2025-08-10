// src/services/apiService.js

export async function uploadFile(file) {
    const formData = new FormData();
    formData.append("file", file);
  
    const response = await fetch("https://localhost:8443/api/files/upload", {
      method: "POST",
      body: formData,
      credentials: "include",      // <-- Include cookies here!
      // Do NOT set Access-Control-Allow-Origin header in browser requests. This is set by server.
    });
  
    if (!response.ok) throw new Error("File upload failed");
    return await response.json(); // contains { content: "..." }
  }
  
  export async function askQuestion(documentText, question) {
    const finalBody = {
      documentText,
      question,
    };
  
    const response = await fetch("https://localhost:8443/api/qa/ask", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(finalBody),
    credentials: "include",      // <-- Include cookies here too!
  });
  
    if (!response.ok) throw new Error("AI request failed");
    return await response.json(); // contains { response: "..." }
  }
  