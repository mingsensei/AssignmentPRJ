<%@page import="org.example.rf.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    User user = (User) session.getAttribute("user");
    // Fetch blog attribute if exists, avoid casting multiple times in JSP
    org.example.rf.model.Blog blog = (org.example.rf.model.Blog) request.getAttribute("blog");
    String action = (String) request.getAttribute("action");
    if (action == null)
        action = "create";
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title><c:out value="${action == 'edit' ? 'Edit Blog' : 'Create Blog'}"/></title>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/css/markdownstyle.css" />
        <style>
            html, body {
                height: 100%;
                margin: 0;
                padding: 0;
            }

            body {
                display: flex;
                flex-direction: column;
                font-family: "Segoe UI", Roboto, Helvetica, sans-serif;
                background-color: #fff;
                color: #222;
                overflow: hidden;
            }

            form {
                display: flex;
                flex-direction: column;
                height: 100%;
                overflow: hidden;
            }

            .topbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #f5f5f7;
                padding: 0.6rem 1.2rem;
                border-bottom: 1px solid #ccc;
                font-size: 0.95rem;
                font-family: "Segoe UI", sans-serif;
                position: sticky;
                top: 0;
                z-index: 10;
            }

            .left-section,
            .right-section {
                display: flex;
                align-items: center;
                gap: 0.5rem;
            }

            .avatar {
                width: 28px;
                height: 28px;
                border-radius: 50%;
                object-fit: cover;
            }

            .workspace-name {
                font-weight: 600;
                margin-right: 0.5rem;
            }

            .slash {
                color: #999;
            }

            .title-input {
                font-size: 1.2rem;
                font-weight: 600;
                border: none;
                background: transparent;
                outline: none;
                width: 200px;
            }

            .icon-btn {
                background: transparent;
                border: none;
                font-size: 1rem;
                cursor: pointer;
                padding: 0.3rem;
                border-radius: 4px;
                transition: background 0.2s;
            }

            .icon-btn:hover {
                background-color: #e0e0e0;
            }

            .editor-preview {
                display: flex;
                flex: 1;
                flex-direction: row;
                overflow: hidden;
                min-height: 0;
            }

            .editor,
            .preview {
                width: 50%;
                height: 100%;
                display: flex;
                flex-direction: column;
                overflow-y: auto;
                padding: 0;
                box-sizing: border-box;
            }

            .editor {
                border-right: 1px solid #ddd;
                background-color: #fdfdfd;
            }

            .preview {
                background-color: #fafafa;
            }

            .preview em {
                color: #777;
            }

            .toolbar {
                display: flex;
                background-color: #f5f5f5;
                flex-wrap: wrap;
            }

            .toolbar button {
                padding: 0.2rem 0.2rem;
                font-size: 0.9rem;
                border: 0;
                background-color: #f5f5f5;
                border-radius: 5px;
                cursor: pointer;
                transition: background 0.2s;
                margin: 5px;
            }

            .toolbar button:hover {
                background-color: #e0e0e0;
            }

            textarea {
                width: 100%;
                flex: 1;
                padding: 0;
                font-size: 1rem;
                font-family: monospace;
                resize: none;
                background-color: #fff;
                color: #222;
                box-sizing: border-box;
                border: 0;
            }

            textarea:focus {
                outline: none;
                box-shadow: none;
            }

            .submit-btn {
                margin-top: 0;
                padding: 0.6rem 1.2rem;
                font-size: 1rem;
                background-color: #007acc;
                color: #fff;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                transition: background-color 0.2s;
            }

            .submit-btn:hover {
                background-color: #005fa3;
            }
        </style>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/blog?action=${action}" method="post">
            <div class="topbar">
                <div class="left-section">
                    <img src="https://i.pravatar.cc/48" alt="Avatar" class="avatar" />
                    <a href="<%= request.getContextPath()%>/user-info" class="login-button"><%= user != null ? user.getUserName() : "Guest"%></a>
                    <span class="slash">/</span>
                    <div style="display: flex; flex-direction: column; gap: 0.4rem;">
                        <input type="text" name="title" class="title-input" value="<%= blog != null && blog.getTitle() != null ? blog.getTitle() : "My blog"%>" required placeholder="Blog Title"/>
                        <input type="url" name="thumbnail" style="font-size: 0.9rem; padding: 0.4rem; width: 100%; max-width: 400px;" value="<%= blog != null && blog.getThumbnail() != null ? blog.getThumbnail() : ""%>" placeholder="Thumbnail Image URL"/>
                        <textarea name="description" rows="2" style="font-size: 0.9rem; padding: 0.4rem; resize: vertical; width: 100%; max-width: 600px;" placeholder="Short description of the blog"><%= blog != null && blog.getDescription() != null ? blog.getDescription() : ""%></textarea>
                    </div>
                </div>
                <div class="right-section">
                    <button class="icon-btn" title="Share" type="button">🔗 Share</button>
                    <button type="submit" class="submit-btn">Publish Blog</button>
                </div>
            </div>
            <div class="editor-preview">
                <div class="editor">
                    <div class="toolbar">
                        <!-- Your toolbar buttons (same as original) -->
                        <button type="button" title="Bold" onclick="wrap('**')"><b>B</b></button>
                        <button type="button" title="Italic" onclick="wrap('_')"><i>I</i></button>
                        <button type="button" title="Strikethrough" onclick="wrap('~~')"><s>S</s></button>
                        <button type="button" title="Inline Code" onclick="wrap('`')">`Code`</button>
                        <button type="button" title="Heading" onclick="insertHeading()">H</button>
                        <button type="button" title="Blockquote" onclick="insertBlockquote()">❝</button>
                        <button type="button" title="Horizontal Rule" onclick="insertHr()">―</button>
                        <button type="button" title="Unordered List" onclick="insertList('- ')">• List</button>
                        <button type="button" title="Ordered List" onclick="insertList('1. ')">1. List</button>
                        <button type="button" title="Task List" onclick="insertList('- [ ] ')">☐ Task</button>
                        <button type="button" title="Link" onclick="insertLink()">🔗</button>
                        <button type="button" title="Image" onclick="insertImage()">🖼️</button>
                    </div>
                    <textarea
                        id="editor"
                        name="content"
                        oninput="updatePreview()"
                        required
                        ><%= blog != null && blog.getContent() != null ? blog.getContent() : ""%></textarea>
                    <input type="hidden" name="id" value="<%= blog != null ? blog.getId() : ""%>" />
                </div>
                <div class="preview markdown-style" id="preview">
                    <p><em>Live preview will appear here...</em></p>
                </div>
            </div>
        </form>

        <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
        <script src="<%= request.getContextPath() %>/js/markdowneditor.js"></script>
    </body>
</html>
