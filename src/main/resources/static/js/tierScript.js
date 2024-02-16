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
                window.location.href = apiUrl;
            } else if (btn.dataset.situation) {
                var apiUrl = `/tier?cuisine=${cuisineParam}&situation=${btn.dataset.situation}`;
                window.location.href = apiUrl;
            }
        })
    });

    // 맨위로 올라가는 버튼
    const scrollTopBtn = document.getElementById('scrollTopBtn');
    scrollTopBtn.addEventListener("click", function() {
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    });
    window.addEventListener('scroll', function() {
        if (window.scrollY > 0) {
            scrollTopBtn.style.display = 'inline';
        } else {
            scrollTopBtn.style.display = 'none';
        }
    })

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

    setMouseHover();
    function setMouseHover() {
        // 표 위에 마우스 올렸을 때 색상 변경
        document.querySelectorAll("#tierTableBody tr").forEach(function (tr) {
            // 마우스를 올렸을 때
            tr.addEventListener("mouseenter", function () {
                this.style.backgroundColor = "#eee";
                this.querySelectorAll("*").forEach(function (child) {
                    child.style.backgroundColor = "#eee";
                });
            });
            // 마우스를 빼앗았을 때
            tr.addEventListener("mouseleave", function () {
                this.style.backgroundColor = "";
                this.querySelectorAll("*").forEach(function (child) {
                    child.style.backgroundColor = "";
                });
            });
        });
    }

    // 검색 로직
    const pageController = document.getElementById('pageController');
    const tierTableBody = document.getElementById('tierTableBody');
    const searchInput = document.getElementById('searchInput');
    const spinner = document.getElementById('spinner');
    var prevInput = '';
    var currentUrl = window.location.href;
    var baseUrl = window.location.origin;
    var relativeUrl = currentUrl.replace(baseUrl, '');
    var lastInputType = 0; // 0이면 빈칸, 1이면 입력
    let timer;
    $('input').on('input', function(event) {
        const inputValue = event.target.value;
        if (prevInput !== '' && inputValue === '') { // 이전에 검색창에 내용이 있었다가 다지워서 빈칸이 된 경우 -> page 원상복귀
            tierTableBody.innerHTML = '';
            lastInputType = 0;
            spinner.style.display = 'inline-block';
            const apiUrl = "/api/page" + relativeUrl;
            fetch(apiUrl)
                .then(response => {
                    if (lastInputType === 0) {
                        spinner.style.display = 'none';
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.text();
                    }
                })
                .then(html => {
                    tierTableBody.innerHTML = html;
                    setMouseHover();
                })
                .catch(error => {
                    // 오류 처리
                    console.error('Error fetching data:', error);
                });

            pageController.style.display = 'flex';
        } else if (prevInput === '') { // 이전에 검색창에 내용이 없었던 경우 -> page가 아닌 list로 불러옴
            tierTableBody.innerHTML = '';
            lastInputType = 1;
            spinner.style.display = 'inline-block';
            const apiUrl = "/api/list" + relativeUrl;
            fetch(apiUrl)
                .then(response => {
                    if (lastInputType === 1) {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.text();
                    }
                })
                .then(html => {
                    pageController.style.display = 'none';
                    tierTableBody.innerHTML = html;
                    setMouseHover();
                    filterTableBody(tierTableBody, inputValue);
                })
                .catch(error => {
                    // 오류 처리
                    console.error('Error fetching data:', error);
                });
        }
        // 이전에 검색창에 내용이 있어서 이미 list 목록인 경우(이미 page가 아님)
        timer = setTimeout(function() {
            if (lastInputType !== 0) {
                pageController.style.display = 'none';
                lastInputType = 1;
                if (lastInputType === 1) {
                    filterTableBody(tierTableBody, inputValue);
                }
            } // 검색 결과를 보여주는거는 0.4초가 지나야 필터링 해줌. 이게 핵심임.
        }, 400);
        prevInput = inputValue;
    })

    // 검색어가 이름이나 type에 들어가 있는 행만 보여줌
    function filterTableBody(tableBody, inputValue) {
        let trList = tableBody.getElementsByTagName("tr");

        // 첫 번재 tr은 맨 위의 열 정보임. 그래서 생략
        for (i = 0; i < trList.length; i++) {
            let td = trList[i].getElementsByTagName("td")[2];
            let spanList = td.getElementsByTagName("span");
            if (td) {
                let restaurantName = spanList[0].textContent || spanList[0].innerText;
                let restaurantType = spanList[1].textContent || spanList[1].innerText;
                if (
                    restaurantName.includes(inputValue) || restaurantType.includes(inputValue)
                ) {
                    trList[i].style.display = "table-row";
                } else {
                    trList[i].style.display = "none";
                }
            }
        }
        spinner.style.display = 'none';
    }
});
