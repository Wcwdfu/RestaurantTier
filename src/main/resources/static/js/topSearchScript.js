const searchArea = document.getElementById('searchArea');
const backdrop = document.getElementById('searchBackdrop');
const mainSearchInput = document.getElementById('mainSearchInput');

document.addEventListener('keydown', function(event) {
    // '/' 키가 눌린 경우
    if (event.key === '/') {
        openSearchWindow();
    }
});

// 뒤에 어두운 배경 누른 경우
backdrop.addEventListener('click', function() {
    closeSearchWindow();
})

// 검색창 열기
function openSearchWindow() {
    searchArea.style.display = 'block';
    mainSearchInput.click();
}
mainSearchInput.onclick = function() {mainSearchInput.focus();}

// 검색창 닫기
function closeSearchWindow() {
    searchArea.style.display = 'none';
}

// 검색창 문자 지우기
document.getElementById('eraseButtonReal').addEventListener('click', function() {
    mainSearchInput.value = '';
    mainSearchInput.click();
})