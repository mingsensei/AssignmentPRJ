package org.example.rf.api.python;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.example.rf.model.AiQuestion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PythonApiClient {
    private static final String PYTHON_API_URL_GENERATE = "http://localhost:8003/generate";
    private static final String PYTHON_API_URL_CALCULATE_LEVEL = "http://localhost:8003/calculate_theta"; // New URL
    private final String PYTHON_API_URL_PROCESS_PDF = "http://localhost:8003/process";

    public List<AiQuestion> generateQuestion(int numAiQuestions, int difficulty, String vectorDbPath) throws IOException {
        String questionJSon = callPythonAPIGenerateQuestion(numAiQuestions, difficulty, vectorDbPath);
        return parseQuestionsFromJson(questionJSon);
    }

    private String callPythonAPIGenerateQuestion(int numAiQuestions, int difficulty, String vectorDbPath) throws IOException {
        URL url = new URL(PYTHON_API_URL_GENERATE);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String postData = "vector_db_path=" + vectorDbPath + "&difficulty=" + difficulty + "&num_questions=" + numAiQuestions;

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = postData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    private List<AiQuestion> parseQuestionsFromJson(String questionsJson) {
        List<AiQuestion> questions = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(questionsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String questionText = jsonObject.getString("question");
                int difficulty = jsonObject.getInt("difficulty");

                JSONArray choicesArray = jsonObject.getJSONArray("choices");
                String optionA = choicesArray.getString(0);
                String optionB = choicesArray.getString(1);
                String optionC = choicesArray.getString(2);
                String optionD = choicesArray.getString(3);

                String correctAnswer = jsonObject.getString("correct_answer");
                String explanation = jsonObject.getString("explanation");

                String correctOption = null;
                for (int j = 0; j < choicesArray.length(); j++) {
                    if (choicesArray.getString(j).equals(correctAnswer)) {
                        correctOption = switch (j) {
                            case 0 -> "A";
                            case 1 -> "B";
                            case 2 -> "C";
                            case 3 -> "D";
                            default -> null;
                        };
                        break;
                    }
                }

                if (correctOption == null) {
                    System.err.println("Correct answer not found in choices for question: " + questionText);
                    continue;
                }

                AiQuestion question = new AiQuestion();
                question.setContent(questionText);
                question.setOptionA(optionA);
                question.setOptionB(optionB);
                question.setOptionC(optionC);
                question.setOptionD(optionD);
                question.setCorrectOption(correctOption);
                question.setExplain(explanation);
                question.setDifficulty(difficulty);
                questions.add(question);
            }
        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
        return questions;
    }

    public String callPythonAPIPdf(String filePath, String chapterId) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(PYTHON_API_URL_PROCESS_PDF);

        // Tạo payload form data
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("pdf_filename", filePath));
        params.add(new BasicNameValuePair("chapter_title", chapterId));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8")); // Set form data

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity, "UTF-8");

        JSONObject jsonResponse = new JSONObject(responseString);
        return jsonResponse.getString("vector_db_path");
    }

    public String callPythonAPIForTheta(String questionsData) throws IOException {
        URL url = new URL(PYTHON_API_URL_CALCULATE_LEVEL); // Use the correct URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json"); // Thay đổi content type
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = questionsData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}
