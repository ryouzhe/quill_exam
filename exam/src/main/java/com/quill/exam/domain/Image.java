package com.quill.exam.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private boolean hasTag = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTEXT_ID")
    private Context context;

    // 양방향 연관관계 편의 메소드
    public void setContext(Context context) {
        this.context = context;
        if(!context.getImages().contains(this)) {
            context.getImages().add(this);
        }
    }
}
