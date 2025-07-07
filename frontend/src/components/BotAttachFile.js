export default function BotAttachFile({ file, setFile }) {
    const handleFileChange = (e) => {
      setFile(e.target.files[0]);
    };
  
    return (
      <>
        <label className="attach-icon" htmlFor="file-upload">📎 Attach</label>
        <input
          id="file-upload"
          type="file"
          onChange={handleFileChange}
          className="file-input"
        />
      </>
    );
  }
  