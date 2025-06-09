<%@ page import="org.example.rf.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Artificial Intelligence Course</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css" />
</head>
<%@ include file="header.jsp" %>
<body>

<!-- Hero Section -->
<section class="hero">
    <div class="hero-content">
        <h1>Learning Powered by AI</h1>
        <p>Enhance your education through the power of Artificial Intelligence</p>
        <a href="<%= request.getContextPath() %>/courses">Learn More</a>
    </div>
</section>


<!-- About Section -->
<section id="about" class="section">
    <h2>About NMQ AI Education</h2>
    <p>This website is designed to apply AI assistants in education, helping learners generate personalized quizzes and receive instant, reliable answers to their questions.</p>
</section>

<!-- Course Content Section -->
<section class="section" style="background-color: #f1f5f9;">
    <h2>What You Will Experience</h2>
    <div class="grid">
        <div class="card">
            <h3>1. 1000+ Lesson</h3>
            <p>Lessons are curated by global experts, ensuring logical flow, accuracy, and suitability for learners of all levels.</p>
        </div>
        <div class="card">
            <h3>2. AI Agent</h3>
            <p>Our virtual assistant helps you answer questions during the learning process with trusted and high-quality sources.</p>
        </div>
        <div class="card">
            <h3>3. AI Test</h3>
            <p>Adaptive testing algorithms create quizzes tailored to each learner’s level and progress.</p>
        </div>
    </div>
</section>

<!-- Call to Action -->
<section class="section cta">
    <h2>Ready to Start Your AI Learning Journey?</h2>
    <p>Join today and receive exclusive early-bird offers!</p>
    <a href="<%= request.getContextPath() %>/login">Register Now</a>
</section>

</body>
<%@ include file="footer.jsp" %>
</html>
