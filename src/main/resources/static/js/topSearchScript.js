const searchArea = document.getElementById('searchArea');
const backdrop = document.getElementById('searchBackdrop');
const mainSearchInput = document.getElementById('mainSearchInput');
const inputContainer = document.getElementById('inputContainer');
const body = document.getElementsByTagName('body')[0];
let isSearching = false;
mainSearchInput.addEventListener('focus', function() {
    inputContainer.classList.add('border-highlight');
})
mainSearchInput.addEventListener('blur', function() {
    inputContainer.classList.remove('border-highlight');
})

document.addEventListener('keydown', function(event) {
    // '/' 키가 눌린 경우
    if (event.key === '/') {
        event.preventDefault();
        openSearchWindow();
    }
});

// 뒤에 어두운 배경 누른 경우
backdrop.addEventListener('click', function() {
    closeSearchWindow();
})

// 검색창 열기
function openSearchWindow() {
    body.classList.add('prevent-scroll'); // 스크롤 막기
    isSearching = true;
    searchArea.style.display = 'block';
    mainSearchInput.click();
}
mainSearchInput.onclick = function() {mainSearchInput.focus();}

// 검색창 닫기
function closeSearchWindow() {
    body.classList.remove('prevent-scroll');
    isSearching = false;
    searchArea.style.display = 'none';
}

// 검색창 문자 지우기
document.getElementById('eraseButtonReal').addEventListener('click', function() {
    mainSearchInput.value = '';
    mainSearchInput.click();
})

// 검색하기
function search(searchInput) {
    console.log(searchInput);
    window.location.href = '/search?kw=' + searchInput;
}
document.addEventListener('keydown', function(event) {
    console.log(event.key);
    console.log(isSearching);
    if (isSearching && (event.key === 'Enter' || event.key === 'Return')) {
        search(mainSearchInput.value);
    }
})