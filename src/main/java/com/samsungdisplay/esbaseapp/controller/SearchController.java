package com.samsungdisplay.esbaseapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.samsungdisplay.esbaseapp.document.ProductDocument;
import com.samsungdisplay.esbaseapp.service.ProductSearchService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/")
    public String index() {
        return "main";  // 템플릿 동일하게 사용
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice", required = false) Integer minPrice,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating,
            @RequestParam(name = "sort", required = false) String sortOption,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {
        long start = System.currentTimeMillis();

        Page<ProductDocument> products = productSearchService.search(
                keyword, minPrice, maxPrice, minRating, maxRating, sortOption, page, size
        );

        long elapsed = System.currentTimeMillis() - start;

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minRating", minRating);
        model.addAttribute("maxRating", maxRating);
        model.addAttribute("sort", sortOption);
        model.addAttribute("searchTime", elapsed);

        return "main";
    }
}