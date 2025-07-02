<%@page import="org.example.rf.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Create Blog</title>
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
        <form action="<%=request.getContextPath()%>/blog?action=create" method="post">
            <div class="topbar">
                <div class="left-section">
                    <img src="https://i.pravatar.cc/48" alt="Avatar" class="avatar">
                    <a href="<%= request.getContextPath()%>/user-info" class="login-button"><%=user.getUserName()%></a>
                    <span class="slash">/</span>
                    <input type="text" name="title" class="title-input" value="My blog" required />
                </div>
                <div class="right-section">
                    <button class="icon-btn" title="Share">🔗 Share</button>
                    <button type="submit" class="submit-btn">Publish Blog</button>
                </div>
            </div>
            <div class="editor-preview">
                <div class="editor">
                    <div class="toolbar">
                        <!-- Text Formatting -->
                        <button type="button" title="Bold" onclick="wrap('**')"><b>B</b></button>
                        <button type="button" title="Italic" onclick="wrap('_')"><i>I</i></button>
                        <button type="button" title="Strikethrough" onclick="wrap('~~')"><s>S</s></button>
                        <button type="button" title="Inline Code" onclick="wrap('`')">`Code`</button>

                        <!-- Headings / Structure -->
                        <button type="button" title="Heading" onclick="insertHeading()">H</button>
                        <button type="button" title="Blockquote" onclick="insertBlockquote()">❝</button>
                        <button type="button" title="Horizontal Rule" onclick="insertHr()">―</button>

                        <!-- Lists -->
                        <button type="button" title="Unordered List" onclick="insertList('- ')">• List</button>
                        <button type="button" title="Ordered List" onclick="insertList('1. ')">1. List</button>
                        <button type="button" title="Task List" onclick="insertList('- [ ] ')">☐ Task</button>

                        <!-- Media -->
                        <button type="button" title="Link" onclick="insertLink()">🔗</button>
                        <button type="button" title="Image" onclick="insertImage()">🖼️</button>
                    </div>

                    <textarea id="editor" name="content" oninput="updatePreview()" required></textarea>
                </div>
                <div class="preview markdown-style" id="preview">
                    <p><em>Live preview will appear here...</em></p>
                </div>
            </div>
        </form>

        <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
        <script>
                            marked.setOptions({
                                breaks: true,
                                gfm: true,
                                smartLists: true,
                                smartypants: true
                            });

                            function updatePreview() {
                                const text = document.getElementById('editor').value;
                                document.getElementById('preview').innerHTML = marked.parse(text);
                            }
                            window.onload = updatePreview;

                            function wrap(tag) {
                                const textarea = document.getElementById('editor');
                                const start = textarea.selectionStart;
                                const end = textarea.selectionEnd;
                                const selected = textarea.value.substring(start, end);
                                const newText = tag + selected + tag;
                                textarea.setRangeText(newText, start, end, 'end');
                                updatePreview();
                            }

                            function insertLink() {
                                const url = prompt('Enter URL:');
                                if (!url)
                                    return;
                                const text = prompt('Enter link text:') || 'text';
                                const textarea = document.getElementById('editor');
                                const start = textarea.selectionStart;
                                const end = textarea.selectionEnd;
                                const markdown = '[' + text + '](' + url + ')';
                                textarea.setRangeText(markdown, start, end, 'end');
                                updatePreview();
                            }

                            function insertImage() {
                                const url = prompt('Enter image URL:');
                                if (!url)
                                    return;
                                let alt = prompt('Enter alt text (optional):') || 'image';
                                const textarea = document.getElementById('editor');
                                const start = textarea.selectionStart;
                                const end = textarea.selectionEnd;
                                const markdown = '![' + alt + '](' + url + ')';
                                textarea.setRangeText(markdown, start, end, 'end');
                                updatePreview();
                            }

                            function insertHeading() {
                                const textarea = document.getElementById('editor');
                                const lineStart = textarea.value.lastIndexOf('\n', textarea.selectionStart - 1) + 1;
                                textarea.setRangeText('# ', lineStart, lineStart, 'end');
                                updatePreview();
                            }
                            function insertBlockquote() {
                                const textarea = document.getElementById('editor');
                                const start = textarea.selectionStart;
                                const end = textarea.selectionEnd;
                                const selected = textarea.value.substring(start, end);
                                const markdown = '> ' + selected.replace(/\n/g, '\n> ');
                                textarea.setRangeText(markdown, start, end, 'end');
                                updatePreview();
                            }

                            function insertHr() {
                                const textarea = document.getElementById('editor');
                                const pos = textarea.selectionStart;
                                const markdown = '\n\n---\n\n';
                                textarea.setRangeText(markdown, pos, pos, 'end');
                                updatePreview();
                            }

                            function insertList(prefix) {
                                const textarea = document.getElementById('editor');
                                const start = textarea.selectionStart;
                                const end = textarea.selectionEnd;
                                const selected = textarea.value.substring(start, end);
                                const lines = selected.split('\n');
                                const modified = lines.map(line => prefix + line).join('\n');
                                textarea.setRangeText(modified, start, end, 'end');
                                updatePreview();
                            }
        </script>
    </body>
</html>
