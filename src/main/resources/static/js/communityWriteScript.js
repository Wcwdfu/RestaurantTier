document.addEventListener('DOMContentLoaded', function () {
    // 드롭다운
    var dropdownItems = document.querySelectorAll('.dropdown-item');

    // 각 드롭다운 메뉴 항목에 클릭 이벤트 리스너를 추가합니다.
    dropdownItems.forEach(function (item) {
        item.addEventListener('click', function () {
            // 현재 활성화된 항목에서 'active' 클래스를 제거합니다.
            var currentActive = document.querySelector('.dropdown-item.active');
            if (currentActive) {
                currentActive.classList.remove('active');
            }

            // 클릭된 항목에 'active' 클래스를 추가합니다.
            this.classList.add('active');

            // 선택된 카테고리의 텍스트를 버튼에 표시합니다.
            var dropdownButton = document.getElementById('dropdownMenuButton').querySelector('span');
            dropdownButton.textContent = this.textContent;
        });
    })

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
        console.log(category)
        const formData = new FormData(form);
        console.log(formData.get("postCategory"))
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
