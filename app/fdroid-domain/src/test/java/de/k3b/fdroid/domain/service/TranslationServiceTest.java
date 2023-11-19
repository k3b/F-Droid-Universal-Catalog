/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of org.fdroid project.
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
package de.k3b.fdroid.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;

import de.k3b.fdroid.domain.entity.Translation;
import de.k3b.fdroid.domain.repository.TranslationRepository;

public class TranslationServiceTest {
    public static final int ID = 1;
    public static final String LOCALE_ID = "de";
    public static final String TYP = "xx";
    TranslationRepository repository;
    TranslationService sut;

    @Before
    public void setUp() throws Exception {
        repository = mock(TranslationRepository.class);
        sut = new TranslationService(TYP, repository);
    }

    @Test
    public void get_existing_shouldGet() {
        create("Hello");
        assertEquals("Hello", sut.getLocalizedText(ID, LOCALE_ID));
    }

    @Test
    public void get_notExisting_shouldGetNull() {
        assertNull(sut.getLocalizedText(ID, LOCALE_ID));
    }

    @Test
    public void save_existing_shouldUpdate() {
        create("Hello");
        sut.save(ID, LOCALE_ID, "some text");
        verify(repository, times(ID)).update(any(Translation.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save_existingNull_shouldDelete() {
        create("Hello");
        sut.save(ID, LOCALE_ID, null);
        verify(repository, times(ID)).delete(any(Translation.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save_new_shouldInsert() {
        sut.save(ID, LOCALE_ID, "some text");
        verify(repository, times(ID)).insert(any(Translation.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save_newEmpty_noInsert() {
        sut.save(ID, LOCALE_ID, null);
        verifyNoMoreInteractions(repository);
    }

    private Translation create(String value) {
        Translation t = new Translation(TYP, ID, LOCALE_ID);
        t.setLocalizedText(value);
        sut.put(t);
        return t;
    }
}