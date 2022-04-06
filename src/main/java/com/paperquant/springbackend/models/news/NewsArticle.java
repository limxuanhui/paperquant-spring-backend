package com.paperquant.springbackend.models.news;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static javax.persistence.GenerationType.AUTO;

public class NewsArticle {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @NotBlank(message = "Author cannot be empty")
    private String author;
    @NotBlank(message = "Headline cannot be empty")
    private String headline;
    @NotBlank(message = "News body cannot be empty")
    private String body;
}
