$(document).on('click', '.body_button', function() {
    const button = $(this); // 이 부분 추가
    const boardId = button.data('board-id');
    const memberId = $('#loggedInUser').data('member-id');

    console.log("Button clicked!");

    $.ajax({
        url: `/like/board/${boardId}/member/${memberId}`,
        type: 'POST',
        success: function(response) {
            // 좋아요 카운트 업데이트
            $('.recom_count').text(response.likeCnt);
            button.find('.recom_count').text(response.likeCnt); // 수정된 부분
            // 아이콘 색상 변경 (이미 좋아요가 눌러져 있다면 회색으로, 아니면 빨간색으로)
            const icon = button.find(".material-icons-outlined");
            if (response.liked) {
                icon.css("color", "#b63826");
            } else {
                icon.css("color", "black");
            }
        },
        error: function(error) {
            alert('좋아요 처리에 실패했습니다.');
        }
    });
});