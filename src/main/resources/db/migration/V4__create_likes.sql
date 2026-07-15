CREATE TABLE likes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    liker_name VARCHAR(30) NOT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_likes_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT uq_likes_post_liker UNIQUE (post_id, liker_name)
);

CREATE INDEX idx_likes_post_id ON likes (post_id);