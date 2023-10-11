$(document).ready(function() {
    // 페이지 로딩 시 기존 일정 불러오기
    $.get('/rest/schedules', function(data) {
        console.log("Data received from server:", data);
        updateGrid(data);
    });

    $('#customsubjects').on('submit', function(event) {
        event.preventDefault();

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

        const isEditing = $('input[name="id"]').val().length > 0;

        if (isEditing) {
            const id = $('input[name="id"]').val();
            $.ajax({
                url: `/rest/schedules/${id}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response) {
                    console.log(response);
                    updateGrid([response]);
                },
                error: function() {
                    alert("서버와의 통신 중 오류가 발생하였습니다. 다시 시도해주세요.");
                }
            });
        } else {
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
        }
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
        gridItem.setAttribute("data-id", schedule.id); // 여기서 id를 추가


        // 여기서 배경색을 랜덤하게 설정합니다.
        const randomColor = getRandomPastelColor();
        gridItem.style.backgroundColor = randomColor;

        gridItem.innerText = `${schedule.classTitle}\n${schedule.professorName}\n${schedule.classPlace}`;

        // 수정 버튼
        const editBtn = document.createElement("button");
        editBtn.classList.add("btn-edit");
        editBtn.onclick = function() {
            editSchedule(schedule);
        };
        gridItem.appendChild(editBtn);

        // 삭제 버튼
        const deleteBtn = document.createElement("button");
        deleteBtn.classList.add("btn-delete");
        deleteBtn.onclick = function() {
            deleteSchedule(schedule);
        };
        gridItem.appendChild(deleteBtn);

        const targetGridIndex = Math.floor(position.top / 60);
        const targetGridElement = tdElement.querySelectorAll('.grid')[targetGridIndex];

        if (targetGridElement) {
            tdElement.insertBefore(gridItem, targetGridElement);
        } else {
            tdElement.appendChild(gridItem);
        }
    }

    function editSchedule(schedule) {
        console.log("Editing schedule:", schedule);
        $('input[name="id"]').val(schedule.id);
        $('input[name="subject_name"]').val(schedule.classTitle);
        $('input[name="professor"]').val(schedule.professorName);
        $('.classPlace').val(schedule.classPlace);
        $('#weeks li').removeClass('active').eq(schedule.day).addClass('active');
        $('.starthour').val(Math.floor(schedule.startTime / 100));
        $('.startminute').val(schedule.startTime % 100);
        $('.endhour').val(Math.floor(schedule.endTime / 100));
        $('.endminute').val(schedule.endTime % 100);

        // form의 제목을 수정
        $('#customsubjects .title').text("수업 수정하기");
        // form을 보이게 합니다.
        $('#customsubjects').show();
        console.log("Form title should be changed!");
    }

    function deleteSchedule(schedule) {
        $.ajax({
            url: `/rest/schedules/${schedule.id}`,
            type: 'DELETE',
            success: function() {
                // 성공적으로 삭제되면 해당 항목을 클라이언트에서 제거
                document.querySelector(`.schedule-item[data-id='${schedule.id}']`).remove();
            },
            error: function() {
                alert("삭제 실패. 다시 시도해주세요.");
            }
        });
    }

    let initialWeekState = []; // 초기 요일 상태 저장
    $('#weeks li').each(function(index, li) {
        initialWeekState.push($(li).hasClass('active'));
    });


    function resetForm() {
        $('#customsubjects .title').text("새 수업 추가");
        $('input[name="id"]').val('');
        $('input[name="subject_name"]').val('');
        $('input[name="professor"]').val('');
        $('.classPlace').val('');
        // 요일 상태 복원
        $('#weeks li').each(function(index, li) {
            if (initialWeekState[index]) {
                $(li).addClass('active');
            } else {
                $(li).removeClass('active');
            }
        });
        // 시작 시간을 첫 번째 값으로 설정
        $('.starthour').prop('selectedIndex', 0);
        $('.startminute').prop('selectedIndex', 0);

        // 시작 시간보다 한 시간 뒤로 종료 시간 설정
        let startHourIndex = $('.starthour').prop('selectedIndex');
        let endHourIndex = startHourIndex + 1;

        // 만약 endHourIndex가 옵션의 범위를 벗어나면 범위 내로 조정
        if (endHourIndex >= $('.endhour option').length) {
            endHourIndex = $('.endhour option').length - 1;
        }

        $('.endhour').prop('selectedIndex', endHourIndex);
        $('.endminute').prop('selectedIndex', 0);
        $('.endhour').prop('selectedIndex', 0);
        $('.endminute').prop('selectedIndex', 0);
        $('#customsubjects').show();
    }
    $('#custom_button').on('click', function() {
        resetForm();
    });

    // Vanilla JS 코드 부분
    const custom_button = document.getElementById('custom_button');
    const customsubjects = document.getElementById('customsubjects');
    const xbutton = document.getElementById('xbutton');

    custom_button.addEventListener('click', function(){
        custom_button.style.display = 'none';
        customsubjects.style.display = 'block';
    });

    xbutton.addEventListener('mouseover', function(){
        xbutton.style.backgroundColor = 'red';
    });

    xbutton.addEventListener('mouseleave', function(){
        xbutton.style.backgroundColor = 'gray';
    });

    xbutton.addEventListener('click', function(){
        custom_button.style.display = 'block';
        customsubjects.style.display = 'none';
    });

});
