/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1 the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.v1.service.util;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.CodeSigner;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Formatter;
import java.util.List;
import java.util.jar.JarEntry;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.util.StringUtil;
import de.k3b.fdroid.v1.service.V1JarException;

public class JarUtilities {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_UTIL);

    private static final String ERR_CERTIFICATE_SIGNERS_COUNT = "More than one code certificate signers are not allowed!";
    private static final String ERR_CERTIFICATE_COUNT = "More than one code signing certificates are not allowed!";
    private static final String ERR_CERTIFICATE_NOT_FOUND = "No code signing certificate found!";
    private static final String ERR_CERTIFICATE_MISMATCH = "Code signing certificate does not match!";
    private static final String ERR_CERTIFICATE_FINGERPRINT_MISMATCH = "Supplied code signing certificate fingerprint does not match!";
    private static final String ERR_CERTIFICATE_KEY_LEN = "Code signing certificate key was shorter than 256 bytes (";
    private static final String ERR_CERTIFICATE_CREATE_FINGERPRINT = "Unable to create Code signing certificate fingerprint";

    /**
     * Inspired by getSigningCertFromJar in
     * https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/IndexUpdater.java .
     * <p>
     * FDroid's index.jar is signed using a particular format and does not allow lots of
     * signing setups that would be valid for a regular jar.  This validates those
     * restrictions.
     */
    public static X509Certificate getSigningCertFromJar(JarEntry jarEntry) throws V1JarException {
        final CodeSigner[] codeSigners = jarEntry.getCodeSigners();
        if (codeSigners == null || codeSigners.length == 0) {
            throw new V1JarException(ERR_CERTIFICATE_NOT_FOUND);
        }
        /* we could in theory support more than 1, but as of now we do not */
        if (codeSigners.length > 1) {
            throw new V1JarException(ERR_CERTIFICATE_SIGNERS_COUNT);
        }
        List<? extends Certificate> certs = codeSigners[0].getSignerCertPath().getCertificates();
        if (certs.size() != 1) {
            throw new V1JarException(ERR_CERTIFICATE_COUNT);
        }
        return (X509Certificate) certs.get(0);
    }

    /**
     * Inspired by verifySigningCertificate in
     * https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/IndexV1Updater.java .
     * <p>
     * Verify that the signing certificate used to sign index-v1.jar
     * matches the signing stored in the database for this repo.  {@link Repo} and
     * {@code repo.signingCertificate} must be pre-loaded from the database before
     * running this, if this is an existing repo.  If the repo does not exist,
     * this will run the TOFU process.
     * <p>
     * Index V1 works with two copies of the signing certificate:
     * <li>in the downloaded jar</li>
     * <li>stored in the local database</li>
     * <p>
     * A new repo can be added with or without the fingerprint of the signing
     * certificate.  If no fingerprint is supplied, then do a pure TOFU and just
     * store the certificate as valid.  If there is a fingerprint, then first
     * check that the signing certificate in the jar matches that fingerprint.
     * <p>
     * This is also responsible for adding the {@link Repo} instance to the
     * database for the first time.
     * <p>
     *
     * @param rawCertFromJar the {@link X509Certificate} embedded in the downloaded jar
     */
    public static void verifySigningCertificate(Repo repo, X509Certificate rawCertFromJar) throws V1JarException {
        String certFromJar;
        try {
            certFromJar = Hex.encodeHexString(rawCertFromJar.getEncoded());
        } catch (CertificateEncodingException e) {
            certFromJar = Hex.encodeHexString(new byte[0]);
        }

        if (StringUtil.isEmpty(certFromJar)) {
            throw new V1JarException(repo, ERR_CERTIFICATE_NOT_FOUND);
        }

        if (repo.getJarSigningCertificate() == null) {
            // sticky certificate set on first run.
            repo.setJarSigningCertificate(certFromJar);
        } else {
            if (!repo.getJarSigningCertificate().equalsIgnoreCase(certFromJar)) {
                throw new V1JarException(repo, ERR_CERTIFICATE_MISMATCH);
            }
        }

        if (repo.getJarSigningCertificateFingerprint() != null) {
            String fingerprintFromJar = calcFingerprint(rawCertFromJar);
            if (!repo.getJarSigningCertificateFingerprint().equalsIgnoreCase(fingerprintFromJar)) {
                throw new V1JarException(repo, ERR_CERTIFICATE_FINGERPRINT_MISMATCH);
            }
        }
    }

    /*
     * Inspired by calcFingerprint in
     * https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/Utils.java .
     */
    public static String calcFingerprint(Certificate cert) {
        if (cert == null) {
            return null;
        }
        try {
            return calcFingerprint(cert.getEncoded());
        } catch (CertificateEncodingException e) {
            return null;
        }
    }

    /*
     * Inspired by calcFingerprint in
     * https://git.bubu1.eu/Bubu/fdroidclassic/-/blob/main/app/src/main/java/org/fdroid/fdroid/Utils.java .
     */
    public static String calcFingerprint(byte[] key) {
        if (key == null) {
            return null;
        }
        if (key.length < 256) {
            throw new V1JarException(ERR_CERTIFICATE_KEY_LEN + key.length + "), cannot be valid!");
        }
        String ret = null;
        try {
            // keytool -list -v gives you the SHA-256 fingerprint
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(key);
            byte[] fingerprint = digest.digest();
            Formatter formatter = new Formatter(new StringBuilder());
            for (byte aFingerprint : fingerprint) {
                formatter.format("%02X", aFingerprint);
            }
            ret = formatter.toString();
            formatter.close();
        } catch (Throwable e) {
            throw new V1JarException(ERR_CERTIFICATE_CREATE_FINGERPRINT, e);
        }
        return ret;
    }


}
