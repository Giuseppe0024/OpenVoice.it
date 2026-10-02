package it.aulab.final_project_giuseppe.repositories;

import org.springframework.data.repository.ListCrudRepository;

import it.aulab.final_project_giuseppe.models.Category;

public interface CategoryRepository extends ListCrudRepository<Category, Long>{

}
