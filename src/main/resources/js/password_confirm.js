var password = document.getElementById('pwd');
var password_confirm = document.getElementById('pwd_re');

password_confirm.addEventListener('keyup', function(){
    if(password_confirm.value != password.value){
        var warn = document.getElementById('not');
        warn.style.display = 'block'
    }
    else if(password_confirm.value == password.value){
        var warn = document.getElementById('not');
        warn.style.display = 'none'
    }
})