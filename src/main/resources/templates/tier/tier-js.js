var isMobile = /Mobi/i.test(window.navigator.userAgent);

window.onload = adjustCategoryWidth;
window.onresize = adjustCategoryWidth;

function adjustCategoryWidth() {
    var categoryBackground = document.getElementById('categoryBackground');
    var tierChart = document.getElementById('tierChartMain');
    if (isMobile) {
        categoryBackground.style.marginLeft = '10px';
        categoryBackground.style.marginRight = '10px';
        tierChart.style.marginLeft = '10px';
        tierChart.style.marginRight = '10px';

        categoryBackground.style.minWidth = '250px';
        tierChart.style.minWidth = '250px';
    } else {
        categoryBackground.parentElement.style.display = 'flex';
        categoryBackground.style.width = '70%';
        tierChart.style.width = '70%';

        categoryBackground.style.minWidth = '300px';
        tierChart.style.minWidth = '300px';
        categoryBackground.style.maxWidth = '900px';
        tierChart.style.maxWidth = '900px';
    }
}

const data = [
    [1, '가가가가', 1, '소규모 회식, 데이트'],
    [2, '나나나나나나나', 1, '소개팅, 혼밥'],
    [3, '다다다', 1, '단체 회식'],
    [4, '가가가가', 1, '소규모 회식, 데이트'],
    [5, '나나나나나나나', 2, '소개팅, 혼밥'],
    [6, '다다다', 2, '단체 회식'],
    [7, '가가가가', 2, '소규모 회식, 데이트'],
    [8, '나나나나나나나', 2, '소개팅, 혼밥'],
    [9, '다다다', 2, '단체 회식'],
    [10, '가가가가', 2, '소규모 회식, 데이트'],
    [11, '나나나나나나나', 2, '소개팅, 혼밥'],
    [12, '다다다', 3, '단체 회식'],
    [13, '가가가가', 3, '소규모 회식, 데이트'],
    [14, '나나나나나나나', 3, '소개팅, 혼밥'],
    [15, '다다다', 3, '단체 회식'],
    [16, '가가가가', 3, '소규모 회식, 데이트'],
    [17, '나나나나나나나', 3, '소개팅, 혼밥'],
    [18, '다다다', 3, '단체 회식'],
    [19, '가가가가', 3, '소규모 회식, 데이트'],
    [20, '나나나나나나나', 3, '소개팅, 혼밥'],
    [21, '다다다', 4, '단체 회식'],
    [22, '가가가가', 4, '소규모 회식, 데이트'],
    [23, '나나나나나나나', 4, '소개팅, 혼밥'],
    [24, '다다다', 4, '단체 회식'],
    [25, '가가가가', 4, '소규모 회식, 데이트'],
    [26, '나나나나나나나', 4, '소개팅, 혼밥'],
    [27, '다다다', 5, '단체 회식'],
    [28, '가가가가', 5, '소규모 회식, 데이트'],
    [29, '나나나나나나나', 5, '소개팅, 혼밥'],
    [30, '다다다', 5, '단체 회식'],
    [21, '다다다', 4, '단체 회식'],
    [22, '가가가가', 4, '소규모 회식, 데이트'],
    [23, '나나나나나나나', 4, '소개팅, 혼밥'],
    [24, '다다다', 4, '단체 회식'],
    [25, '가가가가', 4, '소규모 회식, 데이트'],
    [26, '나나나나나나나', 4, '소개팅, 혼밥'],
    [27, '다다다', 5, '단체 회식'],
    [28, '가가가가', 5, '소규모 회식, 데이트'],
    [29, '나나나나나나나', 5, '소개팅, 혼밥'],
    [30, '다다다', 5, '단체 회식']
]
const imagePaths = {
    person: 'resources/img/person.png',
    people: 'resources/img/people.png',
    people: 'resources/img/crowd.png',
    date: 'resources/img/date.png',
    friend: 'resources/img/friend.png',
    blindDate: 'resources/img/blind-date.png',
    meeting: 'resources/img/meeting.png'
};
const imageMappings = {
    '혼밥': 'person',
    '소규모 회식': 'people',
    '단체 회식': 'people',
    '데이트': 'date',
    '친구 초대': 'friend',
    '소개팅': 'blindDate',
    '미팅': 'meeting',
  };
fillTableWithData(data)

document.getElementById('categoryCheckBtn').addEventListener('change', function() {
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

document.getElementById('categoryText').addEventListener('touchstart', function() {
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

//scrollable
var scrollableElements = document.querySelectorAll('.scrollable');

scrollableElements.forEach(function(scrollableElement) {
    var isMouseDown = false;
    var startX, scrollLeft;

    scrollableElement.addEventListener('mousedown', function(e) {
        isMouseDown = true;
        startX = e.pageX - scrollableElement.offsetLeft;
        scrollLeft = scrollableElement.scrollLeft;
    });

    scrollableElement.addEventListener('mouseleave', function() {
        isMouseDown = false;
    });

    scrollableElement.addEventListener('mouseup', function() {
        isMouseDown = false;
    });

    scrollableElement.addEventListener('mousemove', function(e) {
        if(!isMouseDown) return;
        e.preventDefault();
        var x = e.pageX - scrollableElement.offsetLeft;
        var walk = (x - startX) * 2; // 스크롤 속도 조절을 위한 계수
        scrollableElement.scrollLeft = scrollLeft - walk;
    });
});

//카테고리 창 상단 고정
window.addEventListener('scroll', function() {
    var category = document.getElementById('categoryWindow');
    var headerHeight = document.querySelector('header').offsetHeight;

    if (window.scrollY >= headerHeight) {
        category.classList.add('fixed-category');
    } else {
        category.classList.remove('fixed-category');
    }
});

//표 데이터 채우기
function createImage(imageKey) { //일단 이미지 태그 생성해서 리턴 ㅇㅇ
    const imageName = imageMappings[imageKey];
    const imagePath = imagePaths[imageName];

    const img = document.createElement('img');
    img.src = imagePath;
    return img;
}

function fillTableWithData(data) {
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