document.addEventListener("DOMContentLoaded", function () {

    // ----------(first) 메인 평가 선택 로직---------- //

    var stars = document.querySelectorAll(".stars");
    var comment = document.querySelector("#ratingComment");

    stars.forEach(function (star, index) {
        star.addEventListener("click", function () {
            setMainRating(index);
        });
    });

    function setMainRating(selectedIndex) {
        stars.forEach(function (star, index) {
            if (index <= selectedIndex) {
                star.src = "/img/evaluation/star-filled.png";
            } else {
                star.src = "/img/evaluation/star-empty.png";
            }
            evaluationData.starRating = selectedIndex + 1
            console.log(evaluationData)

        });

        if (selectedIndex < 5) {
            comment.classList.remove("good");
            comment.style.opacity = 0.7;
        } else {
            comment.classList.add("good");
            comment.style.opacity = 1;
        }

        if (selectedIndex == 0) {
            comment.textContent = "장사를 왜 하는지 모르겠어요";
        } else if (selectedIndex == 1) {
            comment.textContent = "음.. 다시 오고싶다는 생각은 딱히.. ";
        } else if (selectedIndex == 2) {
            comment.textContent = "조금 아쉬운 가게에요";
        } else if (selectedIndex == 3) {
            comment.textContent = "평범한 음식점이에요";
        } else if (selectedIndex == 4) {
            comment.textContent = "괜찮아요. 다시 방문은 할겁니다";
        } else if (selectedIndex == 5) {
            comment.textContent = "제법 괜찮아요. 무조건 재방문 합니다";
        } else {
            comment.textContent = "당신을 행복하게 해줄 최고의 맛이에요";
        }

    }

// ---------- (second) 버튼 선택 효과 로직---------- //

    var categoryBtns = document.querySelectorAll(".categoryBtn");

    categoryBtns.forEach(function (button) {
        button.addEventListener("click", function () {
            toggleCategoryBtnsClass(button);
        });
    });

    function toggleCategoryBtnsClass(button) {
        button.classList.toggle("unselected");
        button.classList.toggle("selected");
    }

    // 상단 X 클릭시 뒤로가기
    document.getElementById('closeBtn').addEventListener('click', function() {
        window.history.back();
    });


    // ---------- (third) 버튼 선택 효과 로직---------- //

    document.querySelectorAll(".keywordBtn").forEach(function (button, index) {
        button.addEventListener("click", function () {
            toggleKeywordsBtnClass(button);
            togglekeywordsRatingArea(index)
        });
    });

    function toggleKeywordsBtnClass(button) {
        button.classList.toggle("unselected");
        button.classList.toggle("selected");
    }

    function togglekeywordsRatingArea(index) {
        var targetDiv = document.querySelector('#keywordEvaluateSection .keywordEvaluateArea:nth-child(' + (index + 1) + ')');

        targetDiv.classList.toggle('hidden');
        if (targetDiv.classList.contains('hidden')) {
            resetRatingInArea(targetDiv);
        }
    }

    // 키워드 평가 태그 두번 눌러서 숨겨질 때 데이터 초기화하기
    function resetRatingInArea(area) {
        // 모든 선택 포인트에서 'picked' 클래스 제거
        var selectPoints = area.querySelectorAll('.select-point');
        selectPoints.forEach(function (point) {
            point.classList.remove('picked');
        });

        // 모든 평가 텍스트에서 'bold' 클래스 제거
        var ratingParagraphs = area.querySelectorAll('.bar-comment-area p');
        ratingParagraphs.forEach(function (p) {
            p.classList.remove('bold');
        });

        // 평가 데이터에서 해당 키워드 평가 점수를 리셋
        var keywordIndex = Array.from(document.querySelectorAll('.keywordEvaluateArea')).indexOf(area);
        evaluationData.barRatings[keywordIndex] = 0
    }

    // ---- 바형 ui에서 선택 효과 로직 ---- //

    var keywordEvaluateAreas = document.querySelectorAll('.keywordEvaluateArea');

    keywordEvaluateAreas.forEach(function (keyword) {
        var selectPoints = keyword.querySelectorAll('.select-point');

        selectPoints.forEach(function (point, index) {
            point.addEventListener('click', function () {
                toggleSelectPoint(index + 1, keyword);
            });
        });
    });

    function toggleSelectPoint(rating, keyword) {
        var selectPoints = keyword.querySelectorAll('.select-point');
        var ratingParagraphs = keyword.querySelectorAll('.bar-comment-area p');

        // 해당 keyword 내에서 모든 select-point에서 picked 클래스 제거
        selectPoints.forEach(function (point) {
            point.classList.remove('picked');
        });

        // 클릭된 select-point에 picked 클래스 추가
        keyword.querySelector('.select-point.lv' + rating).classList.add('picked');

        // 각 p 태그에 bold 클래스를 toggle
        ratingParagraphs.forEach(function (p, index) {
            p.classList.toggle('bold', index + 1 === rating);
        });
        var keywordIndex = Array.from(keywordEvaluateAreas).indexOf(keyword);
        evaluationData.barRatings[keywordIndex] = rating;
    }
    // 전송 데이터 초기화
    var evaluationData = {
        starRating: 0,
        barRatings: [],
        restaurantId: 0
    }
    // ----------제출 버튼 눌림효과 로직---------- //
    
    var submitBtn = document.getElementById('submitBtn');

    submitBtn.addEventListener('mousedown', function () {
        submitBtn.classList.add('pushed');
    });

    submitBtn.addEventListener('mouseup', function () {
        submitBtn.classList.remove('pushed');
    });
    
    // 평가하기 버튼 눌렀을 때
    submitBtn.addEventListener('click', function () {
        var restaurantId = extractRestaurantIdFromUrl();
        evaluationData.restaurantId = restaurantId;
        console.log(restaurantId)
        console.log(evaluationData)

        fetch("/api/evaluation", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(evaluationData)
        })
            .then(response =>{
                if(!response.ok){
                    throw new Error("Network response was not ok");
                }
                return response;
            })
            .then(data => {
                console.log('Success', data);
                window.location.href = "/restaurants/"+restaurantId
            }
        )
            .catch(error =>{
                console.error('Error:',error)
            })


    })

    function extractRestaurantIdFromUrl() {
        // URL에서 레스토랑 ID 추출하는 로직
        // 예시: http://example.com/restaurant/123 -> 123 반환
        var urlSegments = window.location.pathname.split('/');
        return urlSegments[urlSegments.length - 1]; // 마지막 세그먼트가 ID라고 가정
    }

});