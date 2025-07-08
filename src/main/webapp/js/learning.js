
document.addEventListener("DOMContentLoaded", function () {
    // Danh sách video YouTube ID
    const videoList = [
        "dQw4w9WgXcQ",
        "-JgmlILSF_g",
        "7zKfpTcUYBA"
    ];

    const randomIndex = Math.floor(Math.random() * videoList.length);
    const selectedVideoId = videoList[randomIndex];

// Thông tin bài học
    const lessonId = getQueryParam("lessonid");

    let videoWatched = false;
    let player;

// Gắn script YouTube API vào trang (nếu chưa có)
    if (!window.YT) {
        const tag = document.createElement('script');
        tag.src = "https://www.youtube.com/iframe_api";
        document.head.appendChild(tag);
    }

// Khi API sẵn sàng
    window.onYouTubeIframeAPIReady = function () {
        player = new YT.Player('lesson-video', {
            videoId: selectedVideoId,
            width: '100%',
            height: '400',
            playerVars: {
                autoplay: 1,
                mute: 1
            },
            events: {
                'onStateChange': onPlayerStateChange
            }
        });
    };

// Theo dõi trạng thái video
    function onPlayerStateChange(event) {
        if (event.data === YT.PlayerState.ENDED && !videoWatched) {
            videoWatched = true;
            console.log("✅ Video đã kết thúc!");

            fetch('learning', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    lessonId: lessonId
                })
            }).then(response => {
                if (response.ok) {
                    console.log("✅ Tiến độ học đã lưu!");
                } else {
                    console.error("❌ Không thể lưu tiến độ.");
                }
            }).catch(error => {
                console.error("❌ Lỗi kết nối:", error);
            });
        }
    }

// --- Các hàm phụ trợ ---
    window.toggleSidebar = function () {
        const sidebar = document.getElementById('sidebar');
        const icon = document.getElementById('toggle-icon');
        sidebar.classList.toggle('collapsed');
        icon.textContent = sidebar.classList.contains('collapsed') ? '»' : '«';
    };

    window.toggleChapter = function (chapterId) {
        const content = document.getElementById("chapter" + chapterId);
        if (content.classList.contains("show")) {
            bootstrap.Collapse.getInstance(content).hide();
        } else {
            if (!bootstrap.Collapse.getInstance(content)) {
                new bootstrap.Collapse(content);
            } else {
                bootstrap.Collapse.getInstance(content).show();
            }
        }
    };
    function getQueryParam(param) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(param);
    }

});

