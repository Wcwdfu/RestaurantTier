// 현재 모바일인지 PC인지 체크(boolean)
var isMobile = /Mobi/i.test(window.navigator.userAgent);
// 창이 로드될 때와 창 크기가 바뀔 때 적용할 함수 넣어주기
window.onload = function() {
    mainImgResize();
    adjustWidth();

    // 네이버 지도
    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.5402396, 127.0705247),//위도, 경도
        zoom: 16,
        minZoom: 8,
    });
    var marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(37.5402396, 127.0705247),//위도, 경도
        map: map
    });
};
window.onresize = function() {
    mainImgResize();
    adjustWidth();
}
// 창이 로드될 때와 창 크기가 바뀔 때 적용되는 함수
function adjustWidth() {
    var outerContainer = document.getElementById('outerContainer');
    if (isMobile) {
        outerContainer.style.marginLeft = '10px';
        outerContainer.style.marginRight = '10px';

        outerContainer.style.minWidth = '260px';
    } else {
        outerContainer.style.width = '70%';

        outerContainer.style.minWidth = '450px';
        outerContainer.style.maxWidth = '900px';
    }
}

// 메인 이미지 정사각형으로 되게
function mainImgResize() {
    const mainImg = document.getElementById('mainImg');
    let mainImgWidth = parseFloat(getComputedStyle(mainImg.parentElement).width) * 0.3;
    mainImg.style.width = mainImgWidth + 'px';
    mainImg.style.height = mainImgWidth + 'px';
}

// 초기 favorite 설정
const beforeImgUrl = 'https://s-lol-web.op.gg/images/icon/icon-bookmark.svg?v=1702977255104'
const afterImgUrl = 'https://s-lol-web.op.gg/images/icon/icon-bookmark-on-w.svg?v=1702977255104'
let isFavorite = false;
const favoriteImg = document.getElementById('favoriteImg');
if (isFavorite) {
  setFavorite(favoriteImg);
} else {
  unsetFavorite(favoriteImg);
}
// favorite 버튼 이벤트리스너 등록
document.getElementById('favoriteImg').addEventListener('click', function() {
  if (this.classList.contains('before-favorite')) { // favorite으로 추가한 경우
    setFavorite(this);
  } else if (this.classList.contains('after-favorite')) { // favorite에서 뺀 경우
    unsetFavorite(this);
  }
});
function setFavorite(favoriteImg) {
  const imgContainerDiv = document.getElementById('favoriteContainer');

  imgContainerDiv.style.backgroundColor = '#E28400';
  imgContainerDiv.style.border = '1px solid #E28400'
  favoriteImg.src = afterImgUrl;
  favoriteImg.classList.remove('before-favorite');
  favoriteImg.classList.add('after-favorite');
}
function unsetFavorite(favoriteImg) {
  const imgContainerDiv = document.getElementById('favoriteContainer');

  imgContainerDiv.style.backgroundColor = '#fff';
  imgContainerDiv.style.border = '1px solid #bbb'
  favoriteImg.src = beforeImgUrl;
  favoriteImg.classList.remove('after-favorite');
  favoriteImg.classList.add('before-favorite');
}


// 티어 element 동적 생성
const tierData = [
    ['한식', '2티어'],
    ['친구', '3티어'],
    ['3~4인', '1티어']
]
fillTierInfo(tierData);
function fillTierInfo(data) {
    console.log("eee");
    const tierInfoContainer = document.getElementById('tierInfoContainer');

    for (let i = 0; i < data.length; i++) {
        const tierOuterSpan = document.createElement('span');
        const tierInnterSpan = document.createElement('span');

        tierInnterSpan.innerText = data[i][0] + ' ' + data[i][1];
        tierOuterSpan.appendChild(tierInnterSpan);

        tierOuterSpan.classList.add('tier');

        tierInfoContainer.appendChild(tierOuterSpan);
    }
}

// 메뉴 element 동적 생성
const menuJsonData = [
    {
        "menu_id": 1,
        "restaurant_id": 1,
        "menu_name": "크로와상",
        "menu_price": "3,200원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230703_218%2F1688371350659NMHlP_JPEG%2FEmmoBollP-9f7S9t1Tm8IS74CMqR8F-2GksEA5oyR8jBJCV0u53yveqmBO1yHubC.jpg&quot"
      },
      {
        "menu_id": 2,
        "restaurant_id": 1,
        "menu_name": "로쉐 초코 크로와상",
        "menu_price": "4,500원",
        "naver_type": "type3",
        "menu_img_url": "icon"
      },
      {
        "menu_id": 3,
        "restaurant_id": 1,
        "menu_name": "과일 크림 크로와상",
        "menu_price": "5,000원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230703_31%2F1688371401181SrNUi_JPEG%2FEmmoBollP-9f7S9t1Tm8IWE-UBogjC5twOCK2PRJFbuRf7PKn2oFqf7YB4L5CjsF.jpg&quot"
      },
      {
        "menu_id": 4,
        "restaurant_id": 1,
        "menu_name": "카야잼 버터 크로와상",
        "menu_price": "4,800원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230703_3%2F1688371416401iXlcd_JPEG%2FEmmoBollP-9f7S9t1Tm8Ia0MCo4L7avZscDoUWphehEnIczhsCfxtBcVpWjFku8X.jpg&quot"
      },
      {
        "menu_id": 5,
        "restaurant_id": 1,
        "menu_name": "소금빵",
        "menu_price": "3,900원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230704_138%2F1688433321224GTLUD_JPEG%2FXrM3Mp6YBABY1q36_F6caPLoJXVHDJqhyDA6RFDQTQtK7frqt9toy42U0eQOLKsF.jpg&quot"
      },
      {
        "menu_id": 6,
        "restaurant_id": 1,
        "menu_name": "팔미까레",
        "menu_price": "5,500원",
        "naver_type": "type3",
        "menu_img_url": "icon"
      },
      {
        "menu_id": 7,
        "restaurant_id": 1,
        "menu_name": "바닐라까눌레",
        "menu_price": "3,500원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230529_241%2F16852994917006Fppq_JPEG%2F6qYFq-IDTjRvZBJPt5BS5GO5tsTqB4LgEH7Ctg8r-8Ry0zc6SbOq-b1aidomFTE7.jpg&quot"
      },
      {
        "menu_id": 8,
        "restaurant_id": 1,
        "menu_name": "얼그레이까눌레",
        "menu_price": "3,500원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230529_75%2F1685299475621EaPav_JPEG%2F6qYFq-IDTjRvZBJPt5BS5NNr6nvlEhugsTtWf53ghZAL270rPmFCt_IQLfSxR9Pv.jpg&quot"
      },
      {
        "menu_id": 9,
        "restaurant_id": 1,
        "menu_name": "논산딸기주스(R(16oz))",
        "menu_price": "6,900원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240115_289%2F1705294526391UVC0H_JPEG%2FyfcXVdXtym0K4bfjcnZYoG1Ikz_fzpVUTct8Nmxb2NSuOr_3TfhsV6v5HOqkXYRZylcf4txMVZ2JSrpz.jpg&quot"
      },
      {
        "menu_id": 10,
        "restaurant_id": 1,
        "menu_name": "논산딸기바나나스노우(R(16oz))",
        "menu_price": "6,900원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240115_157%2F1705294526419PDClF_JPEG%2F2HBh0voQNU1V8-S8i-jTfO48apnwTrazG0NE5AjiyUehizLvn238UqEyZ1cA8AjeiDq3zRT78uFdzr05.jpg&quot"
      },
      {
        "menu_id": 11,
        "restaurant_id": 1,
        "menu_name": "논산딸기베리라떼(R(16oz))",
        "menu_price": "6,900원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240115_114%2F1705294526394jBDE6_JPEG%2FTyXrsfd0VwkaTM95FLDeUU72CDrLC4Dopgakvqvp86kFhul-4DZAJC6sKIcSBE08h0FHWVYMfcV-P3Rz.jpg&quot"
      },
      {
        "menu_id": 12,
        "restaurant_id": 1,
        "menu_name": "딸기 파베쇼콜라",
        "menu_price": "6,000원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240115_205%2F1705294526345xa0dG_JPEG%2FsvRTvZyHhoV50FnT-h-L_smlUCeHavWuPgzKCXMI2DFZvkcYIaeJSSKCwpMkKi7ehewnWwxOJVMeUoPG.jpg&quot"
      },
      {
        "menu_id": 13,
        "restaurant_id": 1,
        "menu_name": "딸기 치즈 케이크",
        "menu_price": "6,500원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240115_176%2F1705294526377OnN8F_JPEG%2FM1p0GuGzNd6_D8GD0Eu18bqOfXX-ZR_WeAyjhgZJvVusJrOJVRO30eAe2KxuByP4zoDY7R6i8Ks-YwPg.jpg&quot"
      },
      {
        "menu_id": 14,
        "restaurant_id": 1,
        "menu_name": "오리지널 불고기 반미",
        "menu_price": "7,400원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220825_141%2F1661388672073SA3q7_JPEG%2FwC7u9bajJU7sLc4p6S71faamQ45jEEG_9dY3JQJF8WNCf5OyXwfJDzWqoLoJSV4GMAyfo6RIl0LACH7X.jpg&quot"
      },
      {
        "menu_id": 15,
        "restaurant_id": 1,
        "menu_name": "에그마요 반미",
        "menu_price": "6,200원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220825_79%2F1661388671881lX1yz_JPEG%2Fa1XLjxd01OiAeFKYC-4vRmbWjbCvYlsZVqAnizIomF-j1EHA3q9Y6p7yAxH22UIJ2M47e12FSKJL0iOR.jpg&quot"
      },
      {
        "menu_id": 16,
        "restaurant_id": 1,
        "menu_name": "햄&에그 반미",
        "menu_price": "7,200원",
        "naver_type": "type3",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220414_89%2F1649925107360IefB6_JPEG%2FJ3_58AP2YjavaBthBGO71pLyjaY6_PRaC02sKV9cgq-rpwHrMJdb7QYD-CspuGyDM3UDt7s81p0luHL5.jpg&quot"
      },
      {
        "menu_id": 17,
        "restaurant_id": 1,
        "menu_name": "햄치즈 토스트",
        "menu_price": "4,700원",
        "naver_type": "type2",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220715_89%2F1657846595850BHq8S_JPEG%2F48Xdo7_0tnrxvZMx6CGwRkN45h7i9y1KU1Z6EXpCOjObXF2i4GUUXHr5tP5uWdFxHjVSPjL80Y_FGANp.jpg&quot"
      },
      {
        "menu_id": 18,
        "restaurant_id": 1,
        "menu_name": "에그 햄치즈 토스트",
        "menu_price": "5,200원",
        "naver_type": "type2",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20220825_292%2F1661388671816Tckt8_JPEG%2FyImRAYEA1kFe-cEahZJJJK9G3uktjz6VgltKniSL-hEy9bSmVzsZ_q2sH1MyT3_j5ubiHubZ44XeGv0q.jpg&quot"
      },
      {
        "menu_id": 19,
        "restaurant_id": 1,
        "menu_name": "1인 샌드위치 SET",
        "menu_price": "10,700원",
        "naver_type": "type4",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221018_108%2F1666051618915M0UcT_JPEG%2F3Ktw_mjCsDrC-wZG5p9ZroYQ73z83WvBMQeFCTIfklZhYdAInvQmxbkAhatnaLRmIlbeiKT97UpxJeMV.jpg&quot"
      },
      {
        "menu_id": 20,
        "restaurant_id": 1,
        "menu_name": "FRESH SANDWICH SET",
        "menu_price": "15,200원",
        "naver_type": "type4",
        "menu_img_url": "https://search.pstatic.net/common/?autoRotate=true&amp;quality=95&amp;type=f320_320&amp;src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20221018_155%2F1666051618924WWrOe_JPEG%2Fx2fzTDsh7cX1CrRxeQM-nI08TPqHvgnFzoqpGHIpJyUbaVEWSWCKY1jpW1nvaI2_m63zjXEuqrIelAc_.jpg&quot"
      }
]
fillMenuInfo(menuJsonData, 3);
function fillMenuInfo(data, num) { //num은 표시할 메뉴 개수임. -1일 경우 모든 메뉴 표시
    const menuInfoContainer = document.getElementById('menuInfoContainer');
    menuInfoContainer.innerHTML = '';
    const menuUl = document.createElement('ul');
    menuUl.classList.add('menu-ul');
    menuInfoContainer.appendChild(menuUl);

    for (var i = 0; i < data.length; i++) {
        const item = data[i];
        const menuLi = document.createElement('li');
        const textDiv = document.createElement('div');
        textDiv.classList.add('menu-text-container');
        const menuNameDiv = document.createElement('div');
        menuNameDiv.classList.add('menu-name');
        menuNameDiv.textContent = item.menu_name;
        const menuPriceContainer = document.createElement('div');
        menuPriceContainer.classList.add('menu-price');
        const menuPriceEm = document.createElement('em');
        if (item.menu_price != undefined) {
            menuPriceEm.textContent = item.menu_price.slice(0,-1);
        }
        const menuPriceSpan = document.createElement('span');
        menuPriceSpan.textContent = '원';
        menuPriceContainer.appendChild(menuPriceEm);
        menuPriceContainer.appendChild(menuPriceSpan);
        textDiv.appendChild(menuNameDiv);
        textDiv.appendChild(menuPriceContainer);
        
        if (item.naver_type === 'type1' || item.naver_type === 'type3') {
            const imgDiv = document.createElement('div');
            imgDiv.classList.add('menu-img-container');
            const img = document.createElement('img');
            const menuImgUrl = item.menu_img_url
            if (menuImgUrl === 'icon') {
                img.setAttribute('src', '/img/tier/logo.png');
                img.style.backgroundColor = '#aaa';
            } else {
                img.setAttribute('src', menuImgUrl);
            }
            menuLi.appendChild(img);
            menuLi.appendChild(textDiv);
        } else if (item.naver_type === 'type2' || item.naver_type === 'type4') {
            menuLi.appendChild(textDiv);
        } else {
            const nullDiv = document.createElement('div');
            nullDiv.classList.add('menu-name');
            nullDiv.textContent = '메뉴 없음';
            menuLi.appendChild(nullDiv);
        }

        menuUl.appendChild(menuLi);
      
        if (i + 1 >= num && num !== -1) {
          break;
        }
      }
}

// 메뉴 펼쳤다 접기
document.getElementById('menuUnfoldButton').addEventListener('click', function() {
    const thisText = this.textContent;
    const menuContainer = document.getElementById('menuContainer');

    if (thisText === '펼치기') {
        this.textContent = '접기';
        fillMenuInfo(menuJsonData, -1); // 모든 메뉴 표시
    } else {
        this.textContent = '펼치기';
        fillMenuInfo(menuJsonData, 3); // 메뉴 3개만 표시
    }
    // 메뉴가 가장 위로 오도록 화면 스크롤
    menuContainer.scrollIntoView({ behavior: 'smooth', block: 'start' });
});

// 네이버 지도 펼쳤다 접기
document.getElementById('mapUnfoldButton').addEventListener('click', function() {
    const thisText = this.textContent;
    const mapDiv = document.getElementById('map');
    const mapContainer = document.getElementById('mapContainer');

    if (thisText === '펼치기') {
        this.textContent = '접기';
        let newHeight = parseFloat(getComputedStyle(this).width) * 0.6;
        if (newHeight < 400) {
            newHeight = 400;
        }
        mapDiv.style.height = newHeight + 'px';
        // 지도가 가장 위로 오도록 화면 스크롤
        mapContainer.scrollIntoView({ behavior: 'smooth', block: 'start' });
    } else {
        this.textContent = '펼치기';
        mapDiv.style.height = '150px';
    }
});

// 댓글 인기순, 최신순 토글 초기화
let activeButton = document.getElementById('button1');
// 댓글 토글 함수
function toggleButton(buttonNumber) {
  const currentButton = document.getElementById(`button${buttonNumber}`);
  
  if (activeButton === currentButton) {
    // active 버튼 다시 클릭
  } else {
    // active가 아닌 버튼 클릭
    if (activeButton) {
      activeButton.classList.remove('active');
    }
    currentButton.classList.add('active');
    activeButton = currentButton;
  }
}

// 댓글 동적 생성
commentData = [
  [4, '김성운6', '2024.01.16. 17:11', '하하하하하하하하 나나 아아아아아 나나나나나 아아아앙'],
  [142, '김성운1', '2024.01.16. 17:11', '하하하하하하하하'],
  [43, '김성운3', '2024.01.16. 17:11', '나나 아아아아아 나나나나나 아아아앙'],
  [-22, '김성운9', '2024.01.16. 17:11', '하하하하하하하하'],
  [63, '김성운2', '2024.01.16. 17:11', '하하하하하하하하히히히히히히ㅣ히히히히히히히ㅣ힣 힣 딯 ㅈ ㄹㅈ ㄹ  가가가각 ㅇ나나나나나 아아아아아 나나나나나 아아아앙 나나나나나 앙아아'],
  [33, '김성운4', '2024.01.16. 17:11', '하하하하하하하하'],
  [-2, '김성운8', '2024.01.16. 17:11', '하하하하하하하하'],
  [12, '김성운5', '2024.01.16. 17:11', '나나 아아아아아 나나나나나 아아아앙'],
  [2, '김성운7', '2024.01.16. 17:11', '하하하하하하하하 나나 아아아아아 나나나나나 아아아앙 나나 아아아아아 나나나나나 아아아앙'],
  [-2424, '김성운ㅈㅈㅈㅈㅈ', '2024.01.16. 17:11', '하하하하하하하하']
]
fillCommentInfo(commentData);
function fillCommentInfo(data) {
    const commentList = document.getElementById('commentList');
    for (var i = 0; i < data.length; i++) {
      const li = document.createElement('li');

      const likeDiv = document.createElement('div');
      likeDiv.classList.add('like-div');
      likeDiv.textContent = data[i][0];
      li.appendChild(likeDiv);

      const bodyDiv = document.createElement('div');
      bodyDiv.classList.add('body-div');

      const nickDateDiv = document.createElement('div');
      nickDateDiv.classList.add('nick-date-div');
      const nickSpan = document.createElement('span');
      nickSpan.classList.add('nick-span');
      nickSpan.textContent = data[i][1];
      const dateSpan = document.createElement('span');
      dateSpan.classList.add('date-span');
      dateSpan.textContent = data[i][2];
      nickDateDiv.appendChild(nickSpan);
      nickDateDiv.appendChild(dateSpan);
      bodyDiv.appendChild(nickDateDiv);

      const realCommentContainer = document.createElement('div');
      realCommentContainer.classList.add('real-comment-container');
      const realComment = document.createElement('span');
      realComment.textContent = data[i][3];
      realCommentContainer.appendChild(realComment);
      bodyDiv.appendChild(realCommentContainer);

      li.appendChild(bodyDiv);

      commentList.appendChild(li);
    }
}

// 댓글 달기 요청
function sendComment() {
  const userTokenId = "user123";
  const apiUrl = "/api/restaurants/1/comments";
  const commentBody = document.getElementById('commentInput').textContent;
  fetch(apiUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      userTokenId: userTokenId,
      commentBody: commentBody,
    }),
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      console.log("Comment added successfully:", data);
    })
    .catch(error => {
      console.error("Error adding comment:", error);
    });
}