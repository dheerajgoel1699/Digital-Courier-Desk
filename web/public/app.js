var config = {
  apiKey: "AIzaSyBbKzdk3wqyH75gFMCIC_ZfeG8NNRLrSAs",
  authDomain: "digital-courier-desk.firebaseapp.com",
  databaseURL: "https://digital-courier-desk.firebaseio.com",
  projectId: "digital-courier-desk",
  storageBucket: "digital-courier-desk.appspot.com",
  messagingSenderId: "73901545863",
  appId: "1:73901545863:web:1ae347f3cd22fbf1f11b22",
  measurementId: "G-61HPMDX7MY"
};
  firebase.initializeApp(config);


 const inputName=document.querySelector("#name");
 const inputEmail=document.querySelector("#email");
 const inputPhone=document.querySelector("#phone");
 const saveButton=document.querySelector("#saveButton");


var firestore = firebase.firestore();



saveButton.addEventListener("click",function(){
  var name = "users/"+inputName.value
  var valid = true;
  var phone = inputPhone.value;
  var email = inputEmail.value;

  if(isNaN(phone)||(!email)||(!inputName.value)||(!phone)){
    valid = false;
  }

  if(valid){
    const docRef = firestore.doc(name);
      docRef.set({
          Name: inputName.value,
          Email: inputEmail.value,
          Phone: inputPhone.value
      }).then(function(){
          console.log("Saved");
          location.href = "done.html";
      }).catch(function(error){
          console.log("Got an error: ",error);
      });
  }

  else {
    document.getElementById("validation").innerHTML = "Man, stuff isn't right. Check your values, son.";
  }


});
