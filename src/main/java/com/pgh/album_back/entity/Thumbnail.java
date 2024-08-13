package com.pgh.album_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Thumbnail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="thumbnail_id")
    private Long id;

    @Column(nullable = false)
    private String imagePath;
}
