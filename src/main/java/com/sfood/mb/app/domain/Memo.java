package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "memos")
public class Memo {

    @Id
    @Column(nullable = false, length = 150)
    private String memoId;

    @Column(nullable = false, length = 120)
    private String boardId;

    @Column(nullable = false, length = 50)
    private String memoTypeId;

    @Column(length = 5000)
    private String content;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @Column(nullable = false)
    private int zindex;

    @Column(nullable = false)
    private boolean isHide;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Memo() {
    }

    public Memo(String memoId,
                String boardId,
                String memoTypeId,
                String content,
                int x,
                int y,
                int width,
                int height,
                int zIndex) {
        this.memoId = memoId;
        this.boardId = boardId;
        this.memoTypeId = memoTypeId;
        this.content = content;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zindex = zIndex;
        this.isHide = false;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public void update(String content, Integer x, Integer y, Integer width, Integer height, Integer zIndex) {
        if (content != null) {
            this.content = content;
        }
        if (x != null) {
            this.x = x;
        }
        if (y != null) {
            this.y = y;
        }
        if (width != null) {
            this.width = width;
        }
        if (height != null) {
            this.height = height;
        }
        if (zIndex != null) {
            this.zindex = zIndex;
        }
    }

    public void hide() {
        this.isHide = true;
    }

    public String getMemoId() {
        return memoId;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getMemoTypeId() {
        return memoTypeId;
    }

    public String getContent() {
        return content;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getZIndex() {
        return zindex;
    }

    public boolean isHide() {
        return isHide;
    }
}
