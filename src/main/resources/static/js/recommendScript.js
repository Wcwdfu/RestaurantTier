document.addEventListener('DOMContentLoaded', function () {
    var restaurantLocation, restaurantEvaluation
    const selectedCuisines = [];
    // 음식 종류에 대한 클릭 리스너
    document.querySelectorAll('.filter-cuisine .col button').forEach(button => {
        button.addEventListener('click', function () {
            // 버튼의 클래스에 "selected"가 있는지 확인
            const isSelected = button.classList.contains('selected');

            // "selected" 클래스가 있으면 제거하고, 없으면 추가
            if (isSelected) {
                button.classList.remove('selected');
            } else {
                button.classList.add('selected');
            }
        });
    });

    // 위치,평가 카테고리에 대한 클릭 리스너

    document.querySelectorAll('.filter-location, .filter-evaluation').forEach(option => {
        option.addEventListener('click', function (e) {
            // 클릭된 요소가 버튼인지 확인
            if (e.target.tagName === 'BUTTON') {
                const isSelected = e.target.classList.contains('selected');

                // 클릭된 버튼이 이미 selected 상태인 경우, 해당 버튼에 대해서만 selected 클래스 제거
                if (isSelected) {
                    e.target.classList.remove('selected');
                } else {
                    // 그렇지 않다면, 현재 row 내의 모든 버튼에서 'selected' 클래스 제거 후,
                    // 클릭된 버튼에 'selected' 클래스 추가
                    e.currentTarget.querySelectorAll('button').forEach(button => {
                        button.classList.remove('selected');
                    });
                    e.target.classList.add('selected');
                }
            }
        });
    });


// 추천하기 버튼 리스너
    document.getElementById('recommendBtn').addEventListener('click', function () {


        document.querySelectorAll('.filter-cuisine .col button').forEach(button => {
            // 선택되지 않은 버튼일 경우에만 텍스트를 배열에 추가
            if (button.classList.contains('selected')) {
                selectedCuisines.push(button.textContent);
            }

        });
        // 지역 데이터 추가
        document.querySelectorAll('.filter-location .col button').forEach(button => {
            // 선택되지 않은 버튼일 경우에만 텍스트를 배열에 추가
            if (button.classList.contains('selected')) {
                restaurantLocation = button.textContent
            }

        });
        // 평가 순 데이터 추가
        document.querySelectorAll('.filter-evaluation .col button').forEach(button => {
            // 선택되지 않은 버튼일 경우에만 텍스트를 배열에 추가
            if (button.classList.contains('selected')) {
                restaurantEvaluation = button.textContent
            }

        });

        if (selectedCuisines.length === 0) {
            // 비어있다면 경고창을 띄우고 함수 종료
            alert("최소 하나의 카테고리를 선택해야 합니다.");
            return;
        }

        const apiUrl = "/api/recommend?cuisine=" + selectedCuisines.join('-') + "&location=" + restaurantLocation + "&evaluation=" + restaurantEvaluation;

        fetch(apiUrl, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`${data.status}: ${data.message}`);
                }
                return response.json();
            })

            .then(data => {
                // 데이터가 비어 있는 경우
                if (!data || data.length === 0) {
                    alert("해당 조건에 맞는 맛집이 존재하지 않습니다.");
                    window.location.reload(); // 페이지 새로고침
                    return; // 이후 로직을 실행하지 않기 위해 함수에서 빠르게 탈출
                }
                var restaurantList = data

                const imgDivs = document.querySelectorAll('.result-img-list');
                let imgCount = 0;
                imgDivs.forEach(imgDiv => {
                    restaurantList.forEach(restaurant => {
                        const imgElement = document.createElement('img');
                        if (restaurant.restaurantImgUrl !== "no_img") {
                            imgElement.src = restaurant.restaurantImgUrl;
                        } else {
                            imgElement.src = "/img/recommend/no_img.png";
                        }


                        // 레스토랑 id를 data 속성으로 추가
                        imgElement.setAttribute('data-id', restaurant.restaurantId);

                        imgDiv.appendChild(imgElement);
                    });
                });



                document.getElementById('recommendPage').classList.add('hidden');
                document.getElementById('recommendBtn').classList.add('hidden');
                document.getElementById('resultPage').classList.remove('hidden');
                // 가장 위로 올리기
                window.scrollTo(0, 0);

                document.getElementById('restartBtn').classList.remove('hidden');
                document.getElementById('restartDirectBtn').classList.remove('hidden');


            })
            .catch(error => {
                console.error("Error adding comment:", error);
            });

        const storeHref = document.getElementById('storeHref');
        storeHref.removeAttribute('href');

        setTimeout(function () {
            matchingdata();
            document.getElementById('resultInfoPage').classList.remove('hidden');
            const resultInfoPage = document.getElementById('resultInfoPage');
            resultInfoPage.style.opacity = 1;
            const processingTitle = document.getElementById('processingTitle');
            processingTitle.textContent = '오늘의 맛집은요..';

            setTimeout(function () {
                const restartBtn = document.getElementById('restartBtn');
                restartBtn.style.opacity = 1;
                const restartDirectBtn = document.getElementById('restartDirectBtn');
                restartDirectBtn.style.opacity = 1;

                const resultImgSlideBar = document.querySelector('.result-img-slideBar');
                resultImgSlideBar.style.opacity = 0;


                setTimeout(function () {
                    resultImgSlideBar.classList.add('hidden');
                }, 1000);

            }, 1000);

        }, 5500);


    });

// 선택된 img의 data-id를 통해 해당 id와 일치하는 식당정보 가져오기
    function matchingdata() {
        var image = findClosestImageToSelectBox()
        var restaurantId = image.dataset.id; // 이미지 URL을 기준으로 정보 조회

        const apiUrl = "/api/recommend/restaurant?restaurantId=" + restaurantId

        fetch(apiUrl, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
            .then(response => response.json())
            .then(data => {
                const matchedData = data;
                if (matchedData) {
                    const restaurantId = matchedData.restaurantId;
                    const restaurantCuisine = matchedData.restaurantCuisine;
                    const restaurantName = matchedData.restaurantName;
                    const restaurantType = matchedData.restaurantType;
                    const restaurantImageUrl = matchedData.restaurantImgUrl;



                    // 식당 점수
                    let score = 0.0
                    score = (matchedData.restaurantScoreSum / matchedData.restaurantEvaluationCount) / 7.0 * 10.0;
                    var formattedScore = score.toFixed(1) + "/10.0";


                    // 이미지 URL과 링크 설정
                    const resultImg = document.getElementById('resultImg');
                    const storeHref = document.getElementById('storeHref');
                    const restaurantUrl = `/restaurants/${restaurantId}`;

                    // 뽑힌 식당의 이미지 url 이 있으면 설정, 없으면 임시이미지
                    if (matchedData.restaurantImgUrl !== "no_img" && matchedData.restaurantImgUrl !== "no_restaurant") {
                        resultImg.src = matchedData.restaurantImgUrl;
                    } else {
                        resultImg.src = "/img/restaurant/no_img.png";
                    }
                    storeHref.href = restaurantUrl;

                    // 식당 정보 삽입
                    const storeInfo = document.getElementById('storeInfo');
                    // 불러온 식당의 평가데이터가 하나도 없는 경우
                    if (matchedData.restaurantEvaluationCount === 0) {
                        storeInfo.innerHTML = `<div class="pt-30px bg-white text-center alt-font">
                    <span class="d-inline-block text-dark-gray fs-19 fw-600">${restaurantName}</span>
                    <div class="w-100">
                        <span class="d-inline-block align-middle">${restaurantType}</span>
                    </div>
                </div>`
                    } // 최소 하나 이상 평가 데이터가 있는 경우 
                    else {
                        storeInfo.innerHTML = `<div class="pt-30px bg-white text-center alt-font">
                    <span class="d-inline-block text-dark-gray fs-19 fw-600">${restaurantName}</span>
                    <div class="w-100">
                        <span class="d-inline-block align-middle">${restaurantType}</span>
                        <span class="d-inline-block align-middle ms-10px me-10px fs-12 opacity-5">◍</span>
                        <span class="d-inline-block align-middle">${formattedScore}</span>
                    </div>
                </div>`
                    }


                    const selectedBox = document.getElementById('SelectedBox');
                    selectedBox.style.backgroundImage = `url('${restaurantImageUrl}')`;

                }
            })
            .catch(error => {
                console.error("Error:", error);
            });


//재설정 버튼 로직
    document.getElementById('restartBtn').addEventListener('click', function () {
        window.location.reload(); // 페이지 새로고침
    });


//바로 다시하기 버튼 로직
    document.getElementById('restartDirectBtn').addEventListener('click', function () {
        //select 박스 초기화
        const selectedBox = document.getElementById('SelectedBox');
        selectedBox.style.backgroundImage = '';



        const apiUrl = "/api/recommend?cuisine=" + selectedCuisines.join('-') + "&location=" + restaurantLocation + "&evaluation=" + restaurantEvaluation;

        fetch(apiUrl, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`${data.status}: ${data.message}`);
                }
                return response.json();
            })

            .then(data => {
                // 데이터가 비어 있는 경우
                if (!data || data.length === 0) {
                    alert("해당 조건에 맞는 맛집이 존재하지 않습니다.");
                    window.location.reload(); // 페이지 새로고침
                    return; // 이후 로직을 실행하지 않기 위해 함수에서 빠르게 탈출
                }
                var restaurantList = data
                const imgDivs = document.querySelectorAll('.result-img-list');
                imgDivs.forEach(imgDiv => {
                    while (imgDiv.firstChild) {
                        imgDiv.removeChild(imgDiv.firstChild);
                    }
                    restaurantList.forEach(restaurant => {
                        const imgElement = document.createElement('img');
                        if (restaurant.restaurantImgUrl !== "no_img" && restaurant.restaurantImgUrl !== "no_restaurant") {
                            imgElement.src = restaurant.restaurantImgUrl;
                        } else {
                            imgElement.src = "/img/recommend/no_img.png";
                        }


                        // 레스토랑 id를 data 속성으로 추가
                        imgElement.setAttribute('data-id', restaurant.restaurantId);

                        imgDiv.appendChild(imgElement);
                    });
                });


                // 결과 페이지 가리기
                document.getElementById('resultInfoPage').classList.add('hidden');

                // 슬라이더 시작
                const resultImgSlideBar = document.querySelector('.result-img-slideBar');
                resultImgSlideBar.style.opacity = 1;
                document.querySelector('.result-img-slideBar').classList.remove('hidden');
                // 스크롤 가장 위로 올리기
                window.scrollTo(0, 0);

                // 버튼 삭제
                const restartDirectBtn = document.getElementById('restartDirectBtn');
                const restartBtn = document.getElementById('restartBtn');

                restartDirectBtn.classList.add("hidden")
                restartBtn.classList.add("hidden")


            })
            .catch(error => {
                console.error("Error adding comment:", error);
            });

        const storeHref = document.getElementById('storeHref');
        storeHref.removeAttribute('href');

        // 멈춘 뒤 멈춘 자리의 사진 데이터 정보 띄우기
        setTimeout(function () {
            matchingdata();
            document.getElementById('resultInfoPage').classList.remove('hidden');
            const resultInfoPage = document.getElementById('resultInfoPage');
            resultInfoPage.style.opacity = 1;
            const processingTitle = document.getElementById('processingTitle');
            processingTitle.textContent = '오늘의 맛집은요..';

            setTimeout(function () {
                const restartBtn = document.getElementById('restartBtn');
                restartBtn.classList.remove("hidden")
                const restartDirectBtn = document.getElementById('restartDirectBtn');
                restartDirectBtn.classList.remove("hidden")

                const resultImgSlideBar = document.querySelector('.result-img-slideBar');
                resultImgSlideBar.style.opacity = 0;


                setTimeout(function () {
                    resultImgSlideBar.classList.add('hidden');
                }, 1000);

            }, 1000);

        }, 5500);
    });

// 셀렉 박스 위치와 가장 가까운 이미지 가져오기
    function findClosestImageToSelectBox() {
        const selectBox = document.querySelector('#SelectedBox'); // .select-box 요소 선택
        const images = document.querySelectorAll('.result-img-list > img'); // 모든 .img_box 요소 선택
        const selectBoxRect = selectBox.getBoundingClientRect(); // .select-box의 위치 정보

        let closestImage = null;
        let minDistance = Infinity;

        images.forEach((image) => {
            const imageRect = image.getBoundingClientRect(); // 각 이미지의 위치 정보
            // .select-box와 이미지 중심점 사이의 거리 계산
            const distance = Math.sqrt(Math.pow(imageRect.x - selectBoxRect.x, 2) + Math.pow(imageRect.y - selectBoxRect.y, 2));

            if (distance < minDistance) {
                closestImage = image; // 가장 가까운 이미지 업데이트
                minDistance = distance;
            }
        });

        if (closestImage) {
            // 가장 가까운 이미지의 src 속성 사용
            // 여기서 추가 작업을 수행할 수 있습니다. 예를 들어, 해당 이미지를 강조 표시하거나 정보를 표시하는 등
        }
        return closestImage
    }

}})



