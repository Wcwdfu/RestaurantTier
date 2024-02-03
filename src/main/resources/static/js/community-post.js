document.addEventListener('DOMContentLoaded', function () {

    // 댓글 작성
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
                console.log(response)
                if (response.ok) {
                    if (response.redirected) {
                        window.location.href = "/user/login";
                    } else {
                        window.location.reload();

                    }


                }
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
                .then(response => {
                    if (response.ok) {
                        if (response.redirected) {
                            window.location.href = "/user/login";
                        } else {
                            window.location.reload();

                        }


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
                .then(response => {
                    if (response.ok) {
                        if (response.redirected) {
                            window.location.href = "/user/login";
                        } else {
                            window.location.reload();

                        }


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
                .then(response => {
                    if (response.ok) {
                        if (response.redirected) {
                            window.location.href = "/user/login";
                        } else {
                            window.location.reload();

                        }


                    }
                }).catch(error =>
                alert("Network error or problem with the request"))

        }
    )

    // 댓글 싫어요 버튼 리스너
    document.querySelectorAll('.comment-up').forEach(button => {
        event.preventDefault()

        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-id'); // data-id 속성에서 댓글 ID 가져오기
            fetch(`/api/comment/like/${commentId}`, { // 서버에 GET 요청 보내기
                method: 'GET'
            })
                .then(response => {
                    if (response.ok) {
                        if (response.redirected) {
                            window.location.href = "/user/login";
                        } else {
                            window.location.reload();

                        }


                    }

                })
                .catch(error => console.error('Error:', error));
        });
    });

    // 댓글 싫어요 버튼 리스너
    document.querySelectorAll('.comment-down').forEach(button => {
        event.preventDefault()
        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-id');
            fetch(`/api/comment/dislike/${commentId}`, {
                method: 'GET'
            })
                .then(response => {
                    if (response.ok) {
                        if (response.redirected) {
                            window.location.href = "/user/login";
                        } else {
                            window.location.reload();

                        }

                    }
                })
                .catch(error => console.error('Error:', error));
        });
    });


})