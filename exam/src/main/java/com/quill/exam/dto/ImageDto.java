package com.quill.exam.dto;

import com.quill.exam.domain.Image;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDto {
    private String fileName;
    private Boolean hasTag;

    @Builder
    public ImageDto(Image image) {
        this.fileName = image.getFileName();
        this.hasTag = image.isHasTag();
    }
}
