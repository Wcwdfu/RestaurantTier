$(document).ready(function () {
    // --------------- 클릭된 종류 버튼 효과 ----------------------------
        // 현재 URL에서 쿼리 스트링 추출
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    let cuisineParam = urlParams.get('cuisine');
    if (!cuisineParam) cuisineParam = '전체';
    let situationParam = urlParams.get('situation');
    if (!situationParam) situationParam = '전체';

    document.querySelectorAll('.category').forEach(btn => {
        btn.addEventListener('click', function() {
            if (btn.dataset.cuisine) {
                var apiUrl = `/tier?cuisine=${btn.dataset.cuisine}&situation=${situationParam}`;
                console.log(apiUrl);
                window.location.href = apiUrl;
            } else if (btn.dataset.situation) {
                var apiUrl = `/tier?cuisine=${cuisineParam}&situation=${btn.dataset.situation}`;
                console.log(apiUrl);
                window.location.href = apiUrl;
            }
        })
    });

// 카테고리바를 눌렀을 때 접혔다 펼쳐졌다 하는 기능을 위한 부분
    document.getElementById('categoryCheckBtn').addEventListener('change', function () {
        const categoryCheckBtnArrow = document.getElementById('categoryCheckBtnArrow');
        const categoryCheckBtnText = document.getElementById('categoryCheckBtnText');
        const categoryList = document.getElementById('categoryList');
        if (this.checked) {
            categoryList.style.display = 'block';
            categoryCheckBtnText.textContent = '접기　';
            categoryCheckBtnArrow.textContent = '▼'
        } else {
            categoryList.style.display = 'none';
            categoryCheckBtnText.textContent = '펼치기　';
            categoryCheckBtnArrow.textContent = '◀'
        }
    });
// 카테고리바를 눌렀을 때 접혔다 펼쳐졌다 하는 기능을 위한 부분
// 두 개인 이유는 텍스트 부분을 누를때는 적용이 안돼서 따로 적용시키기 위함
    document.getElementById('categoryText').addEventListener('touchstart', function () {
        var categoryCheckBtn = document.getElementById('categoryCheckBtn');
        const categoryCheckBtnArrow = document.getElementById('categoryCheckBtnArrow');
        const categoryCheckBtnText = document.getElementById('categoryCheckBtnText');
        var categoryList = document.getElementById('categoryList');
        categoryCheckBtn.checked = !categoryCheckBtn.checked;
        if (categoryCheckBtn.checked) {
            categoryList.style.display = 'block';
            categoryCheckBtnText.textContent = '접기　';
            categoryCheckBtnArrow.textContent = '▼'
        } else {
            categoryList.style.display = 'none';
            categoryCheckBtnText.textContent = '펼치기　';
            categoryCheckBtnArrow.textContent = '◀'
        }
    });

// pc에서도 카테고리의 가로 스크롤을 마우스 드래그로 할 수 있게 해주는 부분
    var scrollableElements = document.querySelectorAll('.scrollable');
    scrollableElements.forEach(function (scrollableElement) {
        var isMouseDown = false;
        var startX, scrollLeft;

        scrollableElement.addEventListener('mousedown', function (e) {
            isMouseDown = true;
            startX = e.pageX - scrollableElement.offsetLeft;
            scrollLeft = scrollableElement.scrollLeft;
        });

        scrollableElement.addEventListener('mouseleave', function () {
            isMouseDown = false;
        });

        scrollableElement.addEventListener('mouseup', function () {
            isMouseDown = false;
        });

        scrollableElement.addEventListener('mousemove', function (e) {
            if (!isMouseDown) return;
            e.preventDefault();
            var x = e.pageX - scrollableElement.offsetLeft;
            var walk = (x - startX) * 1.5; // 스크롤 속도 조절을 위한 계수
            scrollableElement.scrollLeft = scrollLeft - walk;
        });
    });

// 아래로 스크롤 시 카테고리 창 상단 고정해주는 부분
    window.addEventListener('scroll', function () {
        var category = document.getElementById('categoryWindow');
        var headerHeight = document.querySelector('header').offsetHeight;

        if (window.scrollY >= headerHeight) {
            category.classList.add('fixed-category');
        } else {
            category.classList.remove('fixed-category');
        }
    });

});
