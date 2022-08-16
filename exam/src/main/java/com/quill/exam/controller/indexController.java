package com.quill.exam.controller;

import com.quill.exam.domain.Context;
import com.quill.exam.dto.ContextDto;
import com.quill.exam.repository.ContextRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class indexController {

    private final ContextRepository contextRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/content/save")
    public String contentSave(ContextDto contextDto) {
        Context contextEntity = new Context();
        contextEntity.setTitle(contextDto.getTitle());
        contextEntity.setContent(contextDto.getContent());

        contextRepository.save(contextEntity);

        return "redirect:/";
    }

}
