import "./ChatWindow.css"


export default function ChatWindow({ chatHistory,chatEndRef,loading }) {
    return (
      <div className="chat-body">
      {chatHistory.length === 0 ? (
        <div className="text-center text-muted mt-4">
          👋 <i>Welcome to DocuBot — Upload a file & ask your question!</i>
        </div>
      ) : (
        chatHistory.map((entry, index) => (
          <div key={index}>
            <div className="chat-message user-message">
            <strong>You:</strong>&nbsp;{entry.question}
            </div>
            <div className="chat-message bot-message">
            <strong>Bot:</strong>&nbsp;{entry.answer}
            </div>
          </div>
          
        ))
      )}
      {loading && (
  <div className="thinking-indicator">
    <span className="dot">.</span>
    <span className="dot">.</span>
    <span className="dot">.</span> <i>DocuBot is thinking...</i>
  </div>
)}

      <div ref={chatEndRef} />
    </div>
    
         
    );
  }
  