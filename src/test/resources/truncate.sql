TRUNCATE TABLE COMMENT;
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE ARTICLE;
SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE COMMENT ALTER COLUMN id RESTART WITH 1;
ALTER TABLE ARTICLE ALTER COLUMN ARTICLE_ID RESTART WITH 1;
TRUNCATE TABLE SUB_EMOTION;
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE EMOTION;
SET FOREIGN_KEY_CHECKS=1;
TRUNCATE TABLE ARTICLE_EMOTION;
TRUNCATE TABLE ARTICLE_SUB_EMOTION;