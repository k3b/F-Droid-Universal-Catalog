-- V1__initial.sql creates hsqldb for fdroid
create TABLE repo (
  id INT NOT NULL,
  name VARCHAR(255),
  "TIMESTAMP" BIGINT,
  version BIGINT,
  maxage BIGINT,
  icon VARCHAR(255),
  address VARCHAR(255),
  description VARCHAR(255),
  CONSTRAINT pk_repo PRIMARY KEY (id),
  CONSTRAINT ak_repo UNIQUE (name)
);

create TABLE app (
  id INT NOT NULL,
  repo_id INT,
  package_name VARCHAR(255),
  changelog VARCHAR(255),
  suggested_version_name VARCHAR(255),
  suggested_version_code VARCHAR(255),
  issue_tracker VARCHAR(255),
  license VARCHAR(255),
  source_code VARCHAR(255),
  web_site VARCHAR(255),
  added BIGINT,
  icon VARCHAR(255),
  last_updated BIGINT,
  search_name VARCHAR(8000),
  search_summary VARCHAR(8000),
  search_description VARCHAR(256000),
  search_whats_new VARCHAR(8000),
  search_version VARCHAR(255),
  search_sdk VARCHAR(255),
  search_signer VARCHAR(255),
  search_category VARCHAR(255),
  CONSTRAINT pk_app PRIMARY KEY (id),
  CONSTRAINT ak_app UNIQUE (repo_id,package_name),
  CONSTRAINT fk_app_repo FOREIGN KEY (repo_id) REFERENCES repo(id)  ON delete CASCADE
);

create TABLE category (
  id INT NOT NULL,
  name VARCHAR(255),
  CONSTRAINT pk_category PRIMARY KEY (id),
  CONSTRAINT ak_category UNIQUE (name)
);

create TABLE app_category (
  id INT NOT NULL,
  app_id INT,
  category_id INT,
  CONSTRAINT pk_appCat PRIMARY KEY (id),
  CONSTRAINT ak_appCat UNIQUE (app_id,category_id),
  CONSTRAINT fk_appCat_app FOREIGN KEY (app_id) REFERENCES app(id)  ON delete CASCADE,
  CONSTRAINT fk_appCat_cat FOREIGN KEY (category_id) REFERENCES category(id)  ON delete CASCADE
);

create TABLE locale (
  id INT NOT NULL,
  code VARCHAR(255),
  symbol VARCHAR(255),
  CONSTRAINT pk_locale PRIMARY KEY (id),
  CONSTRAINT ak_locale UNIQUE (code)
);

create TABLE localized (
  id INT NOT NULL,
  app_id INT,
  locale_id INT,
  name VARCHAR(255),
  summary VARCHAR(255),
  description VARCHAR(8000),
  icon VARCHAR(255),
  video VARCHAR(255),
  whats_new VARCHAR(8000),
  CONSTRAINT pk_localized PRIMARY KEY (id),
  CONSTRAINT ak_localized UNIQUE (app_id,locale_id),
  CONSTRAINT fk_localized_app FOREIGN KEY (app_id) REFERENCES app(id)  ON delete CASCADE,
  CONSTRAINT fk_localized_cat FOREIGN KEY (locale_id) REFERENCES locale(id)  ON delete CASCADE
);

create TABLE App_Version (
  id INT NOT NULL,
  app_id INT,
  added BIGINT,
  apk_name VARCHAR(255),
  min_sdk_version BIGINT,
  target_sdk_version BIGINT,
  max_sdk_version BIGINT,
  version_code BIGINT,
  version_name VARCHAR(255),
  hash VARCHAR(255),
  hash_type VARCHAR(255),
  sig VARCHAR(255),
  signer VARCHAR(255),
  size BIGINT,
  srcname VARCHAR(255),
  CONSTRAINT pk_version PRIMARY KEY (id),
  CONSTRAINT ak_version UNIQUE (app_id,version_code),
  CONSTRAINT fk_version_app FOREIGN KEY (app_id) REFERENCES app(id) ON delete CASCADE,
);

create TABLE test_entity (
  id INT NOT NULL,
  family_name VARCHAR(255),
  name VARCHAR(255),
  CONSTRAINT pk_testentity PRIMARY KEY (id)
);

create view app_search as
(select id, PACKAGE_NAME, PACKAGE_NAME search, 1000 score from app) union
(select id, PACKAGE_NAME, SEARCH_NAME search, 1000 score from app) union
(select id, PACKAGE_NAME, SEARCH_SUMMARY search, 100 score from app) union
(select id, PACKAGE_NAME, SEARCH_Whats_New search, 10 score from app) union
(select id, PACKAGE_NAME, SEARCH_Category search, 50 score from app) union
(select id, PACKAGE_NAME, SEARCH_DESCRIPTION search, 1 score from app)
;
