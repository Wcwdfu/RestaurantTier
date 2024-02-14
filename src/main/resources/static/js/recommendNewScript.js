document.addEventListener('DOMContentLoaded', function () {
    // Swiper 인스턴스 생성 시 autoplay를 비활성화 상태로 시작
    let swiper = new Swiper('.swiper', {
        speed: 1,
        loop: true,
        slidesPerView: 1,
        spaceBetween: 60,
        centeredSlides: true,
        // autoplay 비활성화 상태로 시작
        // autoplay: false 또는 autoplay 옵션 자체를 제거
        breakpoints: {
            "1200": { "slidesPerView": 5 },
            "992": { "slidesPerView": 3 },
            "768": { "slidesPerView": 3 },
            "576": { "slidesPerView": 2 },
        }
    });

    document.getElementById('startRecommendation').addEventListener('click', function() {
        let initialDelay = 10; // 시작 지연 시간을 매우 짧게 설정
        let maxDelay = 1500; // 최대 지연 시간
        let delayIncrement = 50; // 지연 시간을 점차 증가시킴
        let currentDelay = initialDelay;
        let totalIncreaseTime = 5000; // 총 증가 시간 설정

        // 자동 재생 시작
        swiper.params.autoplay = {
            delay: currentDelay,
            disableOnInteraction: false
        };
        swiper.autoplay.start();

        let startTime = new Date().getTime(); // 시작 시간 기록
        let interval = setInterval(function() {
            let elapsedTime = new Date().getTime() - startTime; // 경과 시간 계산
            if (elapsedTime < totalIncreaseTime) {
                // 경과 시간에 따라 delay 증가
                currentDelay = initialDelay + (maxDelay - initialDelay) * (elapsedTime / totalIncreaseTime);
                swiper.params.autoplay.delay = currentDelay;
            } else {
                // 지정된 시간이 지나면 인터벌 중지 및 자동재생 중지
                clearInterval(interval);
                swiper.autoplay.stop();
            }
        }, delayIncrement); // delayIncrement마다 실행
    });
});
