DROP TABLE IF EXISTS keyword_count;

CREATE TABLE keyword_count (
    keyword      VARCHAR(100)     NOT NULL
  , search_count        BIGINT
  , update_date DATE
  , PRIMARY KEY (keyword)
);

CREATE INDEX idx_cnt ON keyword_count(search_count);
CREATE INDEX idx_date ON keyword_count(update_date);

