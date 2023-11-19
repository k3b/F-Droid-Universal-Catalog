-- V1__initial.sql creates hsqldb for fdroid
CREATE TABLE Repo (
  id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1),
  name VARCHAR(255) NOT NULL,
  timestamp BIGINT default 0,
  address VARCHAR(255) NOT NULL,
  icon VARCHAR(255),
  repoTyp VARCHAR(2),
  autoDownloadEnabled BOOLEAN default false,
  description VARCHAR(255),
  mirrors  VARCHAR(8000),
  lastUsedDownloadMirror VARCHAR(255),
  lastUsedDownloadDateTimeUtc BIGINT default 0 NOT NULL,
  lastAppCount INT default 0 NOT NULL,
  lastVersionCount INT default 0 NOT NULL,
  jarSigningCertificate VARCHAR(8000),
  jarSigningCertificateFingerprint VARCHAR(255),
  downloadTaskId VARCHAR(255),
  CONSTRAINT pk_repo PRIMARY KEY (id),
  CONSTRAINT ak_repo UNIQUE (name)
);

INSERT INTO repo(name, address, autoDownloadEnabled,repoTyp) VALUES('f-droid.org','https://f-droid.org/repo',true,'s');
INSERT INTO repo(name, address,repoTyp) VALUES('f-droid.org/archive','https://f-droid.org/archive','a');
INSERT INTO repo(name, address,repoTyp) VALUES('apt.izzysoft.de','https://apt.izzysoft.de/fdroid/repo','n');
INSERT INTO repo(name, address,repoTyp) VALUES('guardianproject.info','https://guardianproject.info/fdroid/repo','s');

CREATE TABLE App (
  id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1),
  packageName VARCHAR(255) NOT NULL,
  changelog VARCHAR(255),
  suggestedVersionName VARCHAR(255),
  suggestedVersionCode VARCHAR(255),
  issueTracker VARCHAR(255),
  license VARCHAR(255),
  sourceCode VARCHAR(255),
  webSite VARCHAR(255),
  added BIGINT,
  icon VARCHAR(255),
  lastUpdated BIGINT,
  searchName VARCHAR(8000),
  searchSummary VARCHAR(8000),
  searchDescription VARCHAR(256000),
  searchWhatsNew VARCHAR(8000),
  searchVersion VARCHAR(255),
  searchSdk VARCHAR(255),
  searchSigner VARCHAR(8000),
  searchCategory VARCHAR(8000),
  resourceRepoId INT,
  CONSTRAINT fk_app_repo FOREIGN KEY (resourceRepoId) REFERENCES Repo(id)  ON DELETE SET NULL,
  CONSTRAINT pk_app PRIMARY KEY (id),
  CONSTRAINT ak_app UNIQUE (packageName)
);

CREATE TABLE Category (
  id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1),
  name VARCHAR(255) NOT NULL,
  CONSTRAINT pk_category PRIMARY KEY (id),
  CONSTRAINT ak_category UNIQUE (name)
);

CREATE TABLE AppCategory (
  id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1),
  appId INT NOT NULL,
  categoryId INT NOT NULL,
  CONSTRAINT pk_appCat PRIMARY KEY (id),
  CONSTRAINT ak_appCat UNIQUE (appId,categoryId),
  CONSTRAINT fk_appCat_app FOREIGN KEY (appId) REFERENCES App(id)  ON delete CASCADE,
  CONSTRAINT fk_appCat_cat FOREIGN KEY (categoryId) REFERENCES Category(id)  ON delete CASCADE
);

CREATE TABLE Locale (
  id VARCHAR(8) NOT NULL,
  symbol VARCHAR(255),
  nameEnglish VARCHAR(255),
  nameNative VARCHAR(255),
  languagePriority INT default 0,
  CONSTRAINT pk_locale PRIMARY KEY (id)
);

CREATE TABLE Localized (
  id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1),
  appId INT NOT NULL,
  localeId VARCHAR(8) NOT NULL,
  name VARCHAR(255),
  summary VARCHAR(255),
  description VARCHAR(8000),
  icon VARCHAR(255),
  phoneScreenshotDir VARCHAR(255),
  phoneScreenshots VARCHAR(8000),
  video VARCHAR(255),
  whatsNew VARCHAR(8000),
  CONSTRAINT pk_localized PRIMARY KEY (id),
  CONSTRAINT ak_localized UNIQUE (appId,localeId),
  CONSTRAINT fk_localized_app FOREIGN KEY (appId) REFERENCES App(id)  ON delete CASCADE,
  CONSTRAINT fk_localized_locale FOREIGN KEY (localeId) REFERENCES Locale(id)  ON delete CASCADE
);

CREATE TABLE AppVersion (
  id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1),
  appId INT NOT NULL,
  repoId INT NOT NULL,
  added BIGINT,
  apkName VARCHAR(255),
  minSdkVersion INT,
  targetSdkVersion INT,
  maxSdkVersion INT,
  versionCode INT  NOT NULL,
  versionName VARCHAR(255),
  hash VARCHAR(255),
  hashType VARCHAR(255),
  signer VARCHAR(255),
  size INT,
  nativecode VARCHAR(255),
  srcname VARCHAR(255),
  CONSTRAINT pk_version PRIMARY KEY (id),
  CONSTRAINT ak_version UNIQUE (appId,repoId,versionCode),
  CONSTRAINT fk_version_app FOREIGN KEY (appId) REFERENCES App(id) ON delete CASCADE,
  CONSTRAINT fk_version_repo FOREIGN KEY (repoId) REFERENCES Repo(id)  ON delete CASCADE
);

CREATE TABLE TestEntity (
  id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 100, INCREMENT BY 1),
  familyName VARCHAR(255),
  name VARCHAR(255),
  CONSTRAINT pk_testentity PRIMARY KEY (id)
);

CREATE VIEW AppSearch AS
    select id, packageName, packageName search, 1000 score from App union
    select id, packageName, searchName search, 1000 score from App union
    select id, packageName, searchSummary search, 100 score from App union
    select id, packageName, searchWhatsNew search, 10 score from App union
    select id, packageName, searchCategory search, 50 score from App union
    select id, packageName, searchDescription search, 1 score from App
    ;
