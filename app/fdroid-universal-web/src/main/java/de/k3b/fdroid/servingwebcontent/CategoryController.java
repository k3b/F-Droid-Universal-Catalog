package de.k3b.fdroid.servingwebcontent;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import de.k3b.fdroid.domain.entity.Category;
import de.k3b.fdroid.domain.repository.CategoryRepository;

@Controller
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
        // categoryRepository.findAll()
    }

    @ResponseBody
    @GetMapping(value = WebConfig.API_ROOT + "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> categoryList() {
        return categoryRepository.findAll();
    }
}
