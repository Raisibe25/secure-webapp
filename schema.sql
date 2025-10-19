CREATE TABLE IF NOT EXISTS users (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT,
  username      VARCHAR(50) NOT NULL UNIQUE,
  email         VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(60) NOT NULL,
  first_name    VARCHAR(60),
  last_name     VARCHAR(60),
  role          ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
  phone_enc     TEXT,
  prefs_enc     TEXT,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username,email,password_hash,first_name,last_name,role)
VALUES ('admin','admin@example.com','$2a$12$kqO52c2yfsYRgsINloF/re2.1OxNd2RlwUcLp9hMpEBvUVxKRXvtS','Site','Admin','ADMIN');
