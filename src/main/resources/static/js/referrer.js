var ref = document.referrer;
var selectbox = document.getElementById('select_value');
var inputbox = document.getElementById('boardKind');
window.addEventListener('load',function(){
    var split_ref = ref.split('/');
    //alert(split_ref.indexOf('free_board.html?_ijt=rmd42v1atrfubbe8ki4ri5d3ss&_ij_reload=RELOAD_ON_SAVE'));
    //alert(split_ref);
    //alert(ref);

    if(split_ref.indexOf('FREE')>=0){
        selectbox.options[0].selected = true;
        inputbox.value = "FREE";
    } else if(split_ref.indexOf('MARKET')>=0) {
        selectbox.options[1].selected = true;
        inputbox.value = "MARKET";
    } else if(split_ref.indexOf('QNA')>=0) {
        selectbox.options[2].selected = true;
        inputbox.value = "QNA";
    }
})