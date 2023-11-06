/*
 * Copyright (c) 2023 by k3b.
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

package de.k3b.fdroid.servingwebcontent;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.repository.LocaleRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Locale(s) or languages", description = "Get available locales/language-translations for Android Apps",
        externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Localized"))
@SuppressWarnings("unused")
public class LocaleController {
    private final LocaleRepository localeRepository;

    public LocaleController(LocaleRepository localeRepository) {

        this.localeRepository = localeRepository;
        // localeRepository.findAll()
    }

    @ResponseBody
    @GetMapping(value = WebConfig.API_ROOT + "/locale", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Locale> localeList() {
        return localeRepository.findAll();
    }
}
