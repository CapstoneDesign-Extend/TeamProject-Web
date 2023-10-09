$(document).ready(function() {
    // 현재 페이지의 게시글 ID를 가져옴
    let boardId = $('input[name="boardId"]').val();

    function addCommentToDOM(comment) {
        //let formattedTime = moment(comment.creationTime).fromNow(); // 여기서 시간을 포맷팅합니다.
        let commentHTML = `
            <div class="comment_div">
                <div class="comment_top">
                    <img src="/tmp.jpg" alt="" class="comment_profile_pic">
                    <div class="textspace">
                        <div class="name">${comment.authorName}</div>
                        <div class="likey_report">
                            <div class="likey" data-comment-id="${comment.id}">
                                <a href="javascript:void(0)" data-comment-id="${comment.id}" class="comment-like">좋아요</a>
                            </div>
                            <div class="report">
                                <a>신고</a>
                            </div>
                            <div class="delete" data-comment-id="${comment.id}">
                                <input type="hidden" name="_method" value="DELETE"/>
                                <a href="javascript:void(0)" data-comment-id="${comment.id}" class="comment-delete">삭제</a>
                            </div>
                         </div>
                    </div>
                </div>
                <div class="comment_body">
                    <div class="comment_main">${comment.content}</div>
                </div>
                <div class="time">
                    <div class="time_text">${comment.formattedFinalDate}</div>
                    <div class="comment_likey_count">
                        <span id="" class="material-icons-outlined" style="color:#b63826;">favorite_border</span>
                        <span class="recom_count2">${comment.likeCount}</span>
                    </div>
                </div>
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
                console.log(response);  // 이 부분 추가
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

    $('.comment-list').on('click', '.comment-delete', function() {
        let commentId = $(this).data('comment-id');
        deleteComment(commentId, $(this).closest('.comment_div'));
    });

    function deleteComment(commentId, commentDiv) {
        if(!confirm("정말로 삭제하시겠습니까?")) {
            return;  // 사용자가 취소를 선택하면 함수를 종료합니다.
        }
        $.ajax({
            type: 'DELETE',
            url: '/comment/delete/' + commentId,
            success: function(response) {
                console.log(response);
                console.log(commentDiv);
                commentDiv.remove();  // 해당 댓글 DOM 제거
                alert(response.message);      // '댓글이 성공적으로 삭제되었습니다.' 메시지 표시

                // 댓글 카운트 업데이트
                $('.comment_count').text(response.chatCnt);
            },
            error: function(error) {
                alert(error.responseText);
            }
        });
    }
    // 새로 작성된 댓글의 좋아요 버튼 클릭 이벤트 (이벤트 위임 사용)
    $('.comment-list').on('click', '.comment-like', function() {
        let commentId = $(this).data('comment-id');
        let memberId = $('#loggedInUser').data('member-id');

        let that = this;

        $.ajax({
            type: 'POST',
            url: `/like/comment/${commentId}/member/${memberId}`,
            success: function(response) {
                // 응답으로 받은 현재의 좋아요 횟수로 화면 업데이트
                $(that).closest('.comment_div').find('.recom_count2').text(response.likeCnt);

            },
            error: function(error) {
                alert("좋아요 변경에 실패했습니다.");
            }
        });
    });
});