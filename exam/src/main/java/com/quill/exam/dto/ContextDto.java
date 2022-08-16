package com.quill.exam.dto;

import com.quill.exam.domain.Context;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContextDto {
    private String title;
    private String content;

    @Builder
    public ContextDto(Context context) {
        this.title = context.getTitle();
        this.content = context.getContent();
    }
}
