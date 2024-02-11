document.addEventListener('DOMContentLoaded', function () {
    const content = document.querySelector('#plate');
    content.classList.add('on');

    window.addEventListener('scroll', () => {
        const content = document.querySelector('#plate');
        if (window.innerHeight > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });

    window.addEventListener('scroll', () => {
        const content = document.querySelector('#community');
        if (window.innerHeight > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#phrase');
        if (window.innerHeight > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });

    window.addEventListener('scroll', () => {
        const content = document.querySelector('#firstComment');
        if (window.innerHeight > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#secondComment');
        if (window.innerHeight > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#thirdComment');
        if (window.innerHeight > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });
    window.addEventListener('scroll', () => {
        const content = document.querySelector('#transferButton');
        if (window.innerHeight > content.getBoundingClientRect().top) {
            content.classList.add('on');
        }
    });

    document.querySelector("")
});
