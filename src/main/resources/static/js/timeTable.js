$(document).ready(function() {
    // 페이지 로딩 시 기존 일정 불러오기
    $.get('/rest/schedules', function(data) {
        updateGrid(data);
    });

    $('#customsubjects').on('submit', function(event) {
        event.preventDefault(); // 기본 submit 이벤트 중지

        const formData = {
            classTitle: $('input[name="subject_name"]').val(),
            professorName: $('input[name="professor"]').val(),
            classPlace: $('.classPlace').val(),
            day: $('#weeks .active').index(),
            hourStart: parseInt($('.starthour').val()),
            minuteStart: parseInt($('.startminute').val()),
            hourEnd: parseInt($('.endhour').val()),
            minuteEnd: parseInt($('.endminute').val())
        };

        $.ajax({
            url: '/rest/schedules',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                console.log(formData);
                console.log(response);
                updateGrid([response]);
            },
            error: function() {
                alert("서버와의 통신 중 오류가 발생하였습니다. 다시 시도해주세요.");
            }
        });
    });

    function getPositionForTime(startTime, endTime) {
        var timeHeight = 60;  // 각 그리드 높이 (1시간당 60px라고 가정)

        const convertTime = (time) => {
            const hour = Math.floor(time / 100);
            const minute = time % 100;
            return hour + (minute / 60);
        };

        const startConverted = convertTime(startTime);
        const endConverted = convertTime(endTime);

        const top = Math.floor(timeHeight * (startConverted - 9)); // 9시부터 시작하기 때문에 -9
        const height = timeHeight * (endConverted - startConverted);

        return {
            height: height,
            top: top
        };
    }

    function updateGrid(data) {
        data.forEach(schedule => {
            appendToCorrectGrid(schedule);
        });
    }

    function getRandomPastelColor() {
        let hue = Math.floor(Math.random() * 360);
        return `hsl(${hue}, 100%, 85%)`;  // hsl로 연한 색상 생성
    }

    function appendToCorrectGrid(schedule) {
        const day = schedule.day;

        if (day < 0 || day > 6) {
            console.error("Invalid day value:", day);
            return;
        }

        // 해당 요일의 td를 직접 찾습니다.
        const tdElement = document.querySelector(`.td${day}`);
        const position = getPositionForTime(schedule.startTime, schedule.endTime);

        const gridItem = document.createElement("div");
        gridItem.classList.add("schedule-item");
        gridItem.style.top = `${position.top}px`;
        gridItem.style.height = `${position.height}px`;

        // 여기서 배경색을 랜덤하게 설정합니다.
        const randomColor = getRandomPastelColor();
        gridItem.style.backgroundColor = randomColor;

        gridItem.innerText = `${schedule.classTitle}\n${schedule.professorName}\n${schedule.classPlace}`;

        const targetGridIndex = Math.floor(position.top / 60);
        const targetGridElement = tdElement.querySelectorAll('.grid')[targetGridIndex];

        if (targetGridElement) {
            tdElement.insertBefore(gridItem, targetGridElement);
        } else {
            tdElement.appendChild(gridItem);
        }
    }
});
