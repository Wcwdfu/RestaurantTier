document.addEventListener('DOMContentLoaded', function () {

    const selectedCuisines = [];

document.querySelectorAll('.option .col button').forEach(button => {
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

// 추천하기 버튼 리스너
document.getElementById('recommendBtn').addEventListener('click', function () {


    console.log(selectedCuisines);
    // 모든 버튼들을 가져와서 순회
    document.querySelectorAll('.option .col button').forEach(button => {
        // 선택되지 않은 버튼일 경우에만 텍스트를 배열에 추가
        if (button.classList.contains('selected')) {
            selectedCuisines.push(button.textContent);
            console.log(button.textContent)
        }

    });
    console.log(selectedCuisines);

    if (selectedCuisines.length === 0) {
        // 비어있다면 경고창을 띄우고 함수 종료
        alert("모든 메뉴를 제외하고 추천을 진행할 수 없습니다.");
        return;
    }

    const apiUrl = "/api/recommend?cuisine=" + selectedCuisines.join('-');

    fetch(apiUrl, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => {
            console.log(response)
            if (!response.ok) {
                throw new Error(`${data.status}: ${data.message}`);
            }
            return response.json();
        })

        .then(data => {
            console.log(data)
            // const imageUrls = [];
            // data.forEach(item => {
            //     // restaurantImgUrl 값이 존재하는지 확인하고 값이 'no_img'가 아닌 경우에만 push
            //     if (item.restaurantImgUrl && item.restaurantImgUrl !== 'no_img') {
            //         // 값이 존재하면 push
            //         imageUrls.push(item.restaurantImgUrl);
            //     } else {
            //         // 값이 존재하지 않거나 'no_img'인 경우 대체 이미지 경로를 push
            //         imageUrls.push("../img/restaurant/no_img.png");
            //     }
            // });
            var restaurantList=[]

            // 불러온 이미지 리스트에서 30개 추출
            while (restaurantList.length < 30) {
                const randomIndex = Math.floor(Math.random() * data.length);
                restaurantList.push(data[randomIndex]);
            }
            console.log(restaurantList)
            const imgDivs = document.querySelectorAll('.result-img-list');
            let imgCount = 0;
            imgDivs.forEach(imgDiv => {
                restaurantList.forEach(restaurant => {
                    const imgElement = document.createElement('img');
                    if(restaurant.restaurantImgUrl){
                        imgElement.src = restaurant.restaurantImgUrl;
                    }else{
                        imgElement.src = "/img/restaurant/no_img.png";
                        console.log(imgElement)
                    }



                    // 레스토랑 id를 data 속성으로 추가
                    imgElement.setAttribute('data-id', restaurant.restaurantId);

                    imgDiv.appendChild(imgElement);
                });
            });


            console.log(data.length);

            document.getElementById('recommendPage').classList.add('hidden');
            document.getElementById('recommendBtn').classList.add('hidden');
            document.getElementById('resultPage').classList.remove('hidden');
            document.getElementById('restartBtn').classList.remove('hidden');

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
    console.log(image)
    var restaurantId = image.dataset.id; // 이미지 URL을 기준으로 정보 조회
    console.log(restaurantId)

    const apiUrl = "/recommend/restaurant?restaurantId=" + restaurantId

    fetch(apiUrl, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    })
        .then(response => response.json())
        .then(data => {
            console.log(data)
            const matchedData = data;
            console.log(matchedData)
            if (matchedData) {
                const restaurantId = matchedData.restaurantId;
                const restaurantCuisine = matchedData.restaurantCuisine;
                const restaurantName = matchedData.restaurantName;
                const restaurantType = matchedData.restaurantType;
                const restaurantImageUrl = matchedData.restaurantImgUrl;

                console.log(matchedData.restaurantScoreSum)
                console.log(matchedData.restaurantEvaluationCount)
                
                // 식당 점수
                let score = 0.0
                score = (matchedData.restaurantScoreSum / matchedData.restaurantEvaluationCount) / 7.0 * 10.0;
                var formattedScore = score.toFixed(1) + "/10.0";
                console.log(formattedScore); // "9.5/10.0" 출력


                // 이미지 URL과 링크 설정
                const resultImg = document.getElementById('resultImg');
                const storeHref = document.getElementById('storeHref');
                const restaurantUrl = `/restaurants/{restaurantId}`;
                
                // 뽑힌 식당의 이미지 url 이 있으면 설정, 없으면 임시이미지
                if(matchedData.restaurantImgUrl){
                    resultImg.src = matchedData.restaurantImgUrl;
                }else{
                    resultImg.src = "/img/restaurant/no_img.png";

                }
                console.log(resultImg)
                storeHref.href = restaurantUrl;

                // 식당 정보 삽입
                const storeInfo = document.getElementById('storeInfo');
                // 불러온 식당의 평가데이터가 하나도 없으면
                if(matchedData.restaurantEvaluationCount===0){
                    storeInfo.innerHTML = `<div class="pt-30px bg-white text-center alt-font">
                    <span class="d-inline-block text-dark-gray fs-19 fw-600">${restaurantName}</span>
                    <div class="w-100">
                        <span class="d-inline-block align-middle">${restaurantCuisine}</span>
                        <span class="d-inline-block align-middle ms-10px me-10px fs-12 opacity-5">◍</span>
                        <span class="d-inline-block align-middle">${restaurantType}</span>
                    </div>
                </div>`
                }else{
                    storeInfo.innerHTML = `<div class="pt-30px bg-white text-center alt-font">
                    <span class="d-inline-block text-dark-gray fs-19 fw-600">${restaurantName}</span>
                    <div class="w-100">
                        <span class="d-inline-block align-middle">${restaurantCuisine}</span>
                        <span class="d-inline-block align-middle ms-10px me-10px fs-12 opacity-5">◍</span>
                        <span class="d-inline-block align-middle">${restaurantType}</span>
                        <span class="d-inline-block align-middle ms-10px me-10px fs-12 opacity-5">◍</span>
                        <span class="d-inline-block align-middle">${score}</span>
                    </div>F
                </div>`
                }


                const selectedBox = document.getElementById('SelectedBox');
                selectedBox.style.backgroundImage = `url('${restaurantImageUrl}')`;

                console.log(matchedData);
            } else {
                console.log("일치하는 데이터를 찾을 수 없습니다.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

//재설정 버튼 로직
document.getElementById('restartBtn').addEventListener('click', function () {
    location.reload(); // 페이지 새로고침
});
//바로 다시하기 버튼 로직


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
        console.log('가장 가까운 음식점 이미지:', closestImage.src);
        // 여기서 추가 작업을 수행할 수 있습니다. 예를 들어, 해당 이미지를 강조 표시하거나 정보를 표시하는 등
    }
    return closestImage
}

})



