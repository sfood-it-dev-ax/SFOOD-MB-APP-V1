package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "memos")
public class Memo {
    @Id
    @Column(name = "memo_id", nullable = false, length = 255)
    private String memoId;

    @Column(name = "board_id", nullable = false, length = 255)
    private String boardId;

    @Column(name = "type_id", nullable = false, length = 50)
    private String typeId;

    @Column(name = "content")
    private String content;

    @Column(name = "pos_x", nullable = false)
    private Double posX;

    @Column(name = "pos_y", nullable = false)
    private Double posY;

    @Column(name = "width", nullable = false)
    private Double width;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "z_index", nullable = false)
    private Integer zIndex;

    @Column(name = "is_hide", nullable = false)
    private boolean hide;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public static Memo create(String memoId, String boardId, String typeId, String content, Double posX, Double posY,
                              Double width, Double height, Integer zIndex) {
        Memo memo = new Memo();
        memo.memoId = memoId;
        memo.boardId = boardId;
        memo.typeId = typeId;
        memo.content = content;
        memo.posX = posX;
        memo.posY = posY;
        memo.width = width;
        memo.height = height;
        memo.zIndex = zIndex;
        memo.hide = false;
        return memo;
    }

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public String getMemoId() {
        return memoId;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getContent() {
        return content;
    }

    public Double getPosX() {
        return posX;
    }

    public Double getPosY() {
        return posY;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public Integer getZIndex() {
        return zIndex;
    }

    public boolean isHide() {
        return hide;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updatePosition(Double posX, Double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void updateSize(Double width, Double height) {
        this.width = width;
        this.height = height;
    }

    public void updateZIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    public void hide() {
        this.hide = true;
    }
}
