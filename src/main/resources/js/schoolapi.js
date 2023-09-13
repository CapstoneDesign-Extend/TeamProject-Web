const apiKey = "adf19ad501a8fc77860ff1bd60d540a8";
const apiURL = "http://www.career.go.kr/cnet/openapi/getOpenApi";
const params = {
  apiKey: apiKey,
  svcType: "api",
  svcCode: "SCHOOL",
  contentType: "xml",
  gubun: "univ_list",
  thisPage : "1",
  perPage : "444"
};


// API 요청 보내기
fetch(apiURL + "?" + new URLSearchParams(params))
  .then(function(response) {
    return response.text();
  })
  .then(function(data) {
    // XML 데이터 파싱하기
    var parser = new DOMParser();
    var xml = parser.parseFromString(data, "text/xml");

    // 원하는 정보 추출하기
    var contentList = xml.getElementsByTagName("content");
    var universityName = [contentList.length];
    var universityCode = [contentList.length];
    var campusName = [contentList.length];
    var schoolnamefield = document.getElementsByClassName("schoolnamefield");
    var focus = true;

    for (var i = 0; i < contentList.length; i++) {
      universityName[i] = contentList[i].getElementsByTagName("schoolName")[0].textContent;
      universityCode[i] = contentList[i].getElementsByTagName("seq")[0].textContent;
      campusName[i] = contentList[i].getElementsByTagName("campusName")[0].textContent;
      //console.log("대학명: " + universityName[i] + ", 대학 코드: " + universityCode[i] + ", 캠퍼스구분: " + campusName[i]);
    }


    //학교 이름 데이터 a태그로 만들어서 숨겨놓기
    //여기서 생성되는 태그들의 스타일도 정의를 해주어야됨
    //학교검색용
    for (var i = 0; i < contentList.length; i++) {
      var newDiv = document.createElement('a');
      var newText = document.createTextNode(universityName[i] + "(" + campusName[i] + ")");
      var obj = document.getElementById("schoolname");
      newDiv.appendChild(newText);
      newDiv.className = 'schoolnamefield';
      newDiv.id = 'schoolnamefield';
      newDiv.href = '#';
      newDiv.style.position = 'relative';
      newDiv.style.padding = '6px 7px 6px 7px';
      newDiv.style.marginBottom = '4px';
      newDiv.style.boxSizing = 'border-box';
      newDiv.style.fontSize = 'small';
      obj.appendChild(newDiv);
      newDiv.style.display = 'none';

      //검색내용 클릭시 실행되는 이벤트 리스너
      newDiv.addEventListener('click',copycontent);
      newDiv.addEventListener('click',hiddencontent);
    }

    //검색어 필터 함수
    function filter(){
      var search = document.getElementsByClassName("search")[0].value;
      for (var i = 0; i < schoolnamefield.length; i++){
        if(universityName[i].indexOf(search) != -1){
          schoolnamefield[i].style.display = 'flex';
        } else {
          schoolnamefield[i].style.display = 'none';
        }
      }
    }

    //클릭 했을때 해당내용이 검색창에 입력하는 함수
    function copycontent() {
      var targetvalue = event.target.textContent;
      console.log(targetvalue);
      var input = document.getElementById("search");
      input.value = targetvalue;
    }

    //클릭 했을때 검색결과를 히든시키는 함수
    function hiddencontent() {
      for (var i = 0; i < schoolnamefield.length; i++){
        schoolnamefield[i].style.display = 'none';
        console.log("히든");
      }
      focus=false;
    }

    //focus 됐을시에 사용됨 클릭해서 입력했던 내용 지우고 처음부터 검색
    function handlefocus() {
      if(focus == false){
        var input = document.getElementById("search");
        input.value = "";
        focus = true;
      }
    }

    //검색 이벤트 리스너 --> 실제 검색시 실행되는 부분
    var sname = document.getElementById("search");
    sname.addEventListener('keyup',filter);
    sname.addEventListener('focus',handlefocus);
  
  })
  .catch(function(error) {
    console.error("API 요청 중 오류가 발생했습니다.", error);
  });
