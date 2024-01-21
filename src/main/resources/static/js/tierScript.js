$(document).ready(function () {
    // --------------- 클릭된 종류 버튼 효과 ----------------------------
        // 현재 URL에서 쿼리 스트링 추출
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const cuisineParam = urlParams.get('cuisine');

        // 모든 버튼 클래스 초기화
    document.querySelectorAll('.button').forEach(btn => {
        btn.classList.remove('selected');
        btn.classList.add('unselected');
    });

        // 해당하는 cuisine의 a 태그 찾기
    document.querySelectorAll('.cuisine-link').forEach(link => {
        if (link.textContent.trim() === cuisineParam) {
            // a 태그의 부모 span 태그에 클래스 변경
            link.parentElement.classList.add('selected');
            link.parentElement.classList.remove('unselected');
        }
    });



// 데이터 위치
    const imagePaths = {
        person: '/tier-images/person.png',
        people: '/tier-images/people.png',
        crowd: '/tier-images/crowd.png',
        date: '/tier-images/date.png',
        friend: '/tier-images/friend.png',
        blindDate: '/tier-images/blind-date.png',
        meeting: '/tier-images/meeting.png'
    };
// 임시 데이터의 텍스트를 매핑해줄 map
    const imageMappings = {
        '혼밥': 'person',
        '소규모 회식': 'people',
        '단체 회식': 'people',
        '데이트': 'date',
        '친구 초대': 'friend',
        '소개팅': 'blindDate',
        '미팅': 'meeting',
    };
// table에 내용을 채우는 함수 실행(정의는 아래쪽에 있음)
//     fillTableWithData(data)

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
            var walk = (x - startX) * 2; // 스크롤 속도 조절을 위한 계수
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

//표 데이터 채우기
    function createImage(imageKey) { // 일단 이미지 태그 생성해서 리턴 하는 함수
        const imageName = imageMappings[imageKey];
        const imagePath = imagePaths[imageName];

        const img = document.createElement('img');
        img.src = imagePath;
        return img;
    }

    function fillTableWithData(data) { // 표 채우는 함수
        const tableBody = document.getElementById('tierTableBody');

        data.forEach(rowData => {
            const tr = document.createElement('tr');

            for (let i = 0; i < rowData.length - 1; i++) {
                const cellData = rowData[i];
                const td = document.createElement('td');
                if (i == 1) {
                    td.classList.add('left-align');
                } else {
                    td.classList.add('middle-align');
                }
                td.textContent = cellData;
                tr.appendChild(td);
            }

            const td = document.createElement('td');
            const div = document.createElement('div');
            td.appendChild(div);
            const cellData = rowData[rowData.length - 1];
            const categories = cellData.split(',');

            categories.forEach(item => {
                div.appendChild(createImage(item.trim()));
                tr.appendChild(td);
            });

            tableBody.appendChild(tr);
        });
    }

});
