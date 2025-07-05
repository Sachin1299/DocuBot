export default function BotTextArea({message,setMessage}){
    return(
<>
<div>
    <textarea 
    value={message}
    placeholder="Upload and ask anything"
    onChange={(e)=>setMessage(e.target.value)}
    />
   
</div>
</>
    );
}