import { useState, useRef, useEffect } from 'react';
import '../App.css';
import Header from '../components/Header';
import ChatWindow from '../components/ChatWindow';
import BotAttachFile from '../components/BotAttachFile';
import BotTextArea from '../components/BotTextArea';
import SendButton from '../components/SendButton';
import { uploadFile, askQuestion } from '../services/apiService';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Home() {
  const [message, setMessage] = useState('');
  const [file, setFile] = useState(null);
  const [chatHistory, setChatHistory] = useState([]);
  const [loading, setLoading] = useState(null);
  const chatEndRef = useRef(null);
  const Navigate = useNavigate();
  const clearChat = () => {
    setChatHistory([]);
    setMessage('');
  };

  const handleResponse = async () => {
    if (!file || !message.trim()){
      setChatHistory(prev => [...prev, { question: message, answer: "Please upload file first" }]);
      setMessage('');
    } 
    else{

    
    try {
      setLoading(true);
      const fileResult = await uploadFile(file);
      const aiResult = await askQuestion(fileResult.content, message);
      setChatHistory(prev => [...prev, { question: message, answer: aiResult.response }]);
      setMessage('');
    } catch (err) {
      console.error("AI Error:", err);
    }
  }
  };

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [chatHistory]);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const result = await axios.get("https://localhost:8443/api/auth/check", 
      { withCredentials: true });
        console.log(result.status);
      } catch (error) {
        Navigate("/login")
      }
    };
  
    checkAuth(); // ✅ call it
  }, []);
  

  return (
    <div className="App">
      <Header />
      {file && (
        <div className="file-info sticky-header" >
          📄 <strong>{file.name}</strong> ({(file.size / 1024).toFixed(1)} KB)
        </div>
      )}
      <ChatWindow loading={loading} chatHistory={chatHistory} chatEndRef={chatEndRef} />
      <div className="bottom-bar">
        <BotAttachFile setFile={setFile} />
        <BotTextArea
          message={message}
          setMessage={setMessage}
          onEnterPress={handleResponse} // 
          setLoading={setLoading}
        />
        <button className="clear-button" onClick={clearChat}>
          🧹 Clear
        </button>
        <SendButton
          loading={loading}
          setLoading={setLoading}
          handleSend={handleResponse} // 
        />
      </div>
    </div>
  );
}

export default Home;
