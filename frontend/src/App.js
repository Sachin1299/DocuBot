import { useState } from 'react';
import './App.css';
import BotAttachFile from './components/BotAttachFile';
import BotTextArea from './components/BotTextArea';
import SendButton from './components/SendButton';


function App() {
  const [message,setMessage] = useState();
  const [file, setFile] = useState();
  return (
    <div className="App">
      <BotTextArea message={message} setMessage={setMessage}/>
      <BotAttachFile file={file} setFile={setFile}/>
      <SendButton message={message} file={file}/>
    </div>
  );
}

export default App;
