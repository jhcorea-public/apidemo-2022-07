package org.practice.haeng.apidemo.search.webapp.service;

import java.util.List;

import org.practice.haeng.apidemo.search.common.model.KeywordCount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class KeywordController {

    private final KeywordService keywordService;
    private final KeywordRankService keywordRankService;

    public KeywordController(KeywordService keywordService,
        KeywordRankService keywordRankService) {
        this.keywordService = keywordService;
        this.keywordRankService = keywordRankService;
    }

    @GetMapping
    public List<KeywordApiResponse> search(@RequestParam String keyword) {
        return keywordService.search(keyword);
    }

    @GetMapping("/rank10")
    public List<KeywordCount> rank() {
        return keywordRankService.selectTopKeyword(10);
    }

}
