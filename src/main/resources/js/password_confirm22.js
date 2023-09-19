var password = document.getElementById('pwd');
var password_confirm = document.getElementById('pwd_re');
var registerbtn = document.getElementsByClassName('register')[0];
var form = document.getElementById('input_form');
registerbtn.disabled = true;
var inputdiv = []

form.addEventListener('keyup', function(){
    if(password_confirm.value != password.value){
        var warn = document.getElementById('not');
        warn.style.display = 'block'
        registerbtn.disabled = true;
    }
    else if(password_confirm.value == password.value){
        var warn = document.getElementById('not');
        warn.style.display = 'none'
        for(var i=0; i<6; i++){
            inputdiv[i] = document.getElementsByClassName('input')[i];
            console.log(inputdiv[i].value);
        }
        if(inputdiv[0].value!=""&& inputdiv[1].value!="" && inputdiv[2].value!="" && inputdiv[3].value!="" && inputdiv[4].value!="" && inputdiv[5].value!="") {
            registerbtn.disabled = false;
        } else {
            registerbtn.disabled = true;
        }
    }
    console.log(registerbtn.disabled)
});

