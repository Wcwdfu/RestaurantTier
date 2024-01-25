document.addEventListener('DOMContentLoaded', function() {
    // 뒤로 가기 버튼에 이벤트 리스너 추가
    var backButton = document.getElementById('back-button');
    if (backButton) {
        backButton.addEventListener('click', function() {
            window.history.back();
        });
    }

    // 작성 완료 버튼에 이벤트 리스너 추가 (추가적인 로직이 필요하다면 여기에 작성)
    var completeButton = document.getElementById('complete-button');
    if (completeButton) {
        completeButton.addEventListener('click', function() {
            // 작성 완료 관련 로직
        });
    }
});
