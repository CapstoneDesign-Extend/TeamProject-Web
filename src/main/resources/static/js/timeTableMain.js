$(document).ready(function() {
    // 페이지 로딩 시 기존 일정 불러오기
    $.get('/rest/schedules', function(data) {
        console.log("Data received from server:", data);
        updateGrid(data);
    });

    function getPositionForTime(startTime, endTime) {
        var timeHeight = 44;  // 각 그리드 높이 (1시간당 44px라고 가정)

        const convertTime = (time) => {
            const hour = Math.floor(time / 100);
            const minute = time % 100;
            return hour + (minute / 60);
        };

        const startConverted = convertTime(startTime);
        const endConverted = convertTime(endTime);

        const top = Math.floor(timeHeight * (startConverted - 9)); // 9시부터 시작하기 때문에 -9
        const height = timeHeight * (endConverted - startConverted);

        console.log("Position calculated:", {top, height});
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

        // 수업 항목 생성 시 data-id 속성 추가
        gridItem.setAttribute("data-id", schedule.id);

        // 배경색을 랜덤하게 설정
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