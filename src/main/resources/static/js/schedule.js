
function change(){
  var className = event.target.className;
  var nowactive = document.getElementsByClassName('active');
  if(className == "deact"){
    nowactive[0].className = "deact";
    event.target.className = "active";
  }
}