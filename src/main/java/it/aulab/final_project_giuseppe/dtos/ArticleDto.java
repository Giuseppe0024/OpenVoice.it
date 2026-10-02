package it.aulab.final_project_giuseppe.dtos;

import java.time.LocalDate;

import it.aulab.final_project_giuseppe.models.Category;
import it.aulab.final_project_giuseppe.models.Image;
import it.aulab.final_project_giuseppe.models.User;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter 
@Getter 
@NoArgsConstructor 
public class ArticleDto {
    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDate publishDate;
    private User user;
    private Category category;
    private Image image;
}