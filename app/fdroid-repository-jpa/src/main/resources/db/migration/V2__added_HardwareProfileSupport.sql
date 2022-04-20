CREATE TABLE HardwareProfile (
  id INT NOT NULL,
  name VARCHAR(255),
  sdkVersion INT,
  nativecode VARCHAR(255),
  deleteIfNotCompatible BOOLEAN default false,
  CONSTRAINT pk_hardwareprofile PRIMARY KEY (id),
  CONSTRAINT ak_hardwareprofile UNIQUE (name)
);

CREATE TABLE AppHardware (
  id INT NOT NULL,
  appId INT,
  hardwareProfileId INT,

  -- ProfileCommon max (with no prefix)
  added BIGINT,
  apkName VARCHAR(255),
  versionCode INT,
  versionName VARCHAR(255),
  size INT,
  
  -- ProfileCommon min (with prefix 'min_')
  minAdded BIGINT,
  minApkName VARCHAR(255),
  minVersionCode INT,
  minVersionName VARCHAR(255),
  minSize INT,

  CONSTRAINT pk_AppHardware PRIMARY KEY (id),
  CONSTRAINT fk_app_apphardware FOREIGN KEY (appId) REFERENCES App(id)  ON delete CASCADE,
  CONSTRAINT fk_hardwareprofile_apphardware FOREIGN KEY (hardwareProfileId) REFERENCES HardwareProfile(id)  ON delete CASCADE
);
