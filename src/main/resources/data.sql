-- Insert sample assignments
INSERT INTO assignments (
    course_id, title, description, instructions, due_date,
    created_by, created_at, updated_at
) VALUES
(1, 'Java Basics Assignment', 'Intro Java tasks', 'Complete all questions', '2025-02-10 23:59:59', 9001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'OOP Principles', 'Explanation and coding tasks', 'Upload PDF + code', '2025-02-15 23:59:59', 9001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Database Design', 'ERD + SQL schema', 'Submit SQL file', '2025-03-01 23:59:59', 9002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample submissions
INSERT INTO submissions (
    assignment_id, course_id, student_id, file_url,
    submission_notes, grade, submission_date, status
) VALUES
(1, 1, 5001, 'https://cdn.lms.com/submissions/1_5001.pdf',
 'Completed all tasks', 'A', CURRENT_TIMESTAMP, 'GRADED'),

(1, 1, 5002, 'https://cdn.lms.com/submissions/1_5002.zip',
 'Please check question 4', NULL, CURRENT_TIMESTAMP, 'SUBMITTED'),

(3, 2, 5010, 'https://cdn.lms.com/submissions/3_5010.sql',
 'Database schema attached', NULL, CURRENT_TIMESTAMP, 'SUBMITTED');
 
 
-- batch 2
-- Sample assignments 2

-- Assignments for Java Basics
INSERT INTO assignments (
    course_id, title, description, instructions, due_date,
    created_by, created_at, updated_at
) VALUES
-- Java Basics Assignments
(1, 'Java Variables and Data Types', 
'Introduction to Java variables, primitive types, and object references. Students will practice declaring variables and performing type conversions.', 
'Write Java programs demonstrating variable declarations, initializations, and type casting. Submit a ZIP file with your code.', 
'2025-02-05 23:59:59', 9001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1, 'Control Flow in Java', 
'Students will explore conditional statements, loops, and control structures in Java through practical examples.', 
'Create Java programs demonstrating if-else, switch, for, while, and do-while statements. Submit all source files in a ZIP.', 
'2025-02-12 23:59:59', 9001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1, 'Object-Oriented Programming Basics', 
'Learn the fundamentals of OOP: classes, objects, methods, inheritance, and encapsulation in Java.', 
'Design simple Java classes implementing inheritance and encapsulation. Include a main method demonstrating object interactions.', 
'2025-02-20 23:59:59', 9001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1, 'Java Collections Framework', 
'Introduction to collections: Lists, Sets, Maps, and how to use them effectively in Java applications.', 
'Write Java programs demonstrating use of ArrayList, HashSet, and HashMap. Include comments explaining the logic.', 
'2025-02-28 23:59:59', 9001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Spring Boot Essentials Assignments
(2, 'Spring Boot Project Setup', 
'Students will learn to create a Spring Boot application from scratch including dependencies, main class, and basic REST endpoints.', 
'Set up a Spring Boot project, create one controller with GET endpoints, and submit the project as a ZIP.', 
'2025-03-05 23:59:59', 9002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'Dependency Injection and Beans', 
'Explore dependency injection and bean management in Spring Boot with practical coding exercises.', 
'Implement services and repositories using @Service and @Repository annotations. Demonstrate dependency injection in your main application.', 
'2025-03-12 23:59:59', 9002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'REST API Development', 
'Students will design and implement a REST API with CRUD operations using Spring Boot.', 
'Create REST endpoints for a simple entity with GET, POST, PUT, DELETE. Include JSON request/response examples. Submit project ZIP.', 
'2025-03-20 23:59:59', 9002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'Spring Data JPA', 
'Introduction to JPA repositories and database integration using Spring Data JPA.', 
'Create entities, repositories, and demonstrate CRUD operations via a service class. Submit project ZIP.', 
'2025-03-28 23:59:59', 9002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Database Design Assignments (Course 2)
(2, 'ERD Design Assignment', 
'Students will design an Entity-Relationship Diagram for a simple business scenario.', 
'Draw the ERD, identify primary and foreign keys, and submit as PDF.', 
'2025-04-05 23:59:59', 9002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'SQL Schema Creation', 
'Translate ERD into SQL DDL scripts creating tables, relationships, and constraints.', 
'Write SQL scripts to create tables with proper constraints. Submit SQL file.', 
'2025-04-12 23:59:59', 9002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
