package de.k3b.fdroid.v1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import de.k3b.fdroid.domain.interfaces.AppCategoryRepository;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.CategoryRepository;
import de.k3b.fdroid.domain.interfaces.LocaleRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.domain.interfaces.VersionRepository;

@Service
@Transactional
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
