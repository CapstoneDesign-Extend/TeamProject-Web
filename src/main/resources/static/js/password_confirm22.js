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
        for(var i=0; i<5; i++){
            inputdiv[i] = document.getElementsByClassName('input')[i];
            console.log(inputdiv[i].value);
        }
    
        if(inputdiv[0].value!=null&& inputdiv[1].value!=null && inputdiv[2].value!=null && inputdiv[3].value!=null && inputdiv[4].value!=null) {
            registerbtn.disabled = false;
        }
    }
    console.log(registerbtn.disabled)
});

