ALTER TABLE App_Version add nativecode VARCHAR(255);

CREATE TABLE hardware_profile (
  id INT NOT NULL,
  name VARCHAR(255),
  sdk_version BIGINT,
  nativecode VARCHAR(255),
  CONSTRAINT pk_hardwareprofile PRIMARY KEY (id),
  CONSTRAINT ak_hardwareprofile UNIQUE (name)
);

CREATE TABLE app_hardware (
  id INT NOT NULL,
  app_id INT,
  hardware_profile_id INT,
  deleteIfNotCompatible INT,

  -- ProfileCommon max (with no prefix)
  added INT,
  apkName VARCHAR(255),
  versionCode INT,
  versionName VARCHAR(255),
  size INT,
  
  -- ProfileCommon min (with prefix 'min_')
  min_added INT,
  min_apkName VARCHAR(255),
  min_versionCode INT,
  min_versionName VARCHAR(255),
  min_size INT,

  CONSTRAINT pk_app_hardware PRIMARY KEY (id),
  CONSTRAINT fk_app_apphardware FOREIGN KEY (app_id) REFERENCES app(id)  ON delete CASCADE,
  CONSTRAINT fk_hardwareprofile_apphardware FOREIGN KEY (hardware_profile_id) REFERENCES hardware_profile(id)  ON delete CASCADE
);
