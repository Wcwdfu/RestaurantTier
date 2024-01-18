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

// 메뉴 데이터 가져오기
let menuData;

async function getMenuData() {
    if (!menuData) {
        var currentUrl = window.location.href;
        const restaurantId = currentUrl.split('/')[4];

        console.log(currentUrl);

        if (!isNaN(restaurantId)) {
            try {
                const response = await fetch(`/api/restaurants/${restaurantId}/menus`);
                menuData = await response.json();
            } catch (error) {
                console.error('API 요청 실패:', error);
                throw error;
            }
        } else {
            console.log("올바르지 않은 페이지");
        }
    }
    return menuData;
}

getMenuData()
    .then(data => {
        if (data) {
            fillMenuInfo(data, 3);
            console.log(data);
        }
    })
    .catch(error => {
        console.error('데이터 로딩 실패:', error);
    });

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
        menuNameDiv.textContent = item.menuName;
        const menuPriceContainer = document.createElement('div');
        menuPriceContainer.classList.add('menu-price');
        const menuPriceEm = document.createElement('em');
        if (item.menuPrice != undefined) {
            menuPriceEm.textContent = item.menuPrice.slice(0,-1);
        }
        const menuPriceSpan = document.createElement('span');
        menuPriceSpan.textContent = '원';
        menuPriceContainer.appendChild(menuPriceEm);
        menuPriceContainer.appendChild(menuPriceSpan);
        textDiv.appendChild(menuNameDiv);
        textDiv.appendChild(menuPriceContainer);
        
        if (item.naverType === 'type1' || item.naverType === 'type3') {
            const imgDiv = document.createElement('div');
            imgDiv.classList.add('menu-img-container');
            const img = document.createElement('img');
            const menuImgUrl = item.menuImgUrl
            if (menuImgUrl === 'icon') {
                img.setAttribute('src', '/img/tier/logo.png');
                img.style.backgroundColor = '#aaa';
            } else {
                img.setAttribute('src', menuImgUrl);
            }
            menuLi.appendChild(img);
            menuLi.appendChild(textDiv);
        } else if (item.naverType === 'type2' || item.naverType === 'type4') {
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
        fillMenuInfo(menuData, -1); // 모든 메뉴 표시
    } else {
        this.textContent = '펼치기';
        fillMenuInfo(menuData, 3); // 메뉴 3개만 표시
    }
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
  const commentBody = document.getElementById('commentInput').value;
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