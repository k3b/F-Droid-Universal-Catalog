/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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

package de.k3b.fdroid.catalog.v1domain.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static de.k3b.fdroid.catalog.v1domain.util.JarUtilities.ERR_CERTIFICATE_FINGERPRINT_MISMATCH;
import static de.k3b.fdroid.catalog.v1domain.util.JarUtilities.ERR_CERTIFICATE_MISMATCH;
import static de.k3b.fdroid.catalog.v1domain.util.JarUtilities.verifyAndUpdateSigningCertificate;

import org.apache.commons.codec.binary.Hex2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Collection;

import de.k3b.fdroid.catalog.CatalogJarException;
import de.k3b.fdroid.domain.entity.Repo;

@RunWith(Parameterized.class)
public class JarUtilitiesTest {
    public static final String MY_CERT = "My Cert jköljkölfdsajkljkfsdououwierknlkcöspooirwejkljlljkcspicyhklkjkljjkjklsfduo"
            + "My Cert jköljkölfdsajkljkfsdououwierknlkcöspooirwejkljlljkcspicyhklkjkljjkjklsfduo"
            + "My Cert jköljkölfdsajkljkfsdououwierknlkcöspooirwejkljlljkcspicyhklkjkljjkjklsfduo"
            + "My Cert jköljkölfdsajkljkfsdououwierknlkcöspooirwejkljlljkcspicyhklkjkljjkjklsfduo";
    private static final Repo repo = new Repo();

    private static final Certificate exampleCert = createCert(MY_CERT);
    private static final String exampleCertString = Hex2.encodeHexString(MY_CERT.getBytes());
    private static final String exampleFingerprint = JarUtilities.calcFingerprint(repo, exampleCert);

    private final String exampleErrorMessage;
    private final Certificate exampleJarCert;
    private final String exampleDbCert;
    private final String exampleDbFingerprint;

    public JarUtilitiesTest(String errorMessage, Certificate jarCert, String dbCert, String dbFingerprint) {
        exampleErrorMessage = errorMessage;
        exampleJarCert = jarCert;
        exampleDbCert = dbCert;
        exampleDbFingerprint = dbFingerprint;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> input() {
        return Arrays.asList(new Object[][]{
                // errorMessage, jarCert, dbCert, dbFingerprint
                {null, null, null, null},
                {null, exampleCert, exampleCertString, exampleFingerprint},
                {null, exampleCert, null, exampleFingerprint},
                {null, exampleCert, exampleCertString, null},
                {null, exampleCert, null, null},

                {ERR_CERTIFICATE_MISMATCH, exampleCert, "wrong cert", null},
                {ERR_CERTIFICATE_FINGERPRINT_MISMATCH, exampleCert, exampleCertString, "wrong fingerprint"},
        });
    }

    private static Certificate createCert(String encoded) {
        Certificate exampleCert = null;
        try {
            exampleCert = Mockito.mock(Certificate.class);
            when(exampleCert.getEncoded()).thenReturn(encoded.getBytes());
//        } catch (CertificateEncodingException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Cert-mock kann nicht erstellt werden");
        }
        return exampleCert;
    }

    @Test
    public void verify() {
        repo.setJarSigningCertificate(exampleDbCert);
        repo.setJarSigningCertificateFingerprint(exampleDbFingerprint);
        try {
            verifyAndUpdateSigningCertificate(repo, exampleJarCert);
        } catch (CatalogJarException ex) {
            // ignored. error is also set in repo
        }
        assertEquals(exampleErrorMessage, repo.getLastErrorMessage());
    }
}