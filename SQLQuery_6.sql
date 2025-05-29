CREATE TABLE users (
                       id BIGINT IDENTITY(1,1) PRIMARY KEY,
                       user_name NVARCHAR(100),
                       first_name NVARCHAR(100),
                       last_name NVARCHAR(100),
                       email NVARCHAR(100),
                       role NVARCHAR(20),
                       phone VARCHAR(20),
                       password NVARCHAR(256),
                       google_id NVARCHAR(100)  -- thêm cột này để lưu Google ID
);


CREATE TABLE category (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          name NVARCHAR(100),
                          description NVARCHAR(255)
);

CREATE TABLE course (
                        id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        name NVARCHAR(100),
                        description NVARCHAR(255),
                        price FLOAT,
                        type VARCHAR(10),
                        semester INT,
                        category_id BIGINT,
                        FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE chapter (
                         id BIGINT IDENTITY(1,1) PRIMARY KEY,
                         title NVARCHAR(255),
                         course_id BIGINT,
                         order_index INT,
                         FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE lesson (
                        id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        title NVARCHAR(100),
                        description NVARCHAR(MAX),
                        video_url VARCHAR(MAX),
    course_id BIGINT,
    order_index INT,
    chapter_id BIGINT,
    FOREIGN KEY (course_id) REFERENCES course(id),
    FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

CREATE TABLE material (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          title VARCHAR(255),
                          link VARCHAR(255),
                          chapter_id BIGINT,
                          type VARCHAR(50),
                          vectorDbPath VARCHAR(255),
                          FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

CREATE TABLE enrollment (
                            id BIGINT IDENTITY(1,1) PRIMARY KEY,
                            user_id BIGINT,
                            course_id BIGINT,
                            enrolled_at DATETIME,
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE level (
                       id BIGINT IDENTITY(1,1) PRIMARY KEY,
                       student_id BIGINT,
                       chapter_id BIGINT,
                       level INT,
                       FOREIGN KEY (student_id) REFERENCES users(id),
                       FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

CREATE TABLE exam (
                      id BIGINT IDENTITY(1,1) PRIMARY KEY,
                      student_id BIGINT,
                      chapter_id BIGINT,
                      score INT,
                      submitted_at DATETIME,
                      FOREIGN KEY (student_id) REFERENCES users(id),
                      FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

CREATE TABLE question (
                          id BIGINT IDENTITY(1,1) PRIMARY KEY,
                          content NVARCHAR(MAX),
                          option_a NVARCHAR(MAX),
                          option_b NVARCHAR(MAX),
                          option_c NVARCHAR(MAX),
                          option_d NVARCHAR(MAX),
                          correct_option CHAR(1),
                          explain NVARCHAR(MAX),
                          difficulty INT,
                          chapter_id BIGINT,
                          FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);

CREATE TABLE ai_question (
                             id BIGINT IDENTITY(1,1) PRIMARY KEY,
                             content NVARCHAR(MAX),
                             option_a NVARCHAR(MAX),
                             option_b NVARCHAR(MAX),
                             option_c NVARCHAR(MAX),
                             option_d NVARCHAR(MAX),
                             correct_option CHAR(1),
                             explain NVARCHAR(MAX),
                             difficulty INT
);

CREATE TABLE exam_question (
                               id BIGINT IDENTITY(1,1) PRIMARY KEY,
                               exam_id BIGINT,
                               question_id BIGINT,
                               ai_question_id BIGINT,
                               question_order INT,
                               student_answer CHAR(1),
                               FOREIGN KEY (exam_id) REFERENCES exam(id),
                               FOREIGN KEY (question_id) REFERENCES question(id),
                               FOREIGN KEY (ai_question_id) REFERENCES ai_question(id)
);

CREATE TABLE orders (
                        id BIGINT IDENTITY(1,1) PRIMARY KEY,
                        user_id BIGINT,
                        total_amount DECIMAL(18, 2),
                        status VARCHAR(20),
                        created_at DATETIME,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_item (
                            id BIGINT IDENTITY(1,1) PRIMARY KEY,
                            order_id BIGINT,
                            course_id BIGINT,
                            price DECIMAL(18, 2),
                            FOREIGN KEY (order_id) REFERENCES orders(id),
                            FOREIGN KEY (course_id) REFERENCES course(id)
);

CREATE TABLE payment (
                         id BIGINT IDENTITY(1,1) PRIMARY KEY,
                         order_id BIGINT,
                         amount DECIMAL(18, 2),
                         transaction_no VARCHAR(100),
                         bank_code VARCHAR(50),
                         pay_date DATETIME,
                         status VARCHAR(20),
                         created_at DATETIME,
                         FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE cart (
                      id BIGINT IDENTITY(1,1) PRIMARY KEY,
                      user_id BIGINT,
                      created_at DATETIME,
                      updated_at DATETIME,
                      FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE cart_item (
                           id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           cart_id BIGINT,
                           course_id BIGINT,
                           added_at DATETIME,
                           FOREIGN KEY (cart_id) REFERENCES cart(id),
                           FOREIGN KEY (course_id) REFERENCES course(id)
);

-- Blog system
CREATE TABLE blog (
                      id BIGINT IDENTITY(1,1) PRIMARY KEY,
                      title NVARCHAR(255),
                      content NVARCHAR(MAX),
                      created_at DATETIME,
                      updated_at DATETIME
);

CREATE TABLE blog_user (
                           id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           blog_id BIGINT,
                           user_id BIGINT,
                           role NVARCHAR(50),
                           assigned_at DATETIME,
                           FOREIGN KEY (blog_id) REFERENCES blog(id),
                           FOREIGN KEY (user_id) REFERENCES users(id)
);
