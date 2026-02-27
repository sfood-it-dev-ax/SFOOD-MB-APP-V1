package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "mb_memos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

    @Id
    @Column(name = "memo_id", nullable = false, length = 255)
    private String memoId;

    @Column(name = "board_id", nullable = false, length = 255)
    private String boardId;

    @Column(name = "type_id", nullable = false, length = 50)
    private String typeId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "pos_x", nullable = false)
    private float posX;

    @Column(name = "pos_y", nullable = false)
    private float posY;

    @Column(nullable = false)
    private float width;

    @Column(nullable = false)
    private float height;

    @Column(name = "z_index", nullable = false)
    private int zindex;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_hide", nullable = false)
    private boolean hide;

    public Memo(String memoId, String boardId, String typeId, String content, float posX, float posY, float width, float height, int zIndex, LocalDateTime createdAt, LocalDateTime updatedAt, boolean hide) {
        this.memoId = memoId;
        this.boardId = boardId;
        this.typeId = typeId;
        this.content = content;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.zindex = zIndex;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.hide = hide;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePosition(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateZIndex(int zIndex) {
        this.zindex = zIndex;
        this.updatedAt = LocalDateTime.now();
    }

    public void hide() {
        this.hide = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isHide() {
        return hide;
    }

    public int getZIndex() {
        return zindex;
    }
}
