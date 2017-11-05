CREATE TABLE IF NOT EXISTS sequence (
    seq_name        VARCHAR(50) NOT NULL,
    next_val        BIGINT      NOT NULL,
    increment_val   INT         NOT NULL DEFAULT 1,
    PRIMARY KEY (seq_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


