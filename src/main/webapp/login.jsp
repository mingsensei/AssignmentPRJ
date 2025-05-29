<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>

    <!-- Font Icon -->
    <link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="main">
    <section class="sign-in">
        <div class="container">
            <div class="signin-content">

                <!-- Cột hình ảnh + link -->
                <div class="signin-image">
                    <figure><img src="images/signin-image.jpg" alt="sign in image"></figure>
                    <a href="<%= request.getContextPath() %>/register" class="signup-image-link">Create an account</a>
                </div>

                <!-- Cột form đăng nhập -->
                <div class="signin-form">
                    <h2 class="form-title">Sign In</h2>

                    <!-- Form đăng nhập bằng tài khoản nội bộ -->
                    <form method="POST" action="login" class="register-form" id="login-form">
                        <div class="form-group">
                            <label for="email"><i class="zmdi zmdi-account material-icons-name"></i></label>
                            <input type="email" name="email" id="email" placeholder="Your Email" required/>
                        </div>
                        <div class="form-group">
                            <label for="password"><i class="zmdi zmdi-lock"></i></label>
                            <input type="password" name="password" id="password" placeholder="Password" required/>
                        </div>
                        <div class="form-group">
                            <input type="checkbox" name="remember-me" id="remember-me" class="agree-term"/>
                            <label for="remember-me" class="label-agree-term">
                                <span><span></span></span>Remember me
                            </label>
                        </div>

                        <% String error = (String) request.getAttribute("error");
                            if (error != null) { %>
                        <div style="color: red; margin-bottom: 10px;">
                            <%= error %>
                        </div>
                        <% } %>

                        <div class="form-group form-button">
                            <input type="submit" name="signin" id="signin" class="form-submit" value="Log in"/>
                        </div>
                    </form>

                    <!-- Google Sign-In -->
                    <div class="social-login">
                        <span class="social-label">Or login with</span>
                        <ul class="socials">
                            <li>
                                <div id="g_id_onload"
                                     data-client_id="453354005698-ghj46kru4jot9is7qu6pv2h9h7daqhc6.apps.googleusercontent.com"
                                     data-auto_prompt="false"
                                     data-callback="handleCredentialResponse">
                                </div>
                                <div class="g_id_signin" data-type="standard"></div>
                            </li>
                        </ul>
                    </div>

                </div>
            </div>
        </div>
    </section>
</div>

<!-- Google Sign-In SDK -->
<script src="https://accounts.google.com/gsi/client" async defer></script>

<script>
    function handleCredentialResponse(response) {
        const id_token = response.credential;

        if (!id_token) {
            console.warn("⚠ Không nhận được ID token từ Google");
            return;
        }

        console.log("🔑 Gửi token đến servlet...");

        fetch("<%= request.getContextPath() %>/verify-google-token", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify({ token: id_token })
        })
            .then(res => {
                if (!res.ok) throw new Error("❌ Server xác thực thất bại");
                return res.json();
            })
            .then(data => {
                console.log("✅ Xác thực thành công:", data);
                window.location.href = "<%= request.getContextPath() %>/home";
            })
            .catch(err => {
                console.error("❌ Lỗi gửi token:", err);
                alert("Đăng nhập bằng Google thất bại.");
            });
    }
</script>

<script src="vendor/jquery/jquery.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
