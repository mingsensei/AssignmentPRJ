<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>

    <!-- Font Icon -->
    <link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>


<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<div class="alert alert-danger" role="alert" style="margin: 10px;">
    <%= error %>
</div>
<%
    }
%>

<div class="main">
    <section class="signup">
        <div class="container">
            <div class="signup-content">
                <div class="signup-form">
                    <h2 class="form-title">Sign Up</h2>
                    <form method="POST" action="register" class="register-form" id="register-form" onsubmit="return checkPasswords()">
                        <div class="form-group">
                            <label for="first_name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                            <input type="text" name="first_name" id="first_name" placeholder="First Name" required/>
                        </div>
                        <div class="form-group">
                            <label for="last_name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                            <input type="text" name="last_name" id="last_name" placeholder="Last Name" required/>
                        </div>
                        <div class="form-group">
                            <label for="email"><i class="zmdi zmdi-email"></i></label>
                            <input type="email" name="email" id="email" placeholder="Your Email" required/>
                        </div>
                        <div class="form-group">
                            <label for="password"><i class="zmdi zmdi-lock"></i></label>
                            <input type="password" name="password" id="password" placeholder="Password" required/>
                            <span id="passwordError" class="error-message"></span>
                        </div>
                        <div class="form-group">
                            <label for="re_password"><i class="zmdi zmdi-lock-outline"></i></label>
                            <input type="password" name="re_password" id="re_password" placeholder="Repeat your password" required/>
                            <span id="rePasswordError" class="error-message"></span>
                        </div>

                        <div class="form-group">
                            <input type="checkbox" name="agree-term" id="agree-term" class="agree-term" required/>
                            <label for="agree-term" class="label-agree-term">
                                <span><span></span></span>I agree all statements in
                                <a href="#" class="term-service">Terms of service</a>
                            </label>
                        </div>
                        <div class="form-group form-button">
                            <input type="submit" name="signup" id="signup" class="form-submit" value="Register"/>
                        </div>
                    </form>
                    <style>
                        .error-message {
                            color: red;
                            font-size: 12px;
                            margin-top: 5px;
                            display: none;
                        }
                    </style>

                    <script>
                        function checkPasswords() {
                            const password = document.getElementById("password").value;
                            const rePassword = document.getElementById("re_password").value;

                            const rePasswordError = document.getElementById("rePasswordError");

                            if (password !== rePassword) {
                                rePasswordError.textContent = "Mật khẩu nhập lại không khớp! Vui lòng kiểm tra lại.";
                                rePasswordError.style.display = "block";
                                return false;
                            }

                            rePasswordError.style.display = "none";
                            return true;
                        }

                        document.getElementById("password").addEventListener("input", checkPasswords);
                        document.getElementById("re_password").addEventListener("input", checkPasswords);
                    </script>

                </div>
                <div class="signup-image">
                    <figure><img src="images/signup-image.jpg" alt="sign up image"></figure>
                    <a href="<%= request.getContextPath() %>/login" class="signup-image-link">I am already member</a>
                </div>
            </div>
        </div>
    </section>
</div>

<script src="vendor/jquery/jquery.min.js"></script>
<script src="js/main.js"></script>
<script src="js/InputValidator.js"></script>
</body>
</html>
