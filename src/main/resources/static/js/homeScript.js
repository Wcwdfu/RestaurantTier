document.addEventListener('DOMContentLoaded', function () {
    var swiper = new Swiper('.swiper', {
        slidesPerView: 1,
        spaceBetween: 60,
        loop: true,
        autoplay: {
            delay: 2000, // 여기에서 속도를 조정하세요
            disableOnInteraction: false,
        },
        keyboard: {
            enabled: true,
            onlyInViewport: true,
        },
        breakpoints: {
            2500:{
                slidesPerView: 9,
            },
            1800:{
                slidesPerView: 7,
            },
            1200: {
                slidesPerView: 5,
            },
            992: {
                slidesPerView: 3,
            },
            768: {
                slidesPerView: 3,
            },
            576: {
                slidesPerView: 2,
            },
        },
        effect: 'slide',
    });
});
