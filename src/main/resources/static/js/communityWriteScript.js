
document.addEventListener('DOMContentLoaded', function () {
    // 뒤로 가기 버튼에 이벤트 리스너 추가
    var backButton = document.getElementById('back-button');
    if (backButton) {
        backButton.addEventListener('click', function () {
            window.history.back();
        });
    }

    // 작성 완료 버튼 리스너 추가 (추가적인 로직이 필요하다면 여기에 작성)
    var form = document.querySelector('form');
    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 방지
        var category = form.querySelector('select[name="postCategory"]').value;
        if (!category) {
            alert('카테고리를 선택해주세요.');
            return;
        }

        var formData = new FormData(form);
        console.log(formData.image)
        fetch('/api/community/post/create', {
            method: 'POST',
            body: formData
        }).then(response => {
            if(response.redirected)
                window.location.href = "/user/login";
            return response;
        })
            .then(data => {
                window.location.href="/community"
            }).catch(error => {
            alert(error.message)
            console.error('Error:', error);
            window.location.href="/community"

        });
    });

    tinymce.init({
        selector: '.toolbar',  // 적용할 요소 선택
        plugins: 'image',  // 이미지 플러그인 활성화
        toolbar: 'image',  // 이미지 툴바 추가
        images_upload_url: '/api/upload/image',  // 이미지를 업로드할 서버의 엔드포인트
        // 서버로부터 반환된 이미지 URL을 편집기에 삽입하는 처리
        images_upload_handler: function (blobInfo, success, failure) {
            var formData = new FormData();
            formData.append('image', blobInfo.blob(), blobInfo.filename());

            fetch('/api/upload/image', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    console.log("json 변환 전")

                    return response.json();
                })
                .then(result => {
                    console.log("json 변환 후")
                    console.log(result)

                    success(result.fileUrl); // 'result.fileUrl'는 서버로부터 반환된 이미지 URL
                })
                .catch(error => {
                    failure(error.message); // 오류 처리
                });
        }

    });


});



