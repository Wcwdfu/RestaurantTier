document.addEventListener('DOMContentLoaded', function () {

    // 뒤로 가기 버튼에 이벤트 리스너 추가
    var backButton = document.getElementById('back-button');
    if (backButton) {
        backButton.addEventListener('click', function () {
            window.history.back();
        });
    }
    
    // 작성 완료 버튼 클릭 리스너
    var form = document.querySelector('form');
    form.addEventListener('submit', function (event) {
        var category = form.querySelector('select[name="postCategory"]').value;
        if (!category) {
            alert('카테고리를 선택해주세요.');
            return;
        }
        const formData = new FormData(form);
        fetch('/api/community/post/create', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.redirected)
                window.location.href = "/user/login";
            return response;
        })
            .then(data => {
                window.location.href = "/community"
            }).catch(error => {
            alert(error.message)
            console.error('Error:', error);
            window.location.href = "/community"

        });
    });


});


function openImageDialog() {
    document.getElementById('imageInput').click();
}

function insertImage() {
    var file = document.getElementById('imageInput').files[0];
    if (!file) {
        console.log("No file selected");
        return;
    }

    var reader = new FileReader();
    reader.onloadend = function () {
        var base64Image = reader.result;
        console.log("Image Base64:", base64Image); // Check if the image is read correctly
        var textArea = document.querySelector('.post-content');
        textArea.value += '\n[img]' + base64Image + '[/img]\n'; // Append instead of replace
    };

    reader.onerror = function (error) {
        console.log("Error reading file:", error);
    };

    reader.readAsDataURL(file);
}
