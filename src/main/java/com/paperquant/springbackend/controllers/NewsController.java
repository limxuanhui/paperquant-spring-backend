package com.paperquant.springbackend.controllers;

import com.paperquant.springbackend.services.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NewsController {

    private final NewsService newsService;


}
