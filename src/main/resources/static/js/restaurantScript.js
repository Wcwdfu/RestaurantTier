// 창이 로드될 때와 창 크기가 바뀔 때 적용할 함수 넣어주기
var map;
var marker;
window.onload = function() {
    mainImgResize();
    // html 에서 식당 정보 가져오기
    var restaurantInfo = document.getElementById('restaurantInfo');
    var name = restaurantInfo.getAttribute('data-name');
    var latitude = parseFloat(restaurantInfo.getAttribute('data-latitude'));
    var longitude = parseFloat(restaurantInfo.getAttribute('data-longitude'));
    // 네이버 지도
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(latitude, longitude),//위도, 경도
        zoom: 16,
        minZoom: 10,
    });
    marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(latitude, longitude),//위도, 경도
        map: map
    });
};
window.onresize = function() {
    mainImgResize();
}

// 메인 이미지 정사각형으로 되게
function mainImgResize() {
    const mainImg = document.getElementById('mainImg');
    mainImg.alt = 'main img';
    let mainImgWidth = parseFloat(getComputedStyle(mainImg.parentElement).width) * 0.3;
    mainImg.style.width = mainImgWidth + 'px';
    mainImg.style.height = mainImgWidth + 'px';
}

// 초기 favorite 설정
const beforeImgUrl = 'https://s-lol-web.op.gg/images/icon/icon-bookmark.svg?v=1702977255104';
const afterImgUrl = 'https://s-lol-web.op.gg/images/icon/icon-bookmark-on-w.svg?v=1702977255104';
const favoriteImg = document.getElementById('favoriteImg');
// favorite 버튼 이벤트리스너 등록
document.getElementById('favoriteImg').addEventListener('click', function() {
    toggleFavoriteRequest();
});
// 식당 Favorite 토글 요청
function toggleFavoriteRequest() {
    fetch("/api" + window.location.pathname + "/favorite/toggle", {
        method: 'POST',
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = "/user/login";
            } else {
                // 리다이렉션이 없는 경우에 대한 처리
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                toggleFavoriteHTML(favoriteImg);
                return response;
            }
        })
        .catch(error => console.error('Error:', error));
}
// 식당 Favorite 버튼 토글 변경
function toggleFavoriteHTML(favoriteImg) {
    const isFavorite = favoriteImg.classList.contains('after-favorite');

    if (isFavorite) {
        favoriteImg.src = beforeImgUrl;
        favoriteImg.classList.remove('after-favorite');
        favoriteImg.classList.add('before-favorite');
    } else {
        favoriteImg.src = afterImgUrl;
        favoriteImg.classList.remove('before-favorite');
        favoriteImg.classList.add('after-favorite');
    }
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

// 메뉴
function fillMenuInfo(data, num) { //num은 처음 표시할 메뉴 개수임. -1일 경우 모든 메뉴 표시
  const menuInfoContainer = document.getElementById('menuInfoContainer');
  menuInfoContainer.innerHTML = '';
  const menuUl = document.createElement('ul');
  menuUl.id = 'menuUL'
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
      img.alt = 'menu img';
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
const unfoldButton = document.getElementById('menuUnfoldButton');
const menuUL = document.getElementById('menuUL');
if (unfoldButton) {
    let initialMenusHeight;
    if (menuUL) {
        initialMenusHeight = parseFloat(getComputedStyle(menuUL).height);
    }
    unfoldButton.addEventListener('click', function() {
        const windowHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
        const thisText = this.textContent;
        const menuContainer = document.getElementById('menuContainer');
        if (thisText === '펼치기') {
            fillMenuInfo(restaurantMenus, -1); // 모든 메뉴 표시
            const menuUL = document.getElementById('menuUL');
            this.textContent = '접기';
            let maxHeight
                = windowHeight * 0.55 > initialMenusHeight + 70 ? windowHeight * 0.55 : initialMenusHeight + 70;
            menuUL.style.maxHeight = maxHeight + 'px';
            menuUL.style.overflowY = 'scroll';
        } else {
            fillMenuInfo(restaurantMenus, initialDisplayMenuCount);
            const menuUL = document.getElementById('menuUL');
            this.textContent = '펼치기';
            menuUL.style.maxHeight = 'none';
            menuUL.style.overflowY = 'visible';
        }
        menuContainer.scrollIntoView({ behavior: 'smooth', block: 'start' });
    })
}

// 네이버 지도 펼쳤다 접기
document.getElementById('mapUnfoldButton').addEventListener('click', function() {
    const thisText = this.textContent;
    const mapDiv = document.getElementById('map');
    const mapContainer = document.getElementById('mapContainer');
    const width = parseFloat(getComputedStyle(this).width);

    if (thisText === '펼치기') {
        this.textContent = '접기';
        let newHeight = width * 0.6;
        if (newHeight < 400) {
            newHeight = 400;
        }
        resize(width, newHeight);
        // 지도가 가장 위로 오도록 화면 스크롤
        mapContainer.scrollIntoView({ behavior: 'smooth', block: 'start' });
    } else {
        this.textContent = '펼치기';
        resize(width, 150);
    }
});
function resize(width, height){
    var Size = new naver.maps.Size(width, height);
    map.setSize(Size);
}

// 댓글 인기순, 최신순 토글 초기화
let activeButton = document.getElementById('button1');
// 댓글 인기순, 최신순 토글 함수
function toggleButton(buttonNumber) {
    const commentsUl = document.getElementById('commentList');
    const currentButton = document.getElementById(`button${buttonNumber}`);
    const queryParameter = buttonNumber === 1 ? 'POPULAR' : 'LATEST';
    const apiUrl = "/api" + window.location.pathname + "/comments?sort=" + queryParameter;
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(html => {
            commentsUl.innerHTML = html;
        })
        .catch(error => {
            // 오류 처리
            console.error('Error fetching data:', error);
        });

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

// 댓글 달기 요청
function sendComment() {
    const apiUrl = "/api" + window.location.pathname + "/comments";
    const currentUrl = window.location.href;
    const commentInput = document.getElementById('commentInput');
    const commentBody = commentInput.value;
    const commentToggleButton2 = document.getElementById('button2');

    const commentAlert = document.getElementById('commentAlert');

    fetch(apiUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            commentBody: commentBody,
        }),
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                // 리다이렉션이 없는 경우에 대한 처리
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                if (commentBody.length === 0) {
                    commentAlert.innerText = '내용을 입력해 주세요.';
                    return;
                } else {
                    commentInput.value = '';
                    commentAlert.innerText = '';
                    commentToggleButton2.click();
                }

                return response;
            }
        })
        .then(data => {
            //console.log("Comment added successfully:", data);
        })
        .catch(error => {
            //console.error("Error adding comment:", error);
        });
}

// 댓글 좋아요 요청
function commentLike(button) {
    const commentId = button.getAttribute("data-id");
    const apiUrl = `/api/restaurants/comments/${commentId}/like`;
    fetch(apiUrl, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => {
            let data = response.json();
            if (response.redirected) {
                window.location.href = response.url;
            } else if (!response.ok) {
                throw new Error(`${data.status}: ${data.message}`);
            } else {
                changeCommentHtml(commentId, button.closest('li'));
            }
        })
        .catch(error => {
            console.error("Error adding comment:", error);
        });
}
// 댓글 싫어요 요청
function commentDislike(button) {
    const commentId = button.getAttribute("data-id");
    const apiUrl = `/api/restaurants/comments/${commentId}/dislike`;
    fetch(apiUrl, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => {
            let data = response.json();
            if (response.redirected) {
                window.location.href = response.url;
            } else if (!response.ok) {
                throw new Error(`${data.status}: ${data.message}`);
            } else {
                changeCommentHtml(commentId, button.closest('li'));
            }
        })
        .catch(error => {
            console.error("Error adding comment:", error);
        });
}
// 댓글 하나 html 교체
function changeCommentHtml(commentId, commentLi) {
    const apiUrl = `/api/restaurants/comments/${commentId}`;
    fetch(apiUrl, {
        method: "GET",
    })
        .then(response => response.text())
        .then(html => {
            commentLi.innerHTML = html;
        })
        .catch(error => {
            console.error("Error adding comment:", error);
        });
}