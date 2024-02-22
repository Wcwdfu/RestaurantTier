
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
        var category = form.querySelector('select[name="postCategory"]').value;
        if (!category) {
            alert('카테고리를 선택해주세요.');
            return;
        }

        var formData = new FormData(form);
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


});


function openImageDialog(){
    document.getElementById('imageInput').click();
}

function insertImage() {
    var file = document.getElementById('imageInput').files[0];
    if (!file) {
        console.log("No file selected");
        return;
    }

    var reader = new FileReader();
    reader.onloadend = function() {
        var base64Image = reader.result;
        console.log("Image Base64:", base64Image); // Check if the image is read correctly
        var textArea = document.querySelector('.post-content');
        textArea.value += '\n[img]' + base64Image + '[/img]\n'; // Append instead of replace
    };

    reader.onerror = function(error) {
        console.log("Error reading file:", error);
    };

    reader.readAsDataURL(file);
}
