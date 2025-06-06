USE ASSIGNMENT_PRJ;

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
use ASSIGNMENT_PRJ;

CREATE TABLE blog_user (
                           id BIGINT IDENTITY(1,1) PRIMARY KEY,
                           blog_id BIGINT,
                           user_id BIGINT,
                           blog_role NVARCHAR(50),
                           assigned_at DATETIME,
                           FOREIGN KEY (blog_id) REFERENCES blog(id),
                           FOREIGN KEY (user_id) REFERENCES users(id)
);


--Chèn data môn học
--PRJ301
INSERT INTO category (name, description)
VALUES (N'Web Development', N'Courses related to building dynamic websites and web applications using various technologies');

INSERT INTO course (name, description, price, type, semester, category_id)
VALUES (
           N'Java Web Application Development',
           N'Learn Servlet, JSP, MVC, JPA, JDBC, and AI in Java web development',
           199000,
           'offline',
           3,
           1 -- Giả sử category_id = 1 là "Web Development"
       );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Introduction & Setup', 1, 1),
                                                        (N'Servlet and JSP', 1, 2),
                                                        (N'MVC Architecture & Filters', 1, 3),
                                                        (N'JDBC & Database Connection', 1, 4),
                                                        (N'JSP Languages & Server-side Objects', 1, 5),
                                                        (N'JPA in Web Apps', 1, 6),
                                                        (N'Integrating AI into Java Web', 1, 7);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
-- Chương 1
(N'Course Overview', N'Course objectives and prerequisites', NULL, 1, 1, 1),
(N'Setup Tomcat, NetBeans & SQL Server', N'Installing and configuring development tools', NULL, 1, 2, 1),

-- Chương 2
(N'Servlet Basics', N'How Servlets work and are deployed', NULL, 1, 1, 2),
(N'Understanding JSP', N'Introduction to JSP and lifecycle', NULL, 1, 2, 2),

-- Chương 3
(N'MVC Architecture', N'Using Servlet/Filter as Controllers', NULL, 1, 1, 3),
(N'Using Filters', N'Learn filter configuration and usage', 'https://docs.oracle.com/cd/B14099_19/web.1012/b14017/filters.htm', 1, 2, 3),

-- Chương 4
(N'Working with JDBC', N'Connect and query database using JDBC', 'http://docs.oracle.com/javase/tutorial/JDBC', 1, 1, 4),

-- Chương 5
(N'Languages in JSP', N'Using JSTL and Expression Language', NULL, 1, 1, 5),
(N'Server-side Objects', N'Session, Application, Request, Page scope', NULL, 1, 2, 5),

-- Chương 6
(N'Getting Started with JPA', N'Mapping entities and setting up persistence.xml', NULL, 1, 1, 6),
(N'Creating a JPA-based Web Demo', N'Using JPA in a real mini web project', NULL, 1, 2, 6),

-- Chương 7
(N'Intro to AI in Java Web', N'Overview of integrating AI logic in web apps', NULL, 1, 1, 7),
(N'Prompt Engineering for Java Web', N'Guide to Using Prompt Engineering Effectively', NULL, 1, 2, 7),
(N'AI Implementation with NetBeans', N'Using AI modules inside Java web project', NULL, 1, 3, 7),
(N'Database Query Agent', N'Using AI to generate database queries', NULL, 1, 4, 7);

--IOT102
INSERT INTO category (name, description)
VALUES (N'Internet of Things', N'Courses related to IoT technologies, devices, and embedded systems');

INSERT INTO course (name, description, price, type, semester, category_id)
VALUES (
           N'Internet of Things',
           N'Learn IoT concepts, Arduino programming, and real-world circuit design using online + offline methods.',
           159000,
           'hybrid',
           3,
           2  -- Giả sử category_id = 2 tương ứng với Internet of Things
       );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Introduction to IoT', 2, 1),
                                                        (N'Networking & Security in IoT', 2, 2),
                                                        (N'Arduino & Microcontroller Basics', 2, 3),
                                                        (N'Programming with Arduino UNO', 2, 4),
                                                        (N'Practical Circuits & Projects', 2, 5),
                                                        (N'Simulation & Project Finalization', 2, 6);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
-- Chapter 1
(N'What is IoT?', N'Basic concepts and applications of IoT', 'https://learning.edx.org/course/course-v1:CurtinX+IOT1x+2T2018/home', 2, 1, 8),
(N'Things in IoT', N'Understanding "things" in IoT systems', NULL, 2, 2, 8),

-- Chapter 2
(N'Networking in IoT', N'Explore IoT networking models and protocols', NULL, 2, 1, 9),
(N'Security in IoT', N'Understand risks and protections in IoT', NULL, 2, 2, 9),

-- Chapter 3
(N'Arduino UNO Overview', N'Introduction to Arduino UNO and Atmega328', 'http://www.introtoarduino.com', 2, 1, 10),
(N'Interrupts and Microcontroller Architecture', N'Learn about hardware interrupts and system design', NULL, 2, 2, 10),

-- Chapter 4
(N'IN/OUT Digital Programming', N'Use digital pins with Arduino UNO', NULL, 2, 1, 11),
(N'Analog Input/Output', N'Handle analog sensors and actuators', NULL, 2, 2, 11),
(N'Communication Programming', N'Communication techniques using Arduino', NULL, 2, 3, 11),

-- Chapter 5
(N'Component Selection', N'How to select and calculate electronic components', NULL, 2, 1, 12),
(N'Drawing and Installing Circuits', N'Create and build simple IoT circuits', NULL, 2, 2, 12),
(N'20 Arduino Projects', N'Explore projects from Instructables', NULL, 2, 3, 12),

-- Chapter 6
(N'Simulation Software', N'How to simulate your Arduino projects', NULL, 2, 1, 13),
(N'Project Guidelines and Rubrics', N'IOT102 project documents and topics', NULL, 2, 2, 13),
(N'Final Project Presentation', N'Prepare and present your IoT solution', NULL, 2, 3, 13);

--MAS291
INSERT INTO category (name, description)
VALUES (N'Statistics & Probability', N'Courses related to probability theory, statistical inference, and data analysis techniques');

INSERT INTO course (name, description, price, type, semester, category_id)
VALUES (
           N'Statistics & Probability',
           N'This course covers fundamentals of probability, discrete and continuous distributions, descriptive statistics, hypothesis testing, confidence intervals, and regression analysis.',
           299000,
           'offline',
           3,
           3  -- Giả sử category_id = 3 tương ứng với Statistics & Probability
       );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Introduction to Statistics', 3, 1),
                                                        (N'Probability Theory & Random Variables', 3, 2),
                                                        (N'Discrete Distributions', 3, 3),
                                                        (N'Continuous Distributions', 3, 4),
                                                        (N'Descriptive Statistics & Data Visualization', 3, 5),
                                                        (N'Sampling & Central Limit Theorem', 3, 6),
                                                        (N'Confidence Intervals', 3, 7),
                                                        (N'Hypothesis Testing', 3, 8),
                                                        (N'Regression & Correlation', 3, 9);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
-- Chapter 1
(N'Roles of Statistics in Engineering', N'Discuss importance, data collection methods, and inference types', NULL, 3, 1, 14),

-- Chapter 2
(N'Sample Spaces and Events', N'Definitions, rules, independence, conditional probability', NULL, 3, 1, 15),
(N'Random Variables', N'Discrete vs Continuous, and probability calculations', NULL, 3, 2, 15),

-- Chapter 3
(N'Discrete Distributions', N'Binomial, Geometric, Poisson, Hypergeometric, Negative Binomial', NULL, 3, 1, 16),
(N'Mean and Variance of Discrete Variables', N'Calculating expectations and variance', NULL, 3, 2, 16),

-- Chapter 4
(N'Continuous Distributions', N'Uniform and Exponential distributions', NULL, 3, 1, 17),
(N'Normal Distribution', N'Z-scores, standardization, approximations', NULL, 3, 2, 17),

-- Chapter 5
(N'Data Summaries and Visualization', N'Stem-leaf, histograms, box plots, outliers', NULL, 3, 1, 18),

-- Chapter 6
(N'Sampling and CLT', N'Point estimation and applications of Central Limit Theorem', NULL, 3, 1, 19),

-- Chapter 7
(N'Confidence Intervals for Means & Proportions', N'Estimation and sample size calculations', NULL, 3, 1, 20),

-- Chapter 8
(N'Hypothesis Testing Basics', N'Formulate hypotheses, p-value, type I/II errors', NULL, 3, 1, 21),

-- Chapter 9
(N'Simple Linear Regression', N'Regression equation, t-tests, predictions', NULL, 3, 1, 22);


--PRF192
INSERT INTO category (name, description)
VALUES (N'Programming Fundamentals', N'Courses focused on foundational knowledge in procedural programming and software development using C language');

INSERT INTO course (name, description, price, type, semester, category_id)
VALUES (
           N'Programming Fundamentals',
           N'This course provides fundamental knowledge of programming using the C language, focusing on procedure-oriented design, coding, debugging, modularity, and program testing.',
           199000,
           'offline',
           1,
           4 -- Giả sử category_id = 4 tương ứng với Programming Fundamentals
       );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Introduction to Programming and C Language', 4, 1),
                                                        (N'Variables, Expressions, and Operators', 4, 2),
                                                        (N'Logic Constructs and Program Flow', 4, 3),
                                                        (N'Functions and Modularity', 4, 4),
                                                        (N'Pointers and Memory', 4, 5),
                                                        (N'Libraries and Header Files', 4, 6),
                                                        (N'Arrays and Structures', 4, 7),
                                                        (N'String Manipulation in C', 4, 8),
                                                        (N'File Handling in C', 4, 9);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
-- Chapter 1
(N'How Programs Work on Computers', N'Understanding how a program is executed and the computer system environment.', NULL, 4, 1, 23),

-- Chapter 2
(N'Variables and Data Types', N'Explanation and usage of variables, constants, and basic data types in C.', NULL, 4, 1, 24),
(N'Operators and Expressions', N'Arithmetic, logical, relational operators and expression evaluation.', NULL, 4, 2, 24),

-- Chapter 3
(N'Conditional Statements and Loops', N'Using if, else, switch, while, for, and do-while in C programs.', NULL, 4, 1, 25),
(N'Programming Style and Structure', N'Good practices and styling in C programming.', NULL, 4, 2, 25),

-- Chapter 4
(N'Modular Programming and Functions', N'Using functions, parameters, and return values for modularity.', NULL, 4, 1, 26),

-- Chapter 5
(N'Introduction to Pointers', N'Understanding addresses, pointer types, pointer arithmetic.', NULL, 4, 1, 27),
(N'Pointers and Functions', N'Passing by address, pointer to pointer, and memory management.', NULL, 4, 2, 27),

-- Chapter 6
(N'C Libraries and Header Files', N'Standard library functions and usage of custom headers.', NULL, 4, 1, 28),

-- Chapter 7
(N'Working with Arrays', N'One-dimensional and multi-dimensional arrays.', NULL, 4, 1, 29),
(N'Structures in C', N'Defining and using structs, arrays of structs.', NULL, 4, 2, 29),

-- Chapter 8
(N'Strings in C', N'Creating, manipulating and formatting strings.', NULL, 4, 1, 30),

-- Chapter 9
(N'File Input and Output', N'Reading from and writing to text and binary files.', NULL, 4, 1, 31);

--MAD101

INSERT INTO category (name, description)
VALUES (N'Discrete Mathematics', N'Courses covering mathematical logic, set theory, algorithms, combinatorics, and graph theory for computer science and IT applications.');

INSERT INTO course (name, description, price, type, semester, category_id)
VALUES (
           N'Discrete Mathematics',
           N'This course equips students with foundational concepts in logic, sets, functions, combinatorics, algorithm complexity, modular arithmetic, recursion, graphs, and trees to support computer science problem solving.',
           299000,
           'offline',
           1,
           5 -- Giả sử category_id = 5 tương ứng với Discrete Mathematics
       );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Propositional and Predicate Logic', 5, 1),
                                                        (N'Sets, Functions, and Sequences', 5, 2),
                                                        (N'Algorithms and Complexity', 5, 3),
                                                        (N'Number Theory and Modular Arithmetic', 5, 4),
                                                        (N'Induction, Recursion and Recursive Algorithms', 5, 5),
                                                        (N'Counting Principles and Recurrence', 5, 6),
                                                        (N'Graph Theory and Applications', 5, 7),
                                                        (N'Trees and Applications', 5, 8);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
-- Chapter 1: Logic
(N'Logical Propositions and Equivalence', N'Work with propositions, truth tables, and logical equivalence.', NULL, 5, 1, 32),
(N'Predicate Logic and Quantifiers', N'Translating and manipulating logical expressions with quantifiers.', NULL, 5, 2, 32),
(N'Logical Inference and Proofs', N'Using inference rules and proving statement validity.', NULL, 5, 3, 32),

-- Chapter 2: Sets and Functions
(N'Set Theory and Operations', N'Basic set terminology, operations, power sets, and Venn diagrams.', NULL, 5, 1, 33),
(N'Functions and Properties', N'Function operations, injectivity, surjectivity, and bijections.', NULL, 5, 2, 33),
(N'Sequences and Summation', N'Identifying sequences and evaluating summations.', NULL, 5, 3, 33),

-- Chapter 3: Algorithms and Complexity
(N'Algorithms and Pseudocode', N'Understanding what constitutes an algorithm, with pseudocode examples.', NULL, 5, 1, 34),
(N'Big-O and Complexity Analysis', N'Estimating time complexity with Big-O, Big-Omega, and Big-Theta.', NULL, 5, 2, 34),

-- Chapter 4: Number Theory
(N'Modular Arithmetic and Applications', N'Integer division, modular operations, and applications in IT.', NULL, 5, 1, 35),
(N'Cryptography and Random Numbers', N'Modular exponentiation and pseudorandom generation.', NULL, 5, 2, 35),
(N'Prime Factorization and GCD', N'Prime checks, factorization, GCD, and LCM calculations.', NULL, 5, 3, 35),

-- Chapter 5: Induction and Recursion
(N'Mathematical Induction', N'Principles of induction and applying to mathematical proofs.', NULL, 5, 1, 36),
(N'Recursive Definitions and Algorithms', N'Defining and solving recursive sequences and algorithms.', NULL, 5, 2, 36),
(N'Merge Sort and Recursion vs Iteration', N'Mergesort and comparison between recursive and iterative solutions.', NULL, 5, 3, 36),

-- Chapter 6: Counting
(N'Basic Counting Rules', N'Addition and multiplication principles in combinatorics.', NULL, 5, 1, 37),
(N'Recurrence in Counting', N'Solving recurrence relations, including divide-and-conquer.', NULL, 5, 2, 37),

-- Chapter 7: Graph Theory
(N'Introduction to Graphs', N'Terminologies, classifications, and special graphs.', NULL, 5, 1, 38),
(N'Graph Representations and Isomorphism', N'Using matrices and checking graph isomorphism.', NULL, 5, 2, 38),
(N'Paths and Circuits in Graphs', N'Counting paths and identifying Eulerian/Hamiltonian paths.', NULL, 5, 3, 38),
(N'Dijkstra Algorithm and Shortest Path', N'Finding shortest paths in weighted graphs.', NULL, 5, 4, 38),

-- Chapter 8: Trees
(N'Tree Structure and Properties', N'Basic definitions, m-ary trees, and checking tree validity.', NULL, 5, 1, 39),
(N'Binary Search Trees and Decision Trees', N'Building and analyzing BST and decision trees.', NULL, 5, 2, 39),
(N'Huffman Coding and Prefix Trees', N'Constructing prefix codes and optimal trees.', NULL, 5, 3, 39),
(N'Tree Traversals and Notation', N'Preorder, inorder, postorder traversal, postfix evaluation.', NULL, 5, 4, 39),
(N'DFS, BFS and Spanning Trees', N'Depth-first, breadth-first traversal and spanning tree generation.', NULL, 5, 5, 39),
(N'Prim and Kruskal Algorithms', N'Finding minimum spanning trees using MST algorithms.', NULL, 5, 6, 39);

--SSG104
INSERT INTO category (name, description) VALUES
    (N'Soft Skills', N'Courses focused on communication, collaboration, and personal development.');

INSERT INTO course (
    name, description, price, type, semester, category_id
) VALUES (
             N'Communication and In-Group Working Skills (SSG104)',
             N'This course covers both working in groups and communication skills in academic and professional contexts.',
             99000, -- miễn phí hoặc không đề cập giá
             'Core', -- hoặc 'Elective' tùy trường hợp
             1, -- giả định là học kỳ 1
             6
         );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Foundations of Communication and Teamwork', 6, 1),
                                                        (N'Group Psychology and Conflict Management', 6, 2),
                                                        (N'Business Communication and Writing Skills', 6, 3),
                                                        (N'Leadership, Creativity, and Presentation Skills', 6, 4);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
                                                                                           (N'Introduction to Communication Theories', N'Understanding fundamental concepts in communication.', NULL, 6, 1, 40),
                                                                                           (N'Effective Team Collaboration', N'Techniques for working efficiently in a group.', NULL, 6, 2, 40),

                                                                                           (N'Psychology of Group Dynamics', N'Analyzing how group behavior affects communication.', NULL, 6, 1, 41),
                                                                                           (N'Conflict Resolution Strategies', N'Methods to handle conflicts and promote cohesion.', NULL, 6, 2, 41),

                                                                                           (N'Business Email and Report Writing', N'Practice writing formal emails and reports.', NULL, 6, 1, 42),
                                                                                           (N'Organizing and Leading Meetings', N'Skills to plan and lead a business meeting.', NULL, 6, 2, 42),

                                                                                           (N'Leadership Models and Critical Thinking', N'Different leadership models and their applications.', NULL, 6, 1, 43),
                                                                                           (N'Professional Presentation Skills', N'Designing and delivering persuasive presentations.', NULL, 6, 2, 43);

--NWC 204
INSERT INTO category (name, description) VALUES
    (N'Networking', N'Courses related to computer networks, protocols, and network configurations.');

INSERT INTO course (
    name, description, price, type, semester, category_id
) VALUES (
             N'Computer Networking (NWC204)',
             N'This course covers fundamental networking concepts based on CCNA curriculum, including IP addressing, Ethernet, routers, switches, and basic network security.',
             299000, -- miễn phí hoặc không đề cập giá
             'Core',
             2, -- giả định học kỳ 2
             7
         );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Networking Fundamentals and Protocols', 7, 1),
                                                        (N'IP Addressing and Subnetting', 7, 2),
                                                        (N'Network Device Configuration and Security', 7, 3),
                                                        (N'Network Testing and Troubleshooting Tools', 7, 4);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
                                                                                           (N'Introduction to Network Technologies', N'Overview of modern network technologies and architectures.', NULL, 7, 1, 44),
                                                                                           (N'Network Protocols and Models', N'How protocols enable device communication in networks.', NULL, 7, 2, 44),

                                                                                           (N'IPv4 Addressing and Subnetting', N'Calculating and applying IPv4 subnet schemes.', NULL, 7, 1, 45),
                                                                                           (N'IPv6 Addressing Basics', N'Implementing IPv6 schemes and addressing.', NULL, 7, 2, 45),

                                                                                           (N'Configuring Routers and Switches', N'Settings including passwords, IPs, hardening for security.', NULL, 7, 1, 46),
                                                                                           (N'Device Hardening and Network Security', N'Enhancing security features on network devices.', NULL, 7, 2, 46),

                                                                                           (N'Using ICMP and Connectivity Tools', N'Employ various tools to test network connectivity.', NULL, 7, 1, 47),
                                                                                           (N'Troubleshooting with Packet Tracer and Labs', N'Hands-on labs and activities for troubleshooting.', NULL, 7, 2, 47);


--OSG202
INSERT INTO category (name, description) VALUES
    (N'Computer Science', N'Courses related to core computer science topics including operating systems, programming, and software engineering.');

INSERT INTO course (
    name, description, price, type, semester, category_id
) VALUES (
             N'Operating System (OSG202)',
             N'This course introduces fundamental concepts of operating systems including process, memory, file systems, I/O, deadlock, Linux shell commands, and basic C/C++ on Linux.',
             199000, -- không đề cập giá
             'Core',
             2, -- giả định học kỳ 2
             8
         );

INSERT INTO chapter (title, course_id, order_index) VALUES
                                                        (N'Operating System Fundamentals', 8, 1),
                                                        (N'Process and Memory Management', 8, 2),
                                                        (N'File Systems and I/O Management', 8, 3),
                                                        (N'Linux Shell and Programming Basics', 8, 4);

INSERT INTO lesson (title, description, video_url, course_id, order_index, chapter_id) VALUES
                                                                                           (N'Introduction to Operating Systems', N'Roles, concepts, mechanisms, and main problems of operating systems.', NULL, 8, 1, 48),
                                                                                           (N'Deadlock Concepts and Management', N'Understanding deadlocks and handling them in OS.', NULL, 8, 2, 48),

                                                                                           (N'Process Management Basics', N'Concepts of process management including scheduling and states.', NULL, 8, 1, 49),
                                                                                           (N'Memory Management Principles', N'Management of memory, paging, segmentation.', NULL, 8, 2, 49),

                                                                                           (N'File System Concepts', N'Overview of file systems and management.', NULL, 8, 1, 50),
                                                                                           (N'I/O Systems and Devices', N'Operating system interaction with input/output hardware.', NULL, 8, 2, 50),

                                                                                           (N'Linux Shell Commands', N'Using basic shell commands fluently in Linux environment.', NULL, 8, 1, 51),
                                                                                           (N'Basic C/C++ Programming on Linux', N'Fundamental C/C++ programming and shell scripting basics.', NULL, 8, 2, 51),
                                                                                           (N'Using AI Tools to Explore OS Concepts', N'Applying AI tools like ChatGPT to deepen understanding of OS components.', NULL, 8, 3, 51);



