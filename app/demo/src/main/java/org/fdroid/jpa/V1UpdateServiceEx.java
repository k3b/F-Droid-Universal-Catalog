package org.fdroid.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.k3b.fdroid.room.db.AppCategoryRepository;
import de.k3b.fdroid.room.db.AppRepository;
import de.k3b.fdroid.room.db.CategoryRepository;
import de.k3b.fdroid.room.db.LocaleRepository;
import de.k3b.fdroid.room.db.LocalizedRepository;
import de.k3b.fdroid.room.db.RepoRepository;
import de.k3b.fdroid.room.db.VersionRepository;
import de.k3b.fdroid.room.db.v1.V1UpdateService;

@Service
public class V1UpdateServiceEx extends V1UpdateService {
    private static final Logger log = LoggerFactory.getLogger(V1UpdateServiceEx.class);

    public V1UpdateServiceEx(RepoRepository repoRepository, AppRepository appRepository, CategoryRepository categoryRepository, AppCategoryRepository appCategoryRepository, VersionRepository versionRepository, LocalizedRepository localizedRepository, LocaleRepository localeRepository) {
        super(repoRepository, appRepository, categoryRepository, appCategoryRepository, versionRepository, localizedRepository, localeRepository);
    }

    @Override
    protected String log(String s) {
        log.info(s);
        return s;
    }


}
