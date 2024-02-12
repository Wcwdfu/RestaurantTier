document.addEventListener('DOMContentLoaded', function () {
    const content = document.querySelector('#plate');
    content.classList.add('on');

    window.addEventListener('scroll', () => {
        const content = document.querySelector('#plate');
        if (window.innerHeight*0.8 > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });

    window.addEventListener('scroll', () => {
        const content = document.querySelector('#community');
        if (window.innerHeight*0.8 > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#phrase');
        if (window.innerHeight*0.8 > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });

    window.addEventListener('scroll', () => {
        const content = document.querySelector('#firstComment');
        if (window.innerHeight*0.8 > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#secondComment');
        if (window.innerHeight*0.8 > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#thirdComment');
        if (window.innerHeight*0.8 > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#transferButton');
        if (window.innerHeight*0.8 > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });


});
