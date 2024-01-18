$(document).ready(function () {

    // 종류와 상황 버튼에 맞는 음식점들 나열 : 버튼에 리스너 추가
    $('.unselected.button').click(function() {
        // 버튼 클릭되면 모두 unselect으로 설정하고 클릭된 버튼만 selected으로 변경
        $('.button').removeClass('selected').addClass('unselected')
        $(this).removeClass('unselected').addClass('selected')

        var cuisine = $(this).text().trim(); // 버튼의 텍스트를 가져옵니다.
        if(cuisine==="전체")
            cuisine=""
        // AJAX 요청을 사용하여 서버에 데이터를 요청합니다.
        $.ajax({
            url: '/api/tier', // 요청을 보낼 URL
            type: 'GET', // HTTP 메소드
            data: { 'cuisine': cuisine }, // 요청과 함께 보낼 데이터
            success: function(data) {
                //반환한 데이터로 table 데이터 구성
                var tableContent = '';
                data.forEach(function(restaurant) {
                    var restaurantLink = '/restaurants/' + restaurant.restaurantId; // 링크 생성
                    tableContent += '<tr>' +
                        '<td>' + "0" + '</td>' +
                        '<td><a href="' + restaurantLink + '" class="restaurant-link">' + restaurant.restaurantName + '</a></td>' + // 링크 추가
                        '<td>' + "0" + '</td>' +
                        '<td>' + restaurant.restaurantCuisine + '</td>' +
                        '<td>' + "0" + '</td>' +
                        '<td>' + restaurant.restaurantType + '</td>' +
                        '</tr>';
                });
                $("#tierTableBody").html(tableContent);
                $("footer").html("")
            },

            error: function(error) {
                // 에러 핸들링
                console.error("Error fetching data: ", error);
            }
        });
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
