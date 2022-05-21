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
package de.k3b.fdroid.html.service;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.service.CacheService;

/**
 * Translates local url to repo relative url.
 * <p>
 * Example {{#getUrl}}{{apkName}}{{/getUrl}} becomes
 * https://apt.izzysoft.de/fdroid/repo/com.inator.calculator_4.apk
 * if context is Version
 */
public class GetUrlMustacheLamdaService implements Mustache.Lambda {
    private final CacheService<Repo> repoCacheService;

    public GetUrlMustacheLamdaService(CacheService<Repo> repoCacheService) {
        this.repoCacheService = repoCacheService;
    }

    @Override
    public void execute(Template.Fragment frag, Writer out) throws IOException {
        out.write(calculateUrl(frag.context(), frag.execute()));
    }

    private String calculateUrl(Object context, String parameter) {
        int repoId = 0;
        if (context instanceof Version) {
            repoId = ((Version) context).getRepoId();
        } else if (context instanceof App) {
            repoId = ((App) context).getResourceRepoId();
        } else if (context instanceof Repo) {
            repoId = ((Repo) context).getId();
        }
        Repo repo = repoCacheService.getItemById(repoId);
        if (repo != null) return repo.getUrl(parameter);

        return parameter;
    }

}
