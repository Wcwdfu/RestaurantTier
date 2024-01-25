document.addEventListener('DOMContentLoaded', function () {
    // 뒤로 가기 버튼에 이벤트 리스너 추가
    var backButton = document.getElementById('back-button');
    if (backButton) {
        backButton.addEventListener('click', function () {
            window.history.back();
        });
    }

    // 작성 완료 버튼에 이벤트 리스너 추가 (추가적인 로직이 필요하다면 여기에 작성)
    var completeButton = document.getElementById('complete-button');
    if (completeButton) {
        completeButton.addEventListener('click', function () {
            // 작성 완료 관련 로직
        });
    }

    var form = document.querySelector('form');
    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 방지
        var category = form.querySelector('select[name="category"]').value;
        if (!category) {
            alert('카테고리를 선택해주세요.');
            return;
        }
        var formData = new FormData(form);
        fetch('/api/community/post/create', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (!response.ok) {
                throw new Error("로그인이 되지 않았습니다");
            }
            return response;
        })
            .then(data => {
                console.log(data);
                window.location.href="/community"
            }).catch(error => {
            alert(error.message)
            console.error('Error:', error);
            window.location.href="/community"

        });
    });


});
