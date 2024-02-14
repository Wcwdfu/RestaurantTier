var navbarTitles = document.querySelectorAll('.navbar-title');

// -------------navBar 선택 및 보이기 로직 -------------------
navbarTitles.forEach(function(navbarTitle, index) {
    navbarTitle.addEventListener('click', function() {
        // 선택된 탭에 해당하는 인덱스 계산 (인덱스는 0부터 시작하므로 +1 해줍니다)
        var selectedIndex = index + 1;

        // 모든 탭에 대해 selected 클래스를 제거
        navbarTitles.forEach(function(title) {
            title.classList.remove('selected');
        });

        // 클릭된 탭에 selected 클래스 추가
        navbarTitle.classList.add('selected');

        // 모든 contents 영역 숨기기
        var areas = document.querySelectorAll('#mainContents > div');
        areas.forEach(function(area) {
            area.classList.add('hidden');
        });

        // 선택된 contents 영역 보이기
        var selectedArea = document.getElementById('Area' + selectedIndex);
        if (selectedArea) {
            selectedArea.classList.remove('hidden');
        }
    });
});

// -------------navBar 드래그로 옮기기 로직-------------------
var navbar = document.getElementById('navBar');

var isDragging = false;
var startX;
var scrollLeft;

navbar.addEventListener('touchstart', function(e) {
    isDragging = true;
    startX = e.touches[0].clientX - navbar.offsetLeft;
    scrollLeft = navbar.scrollLeft;
});

navbar.addEventListener('touchend', function() {
    isDragging = false;
});

navbar.addEventListener('touchmove', function(e) {
    if (!isDragging) return;
    e.preventDefault();
    var x = e.touches[0].clientX - navbar.offsetLeft;
    var walk = x - startX;
    navbar.scrollLeft = scrollLeft - walk;
});

// ------------- 내정보(Area1) nickname바꾸기 로직 -------------------
document.getElementById('saveBtn').addEventListener('click',function (){
    var newNickname = document.getElementById("nickname").value; // 해당 input 요소의 값을 가져옴

    // 서버로 전송
    fetch("/user/api/myPage/setNickname", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ newNickname: newNickname }), // JSON 형태로 데이터 전송
    })
        .then(response => {
            if (response.ok) {
                // 변경 성공 시 동작할 코드
                alert("닉네임 변경 성공")
                console.log("닉네임 변경 성공");
            } else {
                // 변경 실패 시 동작할 코드
                alert("닉네임 변경 실패")
                console.error("닉네임 변경 실패");
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
})


