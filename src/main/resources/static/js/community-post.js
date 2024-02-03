document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('comment-form').addEventListener('submit', function (event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 방지
        // 현재 주소에서 postId 추출
        var postId = window.location.pathname.split('/').pop();

        var formData = new FormData(this);
        formData.append('postId', postId);

        fetch(this.action, {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("로그인이 되지 않았습니다");
                }
                window.location.reload();
            })
            .catch(error => {
                alert(error.message)
                console.error('Error:', error);
            });
    });
    document.getElementById("likeButton").addEventListener('click',
        function (event) {

            event.preventDefault()
            var postId = this.dataset.postId;
            fetch("/api/post/like?postId=" + postId, {
                method: 'GET'
            })
                .then(reponse => {
                    if (!reponse.ok) {
                        if (reponse.status === 401) {
                            alert("로그인이 되지 않았습니다");
                        } else {
                            reponse.text().then(text => {
                                alert("Error:" + text);
                            })
                        }
                    } else {
                        console.log("좋아요 생성 완료")
                        window.location.reload();
                    }
                }).catch(error =>
                alert("Network error or problem with the request"))

        }
    )
    document.getElementById("dislikeButton").addEventListener('click',
        function (event) {

            event.preventDefault()
            var postId = this.dataset.postId;
            fetch("/api/post/dislike?postId=" + postId, {
                method: 'GET'
            })
                .then(reponse => {
                    if (!reponse.ok) {
                        if (reponse.status === 401) {
                            alert("로그인이 되지 않았습니다");
                        } else {
                            reponse.text().then(text => {
                                alert("Error:" + text);
                            })
                        }
                    } else {
                        console.log("싫어요 생성 완료")
                        window.location.reload();
                    }
                }).catch(error =>
                alert("Network error or problem with the request"))

        }
    )
    document.getElementById("scrap").addEventListener('click',

        function (event) {

            event.preventDefault()
            var postId = this.dataset.postId;
            fetch("/api/post/scrap?postId=" + postId, {
                method: 'GET'
            })
                .then(reponse => {
                    if (!reponse.ok) {
                        if (reponse.status === 401) {
                            alert("로그인이 되지 않았습니다");
                        } else {
                            reponse.text().then(text => {
                                alert("Error:" + text);
                            })
                        }
                    } else {
                        console.log("스크랩 처리 완료")
                        window.location.reload();
                    }
                }).catch(error =>
                alert("Network error or problem with the request"))

        }
    )

    document.querySelectorAll('.comment-up').forEach(button => {
        event.preventDefault()

        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-id'); // data-id 속성에서 댓글 ID 가져오기
            fetch(`/api/comment/like/${commentId}`, { // 서버에 GET 요청 보내기
                method: 'GET'
            })
                .then(response => {
                    if (response.ok) {
                        this.innerHTML = '<img src="/img/community/up-green.png">'; // 버튼 이미지를 초록색으로 변경
                        const span = this.parentNode.querySelector('span');
                        let likeCount = parseInt(span.textContent, 10);
                        likeCount += 1;
                        span.textContent = likeCount;

                    }

                })
                .catch(error => console.error('Error:', error));
        });
    });

    // 'comment-down' 버튼에 대한 이벤트 리스너 추가
    document.querySelectorAll('.comment-down').forEach(button => {
        event.preventDefault()
        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-id');
            fetch(`/api/comment/dislike/${commentId}`, {
                method: 'GET'
            })
                .then(response => {
                    if (response.ok) {
                        this.innerHTML = '<img src="/img/community/down-red.png">'; // 버튼 이미지를 빨간색으로 변경
                        const span = this.parentNode.querySelector('span');
                        let likeCount = parseInt(span.textContent, 10);
                        likeCount -= 1;
                        span.textContent = likeCount;

                    }
                })
                .catch(error => console.error('Error:', error));
        });
    });


})