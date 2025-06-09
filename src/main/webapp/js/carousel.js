window.onload = function () {
    /** @type {HTMLElement} */
    const track = document.getElementById('carouselTrack');
    const slides = track.querySelectorAll('.carousel-card');
    const dotsContainer = document.getElementById('carouselDots');
    const totalSlides = slides.length;

    // Lấy chiều rộng slide thực tế (nếu cần)
    const slideWidth = slides[0].offsetWidth;

    let index = 0; // Biến lưu vị trí slide hiện tại

    // Tạo các dot tương ứng
    slides.forEach((_, i) => {
        const dot = document.createElement('span');
        dot.classList.add('dot');
        if (i === 0) dot.classList.add('active');
        dot.addEventListener('click', () => {
            index = i;
            moveToSlide(index);
            resetInterval();
        });
        dotsContainer.appendChild(dot);
    });

    const dots = dotsContainer.querySelectorAll('.dot');

    function moveToSlide(i1) {
        console.log("Chuyển sang slide số:", i1);
        track.style.transform = `translateX(-${i1 * slideWidth}px)`;
        dots.forEach(dot => dot.classList.remove('active'));
        dots[i1].classList.add('active');
    }

    function autoSlide() {
        index = (index + 1) % totalSlides;
        console.log("Auto slide index:", index);
        moveToSlide(index);
    }

    function resetInterval() {
        clearInterval(interval);
        interval = setInterval(autoSlide, 4000);
    }

    let interval = setInterval(autoSlide, 4000);
};
