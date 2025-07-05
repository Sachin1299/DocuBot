import { useState } from "react"




 export default function Myform(){
 const [file, setFile] = useState(null);
 const [result, setResult] = useState('');
 //const [message, setMessage] = useState(null);
 function handlefilechange(e){
  setFile(e.target.files[0]);
 }
 const handleSubmit = async (e)=>{
  e.preventDefault();
console.log("handle submitt called");
  if(!file){
console.log("upload file");
  }

const formdata = new FormData();
formdata.append("file",file);
try {
  const response = await fetch("http://localhost:8080/api/files/upload",{
    method:"POST",
    body:formdata
  })
   const tempresult = await response.json();
   setResult(tempresult);
      console.log("Server response:", result);
      alert("File uploaded successfully!");
} catch (error) {
  console.log(error);
  console.log(error);
      console.error("Upload failed:", error);
      alert("Upload failed.", error);
}

 }


    return(
    <>
      <h1>Hello There!!</h1>
      <form onSubmit={handleSubmit}>
        <input type={"file"} name="file" onChange={handlefilechange}/>
       <button type="submit">submit</button>
      </form>
      <p>{result}</p>
    </>)
}


// import React, { useState } from "react";

// const Myform = () => {
//   const [file, setFile] = useState(null);

//   const handleFileChange = (e) => {
//     setFile(e.target.files[0]); // Save the selected file to state
//   };

//   const handleSubmit = async (e) => {
//     e.preventDefault(); // Prevent page reload

//     if (!file) {
//       alert("Please select a file before submitting.");
//       return;
//     }

//     const formData = new FormData();
//     formData.append("file", file); // 'file' must match Spring Boot @RequestParam name

//     try {
//       const response = await fetch("http://localhost:8080/upload", {
//         method: "POST",
//         body: formData,
//         // Note: Do NOT manually set Content-Type. Browser sets it automatically with boundary.
//       });

//       const result = await response.json();
//       console.log("Server response:", result);
//       alert("File uploaded successfully!");
//     } catch (error) {
//       console.log(error);
//       console.error("Upload failed:", error);
//       alert("Upload failed.", error);
//     }
//   };

//   return (
//     <form onSubmit={handleSubmit}>
//       <input type="file" onChange={handleFileChange} />
//       <button type="submit">Upload</button>
//     </form>
//   );
// };

// export default Myform;
