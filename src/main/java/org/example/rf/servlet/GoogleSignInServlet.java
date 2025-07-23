package org.example.rf.servlet;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.rf.model.User;
import org.example.rf.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@WebServlet("/verify-google-token")
public class GoogleSignInServlet extends HttpServlet {

    private static final String CLIENT_ID = "453354005698-ghj46kru4jot9is7qu6pv2h9h7daqhc6.apps.googleusercontent.com";

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Đọc JSON từ body
        StringBuilder jsonBuffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuffer.append(line);
        }

        JsonObject jsonObject;
        try {
            jsonObject = JsonParser.parseString(jsonBuffer.toString()).getAsJsonObject();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }

        String idTokenString = jsonObject.has("token") ? jsonObject.get("token").getAsString() : null;
        if (idTokenString == null || idTokenString.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing token");
            return;
        }

        // Xác thực token Google
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token verification failed");
            return;
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = userService.getUserByEmail(email);

            if (user != null) {
                // Email đã tồn tại -> cập nhật Google ID nếu chưa có
                if (user.getGoogleId() == null || user.getGoogleId().isEmpty()) {
                    user.setGoogleId(googleId);
                    userService.updateUser(user); // đảm bảo bạn có phương thức này
                }
            } else {
                // Email chưa tồn tại -> tạo user mới
                user = new User();
                user.setGoogleId(googleId);
                user.setEmail(email);
                user.setUserName(name);
                user.setRole(User.Role.STUDENT);
                user.setPassword("GOOGLE_USER"); // placeholder
                userService.createUser(user);
            }

            // Đăng nhập thành công
            request.getSession().setAttribute("user", user);

            JsonObject result = new JsonObject();
            result.addProperty("status", "success");
            result.addProperty("email", email);
            result.addProperty("name", name);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(result.toString());
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid ID token");
        }
    }


    @Override
    public void destroy() {
        if (userService != null) {
            userService.close();
        }
        super.destroy();
    }
}
