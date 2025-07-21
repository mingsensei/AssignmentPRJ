<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>AI Translate</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .trans-card { background: #fff; border-radius: 26px; box-shadow: 0 8px 32px rgba(33,150,243,0.10); padding: 32px; margin-bottom: 32px; display: flex; gap: 32px; align-items: flex-start; min-height: 55vh;}
        .lang-select { min-width: 120px; border-radius: 12px;}
        .trans-textarea { min-height: 30vh !important; border-radius: 16px; border: 1.8px solid #e4e9f2; font-size: 1.17rem; resize: vertical; box-shadow: none !important; background: #f8fafc; transition: border .18s;}
        .trans-textarea:focus { border-color: #66aaff; background: #f4f8ff; }
        .icon-btn { border-radius: 18px; background: linear-gradient(90deg,#47b0ff,#447de8); color: #fff; border: none; font-size: 1.3rem; height: 48px; width: 48px; display: flex; align-items: center; justify-content: center; box-shadow: 0 3px 16px rgba(60,120,220,0.08); transition: background .17s, transform .13s;}
        .icon-btn:hover, .icon-btn:focus { background: linear-gradient(90deg,#447de8,#47b0ff); transform: translateY(-2px) scale(1.05); color: #fff;}
        .copy-btn { background: #eef4fc; color: #1976d2; border-radius: 12px; font-size: 1.1rem; padding: 2px 14px; margin-top: 8px; border: none; transition: background .13s, color .13s;}
        .copy-btn:hover { background: #e0ebff; color: #134fa3;}
        @media (max-width: 900px) {
            .trans-card { flex-direction: column; gap: 18px; padding: 20px 8px; }
            .trans-textarea { min-height: 110px; font-size: 1rem;}
        }
    </style>
</head>
<%@ include file="header.jsp" %>
<body>
<div class="container" style="max-width:1050px; margin:auto; padding-top: 20px;padding-bottom: 60px;">
    <div class="mb-4 d-flex align-items-center justify-content-center gap-3 flex-wrap">
        <img src="https://img.icons8.com/fluency/48/translation.png" style="height:44px">
        <h2 class="fw-bold text-primary" style="font-size:2.1rem;">AI Translate</h2>
    </div>
    <div class="trans-card">
        <div class="flex-grow-1 w-100">
            <div class="mb-2 d-flex align-items-center gap-2">
                <select class="form-select lang-select" id="fromLang">
                    <option value="vi">Tiếng Việt</option>
                    <option value="en" selected>English</option>
                    <option value="ja">日本語</option>
                    <option value="fr">Français</option>
                    <option value="zh">中文</option>
                    <option value="ko">한국어</option>
                </select>
            </div>
            <textarea class="form-control trans-textarea" id="inputText" placeholder="Enter text to translate..."></textarea>
        </div>
        <div class="d-flex flex-column justify-content-center align-items-center" style="min-width:60px;">
            <button class="icon-btn mb-3" id="swapBtn" title="Đổi chiều">
                <svg width="24" height="24" fill="currentColor"><path d="M6.5 16.5l-3-3 3-3M3.5 13.5h13a4 4 0 0 1 4 4v1"/><path d="M17.5 7.5l3 3-3 3M20.5 10.5h-13a4 4 0 0 0-4-4v-1"/></svg>
            </button>
            <button class="icon-btn" id="translateBtn" title="Dịch">
                <svg width="24" height="24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 19V5M6 5l7.5 7.5M6 5l-7.5 7.5M18 19l3-3-3-3M21 16h-13a4 4 0 0 1-4-4v-1"/></svg>
            </button>
        </div>
        <div class="flex-grow-1 w-100">
            <div class="mb-2 d-flex align-items-center gap-2">
                <select class="form-select lang-select" id="toLang">
                    <option value="vi" selected>Tiếng Việt</option>
                    <option value="en">English</option>
                    <option value="ja">日本語</option>
                    <option value="fr">Français</option>
                    <option value="zh">中文</option>
                    <option value="ko">한국어</option>
                </select>
            </div>
            <textarea class="form-control trans-textarea" id="outputText" placeholder="Result..." readonly></textarea>
            <button class="copy-btn" onclick="navigator.clipboard.writeText(document.getElementById('outputText').value)">Copy</button>
        </div>
    </div>
    <div class="text-center text-muted mt-3">AI-Powered Translation. Perfectly Understood.</div>
</div>
<script>
    // Đổi chiều ngôn ngữ
    document.getElementById('swapBtn').onclick = function() {
        let from = document.getElementById('fromLang');
        let to = document.getElementById('toLang');
        let temp = from.value;
        from.value = to.value;
        to.value = temp;
    };

    // AJAX gọi dịch
    document.getElementById('translateBtn').onclick = function() {
        let txt = document.getElementById('inputText').value.trim();
        let from = document.getElementById('fromLang').value;
        let to = document.getElementById('toLang').value;
        let outputArea = document.getElementById('outputText');
        outputArea.value = "";
        if (!txt) return;

        // Gửi AJAX POST lên /ai-translate
        fetch("<%=request.getContextPath()%>/ai-translate", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
            },
            body: "text=" + encodeURIComponent(txt)
                + "&from=" + encodeURIComponent(from)
                + "&to=" + encodeURIComponent(to)
        })
            .then(response => response.text())
            .then(data => {
                outputArea.value = data;
            })
            .catch(e => {
                outputArea.value = "Lỗi dịch tự động!";
            });
    };
</script>
</body>
<%@ include file="footer.jsp" %>
</html>
