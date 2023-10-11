// const searchEl = document.querySelector('.search');
// const searchInputEl = searchEl.querySelector('input');


searchEl.addEventListener('click', function(){
  searchInputEl.focus();
});

searchInputEl.addEventListener('focus',function(){
  searchEl.classList.add('focused');
  searchInputEl.setAttribute('placeholder', '통합검색');
});

searchInputEl.addEventListener('blur', function(){
  searchEl.classList.remove('focused');
  searchInputEl.setAttribute('placeholder','');
});

const maincategory = document.getElementsByClassName('main_category_mypage')[0];
const maincategory_community = document.getElementsByClassName('main_category_community')[0];
const subcategory = document.querySelector('.sub_category_mypage');
const subcategory_community = document.querySelector('.sub_category_community')
const wrapsubcategory = document.querySelector('.wrap_sub_category_mypage');
const wrapsubcategory_community = document.querySelector('.wrap_sub_category_community');
const mainframe = document.querySelector('.mainframe');

//마이페이지 버튼 호버링
maincategory.addEventListener('mouseover', function(){
  console.log("콘솔");
  subcategory.classList.remove('hide');
  wrapsubcategory.style.display = "flex";
  mainframe.style.marginTop = "99px";
  mainframe.style.transition = "margin .4s";
});

maincategory.addEventListener('mouseout', function(){
  subcategory.classList.add('hide');
  wrapsubcategory.style.display = "none";
  mainframe.style.marginTop = "40px";
  mainframe.style.transition = "margin .4s";
});

//커뮤니티 버튼 호버링
maincategory_community.addEventListener('mouseover', function(){
  subcategory_community.classList.remove('hide');
  wrapsubcategory_community.style.display = "flex";
  mainframe.style.marginTop = "99px";
  mainframe.style.transition = "margin .4s";
})
maincategory_community.addEventListener('mouseout', function(){
  subcategory_community.classList.add('hide');
  wrapsubcategory_community.style.display = "none";
  mainframe.style.marginTop = "40px";
  mainframe.style.transition = "margin .4s";
});


const calendartoday = document.querySelector('.calendartoday').firstChild;
var today = new Date();
var year = today.getFullYear();
var month = ('0' + (today.getMonth()+1)).slice(-2);
var day = ('0' + today.getDate()).slice(-2);
var weekday = new Array(7);
weekday[0] = "일요일";
weekday[1] = "월요일";
weekday[2] = "화요일";
weekday[3] = "수요일";
weekday[4] = "목요일";
weekday[5] = "금요일";
weekday[6] = "토요일";

var todayString = year + '년 ' + month + '월 ' + day + '일 ' + weekday[today.getDay()];
console.log(todayString);
calendartoday.nodeValue = todayString;

function generateCalendar() {
  var calendarBody = document.getElementById("calendar-body");

  // 현재 날짜 가져오기
  var date = new Date();

  // 달의 첫째 날 가져오기
  var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);

  // 달의 첫째 날의 요일 가져오기 (0: 일요일, 1: 월요일, ... , 6: 토요일)
  var startingDay = firstDay.getDay();

  // 달의 마지막 날 가져오기
  var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();

  // 달력 초기화
  calendarBody.innerHTML = "";

  // 달력 날짜 생성
  var dateCount = 1;
  for (var row = 0; row < 6; row++) {
    var tr = document.createElement("tr");

    for (var col = 0; col < 7; col++) {
      if (row === 0 && col < startingDay) {
        var td = document.createElement("td");
        td.classList.add("cal-body");
        tr.appendChild(td);
      } else if (dateCount > lastDay) {
        break;
        } else {
        var td = document.createElement("td");
        td.classList.add("cal-body");

        // 날짜 div 생성
        var dateDiv = document.createElement("div");
        dateDiv.classList.add("datediv");
        dateDiv.innerText = dateCount;
        dateDiv.style.height = "14px";
        td.appendChild(dateDiv);

        //일정입력div 생성
        var scheduleDiv = document.createElement("div");
        scheduleDiv.classList.add("schedulediv" + dateCount);
        scheduleDiv.style.height = "36px";
        td.appendChild(scheduleDiv);

        // 일요일은 빨간색으로 표시
        if (col === 0) {
          td.classList.add("sunday");
        }
        // 토요일은 파란색으로 표시
        else if (col === 6) {
          td.classList.add("saturday");
        }

        tr.appendChild(td);
        dateCount++;
      }
    }

    calendarBody.appendChild(tr);
  }
}

// 달력 생성 함수 호출
generateCalendar();