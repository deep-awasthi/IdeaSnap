-- Password for all seed users is 'password' (bcrypt: $2a$10$y58fXgN31k8/X7.Lh1Z6h.F0pDk48n0C2E.D5oXvN/c3eFp1h8uQ6)

INSERT INTO users (id, username, email, password, bio, profile_image, role, created_at)
VALUES 
('a6d36e8b-70c8-47e0-b638-34f3b58fb177', 'admin', 'admin@ideasnap.com', '$2a$10$y58fXgN31k8/X7.Lh1Z6h.F0pDk48n0C2E.D5oXvN/c3eFp1h8uQ6', 'IdeaSnap Administrator', 'https://api.dicebear.com/7.x/bottts/svg?seed=admin', 'ADMIN', CURRENT_TIMESTAMP),
('b3034a78-2d6e-4e8c-8f19-90b9576a1622', 'alice', 'alice@ideasnap.com', '$2a$10$y58fXgN31k8/X7.Lh1Z6h.F0pDk48n0C2E.D5oXvN/c3eFp1h8uQ6', 'Senior Java Developer | Spring Boot enthusiast', 'https://api.dicebear.com/7.x/adventurer/svg?seed=alice', 'USER', CURRENT_TIMESTAMP),
('c8159b3f-1d89-4e56-9a2c-d9c02014b2d5', 'bob', 'bob@ideasnap.com', '$2a$10$y58fXgN31k8/X7.Lh1Z6h.F0pDk48n0C2E.D5oXvN/c3eFp1h8uQ6', 'AI Engineer | Python & Kubernetes developer', 'https://api.dicebear.com/7.x/adventurer/svg?seed=bob', 'USER', CURRENT_TIMESTAMP);

INSERT INTO posts (id, author_id, title, content, visibility, created_at, expires_at)
VALUES 
('d5059e7e-cf9d-4e92-ba78-2c262d1a3b11', 'b3034a78-2d6e-4e8c-8f19-90b9576a1622', 'Mastering Spring Boot 3.3 Virtual Threads', 'Spring Boot 3.2 introduced support for Java 21 virtual threads. To enable it, just set `spring.threads.virtual.enabled=true`. Under the hood, this configures the Tomcat executor to use virtual threads instead of platform threads. This drastically improves throughput for I/O bound operations. Happy coding! #Spring #Java', 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '1 HOUR', NULL),
('e6160f8f-df0e-4f03-cb89-3d373e2b4c22', 'c8159b3f-1d89-4e56-9a2c-d9c02014b2d5', 'Deploying LLMs on Kubernetes with KServe', 'KServe provides a Kubernetes Custom Resource Definition for serving machine learning models. It supports ML frameworks like PyTorch, TensorFlow, and HuggingFace out of the box. Highly recommended for scaling AI workflows. #AI #Kubernetes #DevOps', 'PUBLIC', CURRENT_TIMESTAMP - INTERVAL '2 HOUR', NULL);

INSERT INTO post_tags (post_id, tag)
VALUES 
('d5059e7e-cf9d-4e92-ba78-2c262d1a3b11', 'Spring'),
('d5059e7e-cf9d-4e92-ba78-2c262d1a3b11', 'Java'),
('e6160f8f-df0e-4f03-cb89-3d373e2b4c22', 'AI'),
('e6160f8f-df0e-4f03-cb89-3d373e2b4c22', 'Kubernetes'),
('e6160f8f-df0e-4f03-cb89-3d373e2b4c22', 'DevOps');

INSERT INTO follows (follower_id, following_id, created_at)
VALUES 
('c8159b3f-1d89-4e56-9a2c-d9c02014b2d5', 'b3034a78-2d6e-4e8c-8f19-90b9576a1622', CURRENT_TIMESTAMP);
