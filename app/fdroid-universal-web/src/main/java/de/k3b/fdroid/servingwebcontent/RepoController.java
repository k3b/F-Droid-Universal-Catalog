/*
 * Copyright (c) 2022-2023 by k3b.
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

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.service.RepoIconService;

@Controller
public class RepoController {
    private final RepoIconService iconService;
    private final RepoRepository repoRepository;

    public RepoController(@Value("${de.k3b.fdroid.downloads.icons}") String iconsDir,
                          RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
        iconService = new RepoIconService(iconsDir);
    }

    @GetMapping("/Repo/repo")
    public String repoList(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        model.addAttribute("repo", repoRepository.findAll());
        return "Repo/repo_overview";
    }


    @ResponseBody
    @GetMapping(value = WebConfig.API_ROOT + "/repo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Repo> repoList() {
        return repoRepository.findAll();
    }

    @GetMapping("/Repo/repo/{id}")
    public String repoList(
            @PathVariable int id,
            Model model) {
        model.addAttribute("name", "world");
        model.addAttribute("repo", repoRepository.findById(id));
        return "Repo/repo_detail";
    }


    @GetMapping(value = "/Repo/repo/icons/repo_{id}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] appIcon(@PathVariable int id) {
        File file = iconService.getOrDownloadLocalImageFile(repoRepository.findById(id));
        if (file != null) {
            try (InputStream in = new FileInputStream(file)) {
                return IOUtils.toByteArray(in);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
        return null;
    }

}