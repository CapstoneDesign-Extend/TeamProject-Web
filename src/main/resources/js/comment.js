// 문서가 준비되면 실행되는 함수
$(document).ready(function() {
    // 페이지 로드 시 댓글 목록 불러오기
    // 현재 페이지의 게시글 ID를 가져옴
    let boardId = $('input[name="boardId"]').val();

    // 페이지 로드 시 게시글에 해당하는 댓글 목록을 불러옴
    $.ajax({
        type: 'GET',
        url: '/comment?boardId=' + boardId,
        success: function(response) {
            // 기존 댓글들을 화면에서 제거
            $('.comment').empty();
            // 불러온 댓글들을 화면에 추가
            response.forEach(comment => {
                let commentHTML = `
                    <div class="comment_div">
                        <div class="comment_top">
                            <img src="/tmp.jpg" alt="" class="comment_profile_pic">
                            <div class="textspace">
                                <div class="name">${comment.author}</div>
                                <div class="likey_report">
                                    <div class="likey">좋아요</div>
                                    <div class="report">신고</div>
                                </div>
                            </div>
                        </div>
                        <div class="comment_body">
                            <div class="comment_main">${comment.content}</div>
                        </div>
                        <div class="time">${comment.creationTime}</div>
                    </div>
                `;
                $('.comment').append(commentHTML);
            });
        },
        error: function(error) {
            console.log(error);
        }
    });

    // 댓글 작성 버튼 클릭 시 실행되는 이벤트 핸들러
    $('.wrapsubmit').click(function(e) {
        // 폼 제출의 기본 동작을 막음 (페이지 리로딩 방지)
        e.preventDefault();

        // 입력된 댓글 내용과 익명 옵션 값을 가져옴
        let commentText = $('.comment_input_inner').val();
        let isAnonymous = $('.anonymous').is(':checked');

        // 댓글을 서버에 저장
        $.ajax({
            type: 'POST',
            url: '/comment',
            data: {
                content: commentText,
                isAnonymous: isAnonymous,
                boardId: boardId
            },
            success: function(response) {
                $('.comment').empty();
                response.forEach(comment => {
                    let commentHTML = `
                        <div class="comment_div">
                            <div class="comment_top">
                                <img src="/tmp.jpg" alt="" class="comment_profile_pic">
                                <div class="textspace">
                                    <div class="name">${comment.author}</div>
                                    <div class="likey_report">
                                        <div class="likey">좋아요</div>
                                        <div class="report">신고</div>
                                    </div>
                                </div>
                            </div>
                            <div class="comment_body">
                                <div class="comment_main">${comment.content}</div>
                            </div>
                            <div class="time">${comment.creationTime}</div>
                        </div>
                    `;
                    $('.comment').append(commentHTML);
                });
            },
            error: function(error) {
                console.log(error);
            }
        });
    });
});