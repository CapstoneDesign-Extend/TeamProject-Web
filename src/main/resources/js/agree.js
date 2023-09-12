function is_checked() {
    var checkboxEl = document.getElementById('check');
    var is_checked = checkboxEl.checked;
    var today = new Date();
    var year = today.getFullYear();
    var month = ('0' + (today.getMonth()+1)).slice(-2);
    var day = ('0' + today.getDate()).slice(-2);
    var todayString = year + '년 ' + month + '월 ' + day + '일 '
    if(is_checked)
    {
        alert("Extend\n" + todayString + "\n" + "광고성 정보 수신 동의 완료");
    }
    if(!is_checked)
    {
        alert("Extend\n" + todayString + "\n" + "광고성 정보 수신 거부 완료");
    }
}



