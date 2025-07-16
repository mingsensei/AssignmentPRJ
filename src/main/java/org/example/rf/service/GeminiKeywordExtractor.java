package org.example.rf.service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiKeywordExtractor {

    // THAY THẾ BẰNG API KEY THỰC CỦA BẠN TỪ GOOGLE AI STUDIO
    private static final String GEMINI_API_KEY = "AIzaSyDgHsfABcItnJu0Vgb-YQK-p7zjFwuDNk0";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> extractKeywordsFromGemini(String inputText) throws IOException {
        List<String> keywords = new ArrayList<>();

        // 1. Tạo prompt cho Gemini
        // Prompt này hướng dẫn Gemini tách từ khóa và định dạng chúng.
        String prompt = "Trích xuất các từ khóa quan trọng nhất từ văn bản sau. Liệt kê chúng dưới dạng một danh sách được phân tách bằng dấu phẩy. Đây là văn bản người dùng nhập vào để tìm kiếm khoá học, cho nên phải trích xuất ra những từ để phân biệt khoá học, trả về kết quả là tiếng anh và các từ tiếng anh đồng nghĩa / liên quan ( khoảng 7 từ) VD: Từ khóa 1, Từ khóa 2, Từ khóa 3. chỉ trả lời kết quả theo yêu cầu, không trả lời gì thêm \n\nVăn bản: \"" + inputText + "\"";

        // 2. Chuẩn bị request body (payload) cho API Gemini
        // Cấu trúc JSON theo yêu cầu của Gemini API cho generateContent
        String jsonPayload = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}",
                prompt.replace("\"", "\\\"") // Escape dấu nháy kép trong prompt
        );

        RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json; charset=utf-8"));

        // 3. Xây dựng HTTP request
        Request request = new Request.Builder()
                .url(GEMINI_API_URL + GEMINI_API_KEY)
                .post(body)
                .build();

        // 4. Gửi request và nhận response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Xử lý lỗi nếu API không thành công (ví dụ: API Key sai, giới hạn rate,...)
                throw new IOException("Unexpected code " + response + " | Body: " + response.body().string());
            }

            // 5. Phân tích phản hồi JSON
            String responseBody = response.body().string();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Đường dẫn tới nội dung văn bản từ Gemini.
            // Cấu trúc JSON có thể phức tạp, bạn cần kiểm tra lại tài liệu API hoặc in ra responseBody
            // Ví dụ: response.candidates[0].content.parts[0].text
            JsonNode textNode = rootNode
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            if (textNode.isTextual()) {
                String geminiOutput = textNode.asText();
                // 6. Parse từ khóa từ chuỗi trả về
                // Sử dụng regex để tách các từ khóa dựa trên định dạng "Từ khóa 1, Từ khóa 2"
                Pattern pattern = Pattern.compile("\\s*([^,]+?)(?:,\\s*|$)"); // Tách các chuỗi không phải dấu phẩy
                Matcher matcher = pattern.matcher(geminiOutput);

                while (matcher.find()) {
                    String keyword = matcher.group(1).trim();
                    if (!keyword.isEmpty()) {
                        keywords.add(keyword);
                    }
                }
            } else {
                System.err.println("Gemini did not return a valid text response: " + responseBody);
            }
        }
        return keywords;
    }

    public String translateSmartly(String inputText, String sourceLanguage, String targetLanguage) throws IOException {
        // 1. Tạo prompt cho Gemini để dịch thuật
        // Prompt này yêu cầu Gemini dịch một cách tự nhiên và mượt mà nhất
        String prompt = String.format(
                "Dịch văn bản sau từ %s sang %s. Hãy dịch một cách tự nhiên và lưu giữ ý nghĩa gốc nhất có thể. Chỉ trả về văn bản đã dịch, không thêm bất kỳ ghi chú hay thông tin bổ sung nào khác.\n\nVăn bản gốc: \"%s\"",
                sourceLanguage,
                targetLanguage,
                inputText.replace("\"", "\\\"") // Escape dấu nháy kép trong input text
        );

        // 2. Chuẩn bị request body (payload)
        String jsonPayload = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}",
                prompt.replace("\"", "\\\"") // Escape dấu nháy kép trong prompt
        );

        RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json; charset=utf-8"));

        // 3. Xây dựng HTTP request
        Request request = new Request.Builder()
                .url(GEMINI_API_URL + GEMINI_API_KEY)
                .post(body)
                .build();

        // 4. Gửi request và nhận response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " | Body: " + response.body().string());
            }

            // 5. Phân tích phản hồi JSON
            String responseBody = response.body().string();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode textNode = rootNode
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            if (textNode.isTextual()) {
                return textNode.asText(); // Trả về trực tiếp văn bản dịch
            } else {
                System.err.println("Gemini did not return a valid text response for translation: " + responseBody);
                return "Error: Could not translate."; // Trả về lỗi nếu không có văn bản dịch
            }
        }
    }

    public static void main(String[] args) {
        GeminiKeywordExtractor extractor = new GeminiKeywordExtractor();
        String text = "Lập trình Java là một kỹ năng quan trọng trong phát triển phần mềm. Học thuật toán giúp cải thiện tư duy logic và giải quyết vấn đề hiệu quả.";

        System.out.println("Văn bản gốc: " + text);

        try {
            List<String> extractedKeywords = extractor.extractKeywordsFromGemini(text);
            System.out.println("Từ khóa được trích xuất bởi Gemini: " + extractedKeywords);
            // Kết quả mong đợi (có thể thay đổi tùy thuộc vào Gemini):
            // [Lập trình Java, kỹ năng quan trọng, phát triển phần mềm, Học thuật toán, tư duy logic, giải quyết vấn đề]
        } catch (IOException e) {
            System.err.println("Lỗi khi gọi Gemini API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
