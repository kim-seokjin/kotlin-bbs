CREATE TABLE comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    author_name VARCHAR(30) NOT NULL,
    deleted_at DATETIME NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE INDEX idx_comments_post_id ON comments (post_id);