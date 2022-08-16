package com.quill.exam.repository;

import com.quill.exam.domain.Context;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContextRepository extends JpaRepository<Context, Long> {
}
