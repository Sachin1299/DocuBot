

 export default function BotAttachFile({file, setFile}){
    return(
        <>
        <div>
        <input
         onChange={(e)=>setFile(e.target.files[0])}
         type={"file"} 
         placeholder="upload file"/>
        </div>
        </>
    )
}