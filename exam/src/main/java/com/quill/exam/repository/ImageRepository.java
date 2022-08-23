package com.quill.exam.repository;

import com.quill.exam.domain.Context;
import com.quill.exam.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // Context로 Image 리스트 찾기
    List<Image> findByContext(Context context);
}
