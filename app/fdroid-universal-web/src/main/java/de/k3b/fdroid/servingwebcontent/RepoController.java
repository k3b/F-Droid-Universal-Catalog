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
package de.k3b.fdroid.servingwebcontent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.web.service.RenamingService;

@Controller
public class RepoController {
    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private RenamingService renamingService;

    @GetMapping("/Repo/repo")
    public String repoList(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        model.addAttribute("r", renamingService.templateValue(
                repoRepository.findAll()));
        return "Repo/repo";
    }

}