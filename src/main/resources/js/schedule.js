const custom_button = document.getElementById('custom_button');
const customsubjects = document.getElementById('customsubjects');
const xbutton = document.getElementById('xbutton');
var count = 0;

custom_button.addEventListener('click', function(){
  custom_button.style.display = 'none';
  customsubjects.style.display = 'block';
});

xbutton.addEventListener('mouseover', function(){
  xbutton.style.backgroundColor = 'red';
});

xbutton.addEventListener('mouseleave', function(){
  xbutton.style.backgroundColor = 'gray';
});

xbutton.addEventListener('click', function(){
  custom_button.style.display = 'block';
  customsubjects.style.display = 'none';
});

function change(){
  var className = event.target.className;
  var nowactive = document.getElementsByClassName('active');
  if(className == "deact"){
    nowactive[0].className = "deact";
    event.target.className = "active";
  }
}