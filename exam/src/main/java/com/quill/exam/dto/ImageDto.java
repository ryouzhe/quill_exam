package com.quill.exam.dto;

import com.quill.exam.domain.Image;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {
    private String path;
    private Boolean hasTag;

    @Builder
    public ImageDto(Image image) {
        this.path = image.getPath();
        this.hasTag = image.isHasTag();
    }
}
