import { useState } from "react";

export default function BotTextArea({ message, setMessage, onEnterPress, setLoading }) {
  const [editable, setEditable]=useState(false);
  const handleKeyDown = async (e) => {
    
    if (e.key === 'Enter' && !e.shiftKey) {
      setEditable(true);
      setLoading(true);
      e.preventDefault(); // prevent newline
      
      await onEnterPress();
      setEditable(false);
      setLoading(false);
    }
  };

  return (
    <textarea
      className="bot-textarea"
      value={message}
      placeholder="Ask anything..."
      onChange={(e) => setMessage(e.target.value)}
      onKeyDown={handleKeyDown}
      disabled={editable}
    />
  );
}
