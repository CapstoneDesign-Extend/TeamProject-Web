$(document).ready(function() {
    // 현재 페이지의 게시글 ID를 가져옴
    let boardId = $('input[name="boardId"]').val();

    function addCommentToDOM(comment) {
        let formattedTime = moment(comment.creationTime).fromNow(); // 여기서 시간을 포맷팅합니다.
        let commentHTML = `
            <div class="comment_div">
                <div class="comment_top">
                    <img src="/tmp.jpg" alt="" class="comment_profile_pic">
                    <div class="textspace">
                        <div class="name">${comment.authorName}</div>
                        <div class="likey_report">
                             <div class="likey">
                                 <a>좋아요</a>
                             </div>
                             <div class="report">
                                 <a>신고</a>
                             </div>
                             <div class="delete">
                                 <a>삭제</a>
                             </div>
                         </div>
                    </div>
                </div>
                <div class="comment_body">
                    <div class="comment_main">${comment.content}</div>
                </div>
                <div class="time">${formattedTime}</div>
            </div>
        `;
        $('.comment-list').append(commentHTML);
    }

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
                addCommentToDOM(response);
                $('.comment_input_inner').val('');  // 댓글 입력란 초기화

                // chatCnt 값으로 댓글 카운트 업데이트
                $('.comment_count').text(response.chatCnt);
            },
            error: function(error) {
                console.log(error);
                alert("댓글 저장에 실패했습니다.");
            }
        });
    });
});