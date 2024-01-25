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
})