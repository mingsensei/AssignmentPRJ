package org.example.rf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "link", length = 255)
    private String link;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "vectorDbPath", length = 255)
    private String vectorDbPath;

    public Material() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public Long getChapterId() { return chapterId; }
    public void setChapterId(Long chapterId) { this.chapterId = chapterId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getVectorDbPath() { return vectorDbPath; }
    public void setVectorDbPath(String vectorDbPath) { this.vectorDbPath = vectorDbPath; }
}
