package com.paperquant.springbackend.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class News {
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
