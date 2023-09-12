var checkboxes = document.getElementsByClassName('required_check');
var checkboxes2 = document.getElementsByClassName('required_check2');
var adcheck = document.getElementById('adc');
var div = document.getElementById('content');
var cont1 = document.getElementById('cont1');
var cont2 = document.getElementById('cont2');
var cont3 = document.getElementById('cont3');
var cont4 = document.getElementById('cont4');
var cont5 = document.getElementById('cont5');
var ad = document.getElementById('ad');

function allcheckbox(selectAll) {
    var allcheckbox = document.getElementById("allcheck");
    var ischecked = allcheckbox.checked;
    console.log(ischecked);
    if(ischecked) {
        for(var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = selectAll.checked;
        }
    }
    
    if(!ischecked) {
        for(var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = selectAll.checked;
        }
    }
}

div.addEventListener('click', function(){
    var check1 = checkboxes[0].checked;
    var check2 = checkboxes[1].checked;
    var check3 = checkboxes[2].checked;
    var check4 = checkboxes2[0].checked;
    var check5 = checkboxes2[1].checked;
    var check6 = adcheck.checked;


    console.log(check1, check2, check3)
    var button1 = document.getElementById('phone');
    var button2 = document.getElementById('ipin');
    if(check1 == false || check2 == false || check3 == false || check4 == false || check5 == false) {
        button1.disabled = true;
        button2.disabled = true;
    }
    else if(check1 && check2 && check3 && check4 && check5) {
        button1.disabled = false;
        button2.disabled = false;
    }
    
    if(check1){
        cont1.style.display = 'none';
    }
    else{
        cont1.style.display = 'block';
    }

    if(check2){
        cont2.style.display = 'none';
    }
    else{
        cont2.style.display = 'block';
    }

    if(check3){
        cont3.style.display = 'none';
    }
    else{
        cont3.style.display = 'block';
    }

    if(check4){
        cont4.style.display = 'none';
    }
    else{
        cont4.style.display = 'block';
    }

    if(check5){
        cont5.style.display = 'none';
    }
    else{
        cont5.style.display = 'block';
    }

    if(check6){
        ad.style.display = 'none';
    }
    else{
        ad.style.display = 'block';
    }
});
