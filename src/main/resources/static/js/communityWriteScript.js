
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

    var tinyEditor = tinymce.init({
        selector: ".tiny-editor",
        min_height: 500,
        max_height: 3000,
        menubar: false,
        paste_as_text: true,
        fullpage_default_font_size: "14px",
        branding: false,
        plugins: "autolink code link autoresize paste contextmenu image preview",
        toolbar: "undo redo | fontsizeselect | forecolor | bold italic strikethrough underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link custom_image | code preview",
        fontsize_formats: '10px 12px 14px 16px 18px 20px 22px 24px 28px 32px 36px 48px',
        setup: function(editor) {
            editor.ui.registry.addButton('custom_image', {
                icon: 'image',
                tooltip: 'insert Image',
                onAction: function () {
                    documentUpload({
                        multiple: false,
                        accept: '.jpg, .png',
                        callback: function (data) {
                            if (data.rs_st === 0) {
                                var fileInfo = data.rs_data;
                                tinymce.execCommand('mceInsertContent', false,
                                    /**
                                     "<img src='" + fileInfo.fullPath + "' data-mce-src='" + fileInfo.fullPath + "' data-originalFileName='" + fileInfo.orgFilename + "' >");
                                     **/
                                    "<img src='" + fileInfo.thumbnailPath + "' data-mce-src='" + fileInfo.thumbnailPath + "' data-originalFileName='" + fileInfo.orgFilename + "' >");
                            }
                        }
                    });
                }
            });
        }
    });
    function documentUpload(options) {
        // 입력: options 객체, 내부에는 multiple, accept, callback 함수가 포함됨
        var input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', options.accept);
        if (options.multiple) {
            input.setAttribute('multiple', 'multiple');
        }

        input.onchange = function() {
            var files = this.files;
            var formData = new FormData();
            formData.append('image', files[0]); // 첫 번째 선택된 파일만 처리

            // Fetch API를 사용하여 서버로 파일을 비동기로 전송
            fetch('/api/upload/image', {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    if (typeof options.callback === 'function') {
                        options.callback(data); // 서버로부터 받은 응답을 callback 함수에 전달
                    }
                })
                .catch(error => console.error('Error:', error));
        };

        input.click(); // 사용자가 파일을 선택할 수 있게 파일 입력 창을 열음
    }



});



