window.onload = function() {
    const els = document.querySelectorAll('.fade-in-up');
    els.forEach((el, idx) => {
        el.style.animationDelay = (idx * 0.08) + 's';
    });
};

