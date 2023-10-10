// 페이지가 로드될 때 기존 일정 불러오기
document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/schedules')
        .then(response => response.json())
        .then(data => {
            // 여기에 HTML에 일정을 채우는 코드를 작성
            // 예:
            // data.forEach(schedule => {
            //   const scheduleElement = document.createElement('a');
            //   scheduleElement.textContent = schedule.name;
            //   document.querySelector('.addschedule').appendChild(scheduleElement);
            // });
        });
});

// 새로운 일정 추가
document.querySelector('#scheduleForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const formData = new FormData(e.target);
    const scheduleData = {
        classTitle: formData.get('classTitle'),
        classPlace: formData.get('classPlace'),
        professorName: formData.get('professorName'),
        day: parseInt(formData.get('day')),
        hourStart: parseInt(formData.get('hourStart')),
        minuteStart: parseInt(formData.get('minuteStart')),
        hourEnd: parseInt(formData.get('hourEnd')),
        minuteEnd: parseInt(formData.get('minuteEnd'))
    };

    fetch('/api/schedules', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(scheduleData)
    })
        .then(response => response.json())
        .then(data => {
            // 여기에서 응답 처리
            // 예: 새로운 일정을 반영하여 UI를 업데이트하거나, 성공 메시지를 표시하기 등
        });
});






