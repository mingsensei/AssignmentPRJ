USE ASSIGNMENT_PRJ;
-- Insert users

INSERT INTO users (user_name, first_name, last_name, email, role, phone, password, google_id) VALUES
(N'jdoe', N'John', N'Doe', N'jdoe@example.com', N'ADMIN', '1234567890', N'hashed_password_1', N'google123'),
(N'ajane', N'Alice', N'Jane', N'ajane@example.com', N'ADMIN', '0987654321', N'hashed_password_2', N'google456'),
(N'bsmith', N'Bob', N'Smith', N'bsmith@example.com', N'ADMIN', '1112223333', N'hashed_password_3', N'google789');

-- Insert blogs
INSERT INTO blog (title, content, created_at, updated_at) VALUES
(N'First Blog Post', N'This is the content of the first blog post.', GETDATE(), GETDATE()),
(N'Second Blog Post', N'Some more blog content to test styling and layout.', GETDATE(), GETDATE()),
(N'Editor''s Note', N'A post by the editor to review recent topics.', GETDATE(), GETDATE());

-- Insert blog_user relationships

INSERT INTO blog_user (blog_id, user_id, blog_role, assigned_at) VALUES
(1, 1, N'author', GETDATE()),
(2, 2, N'author', GETDATE()),
(3, 3, N'editor', GETDATE()),
(1, 2, N'reviewer', GETDATE());  -- Example of multiple users per blog


