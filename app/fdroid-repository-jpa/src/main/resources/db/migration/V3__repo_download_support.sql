-- V3__repo_download_support.sql
ALTER TABLE repo add mirrors  VARCHAR(255);
ALTER TABLE repo add lastUsedDownloadMirror VARCHAR(255);
ALTER TABLE repo add lastUsedDownloadDateTimeUtc INT default 0 NOT NULL ;
ALTER TABLE repo add jarSigningCertificate VARCHAR(255);
ALTER TABLE repo add jarSigningCertificateFingerprint VARCHAR(255);
