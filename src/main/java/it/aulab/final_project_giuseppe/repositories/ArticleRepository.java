package it.aulab.final_project_giuseppe.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import it.aulab.final_project_giuseppe.models.Article;
import it.aulab.final_project_giuseppe.models.Category;
import it.aulab.final_project_giuseppe.models.User;

public interface ArticleRepository extends ListCrudRepository<Article, Long>{
    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
}
