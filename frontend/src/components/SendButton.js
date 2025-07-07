

export default function SendButton({ handleSend, loading, setLoading }) {
  const handleClick = async () => {
    setLoading(true);
    await handleSend();
    setLoading(false);
  };

  return (
    <button className="send-button" onClick={handleClick}>
      {loading ? "Sending..." : "➤"}
    </button>
  );
}
