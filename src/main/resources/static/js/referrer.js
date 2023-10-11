var ref = document.referrer;
var selectbox = document.getElementById('select_value');
var inputbox = document.getElementById('boardKind');
window.addEventListener('load',function(){
    var split_ref = ref.split('/');
    //alert(split_ref.indexOf('free_board.html?_ijt=rmd42v1atrfubbe8ki4ri5d3ss&_ij_reload=RELOAD_ON_SAVE'));
    //alert(split_ref);
    //alert(ref);

    if(split_ref.indexOf('TIP')>=0){
        selectbox.options[0].selected = true;
        inputbox.value = "TIP";
    } else if(split_ref.indexOf('REPORT')>=0) {
        selectbox.options[1].selected = true;
        inputbox.value = "REPORT";
    } else if(split_ref.indexOf('QNA')>=0) {
        selectbox.options[2].selected = true;
        inputbox.value = "QNA";
    } else if(split_ref.indexOf('MARKET')>=0) {
        selectbox.options[3].selected = true;
        inputbox.value = "MARKET";
    } else if(split_ref.indexOf('FREE')>=0) {
        selectbox.options[4].selected = true;
        inputbox.value = "FREE";
    } else if(split_ref.indexOf('FRESH')>=0) {
        selectbox.options[5].selected = true;
        inputbox.value = "FRESH";
    } else if(split_ref.indexOf('FOSSIL')>=0) {
        selectbox.options[6].selected = true;
        inputbox.value = "FOSSIL";
    } else if(split_ref.indexOf('INFO')>=0) {
        selectbox.options[7].selected = true;
        inputbox.value = "INFO";
    } else if(split_ref.indexOf('CAREER')>=0) {
        selectbox.options[8].selected = true;
        inputbox.value = "CAREER";
    }
})