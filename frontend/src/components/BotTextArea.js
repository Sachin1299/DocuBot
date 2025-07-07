export default function BotTextArea({ message, setMessage, onEnterPress }) {
  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault(); // prevent newline
      onEnterPress();
    }
  };

  return (
    <textarea
      className="bot-textarea"
      value={message}
      placeholder="Ask anything..."
      onChange={(e) => setMessage(e.target.value)}
      onKeyDown={handleKeyDown}
    />
  );
}
