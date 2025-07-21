<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .comment-section {
        margin: 2rem auto;
        padding: 1rem;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    .comment-section h4 {
        font-size: 1.2rem;
        margin-bottom: 1.2rem;
        color: #1c1e21;
    }

    .comment {
        display: flex;
        width: 90%;
        align-items: flex-start;
        gap: 0.75rem;
        background-color: #f0f2f5;
        padding: 0.75rem 1rem;
        border-radius: 18px;
        box-shadow: 0 1px 2px rgba(0,0,0,0.1);
    }

    .avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        object-fit: cover;
    }

    .comment-content {
        display: flex;
        flex-direction: column;
    }

    .comment-content strong {
        font-weight: 600;
        color: #050505;
    }

    .comment-content small {
        font-size: 0.8rem;
        color: #65676b;
        margin-top: 2px;
    }

    .comment-form {
        display: flex;
        flex-direction: column;
        gap: 0.5rem;
        background-color: #fff;
        padding: 1rem;
        border-radius: 12px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }

    .comment-form textarea {
        border: none;
        background-color: #f0f2f5;
        border-radius: 18px;
        padding: 0.75rem 1rem;
        font-size: 1rem;
        resize: none;
    }

    .comment-form button {
        align-self: flex-end;
        background-color: #1877f2;
        color: white;
        border: none;
        padding: 0.5rem 1rem;
        font-size: 0.95rem;
        border-radius: 6px;
        cursor: pointer;
        transition: background-color 0.2s ease;
    }

    .comment-form button:hover {
        background-color: #165ecc;
    }
</style>

<div class="comment-section">
    <h4>Comments</h4>
    <form class="comment-form" method="post" action="${pageContext.request.contextPath}/Comment">
        <input type="hidden" name="action" value="add"/>
        <input type="hidden" name="type" value="${type}"/>
        <input type="hidden" name="id" value="${id}"/>
        <textarea name="content" rows="3" required placeholder="Write a comment..."></textarea>
        <button type="submit">Post</button>
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <img class="avatar" src="https://i.pravatar.cc/48" alt="User"/>
                <div class="comment-content">
                    <strong>${comment.user.userName}</strong>
                    <p style="word-break: break-word">${comment.content}</p>
                    <small>${comment.createdAt}</small>
                </div>
            </div>
        </c:forEach>
    </form>



</div>
