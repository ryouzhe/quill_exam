package com.quill.exam.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Context {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 10000)
    private String content;

    @Column(length = 3000)
    private String imgFilenames;

    @OneToMany(mappedBy = "context", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<Image>();
}
