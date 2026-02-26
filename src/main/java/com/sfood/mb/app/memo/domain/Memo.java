package com.sfood.mb.app.memo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "memos")
public class Memo {

    @Id
    @Column(name = "memo_id", nullable = false, length = 160)
    private String memoId;

    @Column(name = "board_id", nullable = false, length = 120)
    private String boardId;

    @Column(name = "memo_type_id", nullable = false, length = 80)
    private String memoTypeId;

    @Column(name = "content", length = 5000)
    private String content;

    @Column(name = "pos_x", nullable = false)
    private int posX;

    @Column(name = "pos_y", nullable = false)
    private int posY;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "z_index", nullable = false)
    private int zIndex;

    @Column(name = "text_color", length = 40)
    private String textColor;

    @Column(name = "font_family", length = 120)
    private String fontFamily;

    @Column(name = "font_size")
    private Integer fontSize;

    @Column(name = "hide", nullable = false)
    private boolean hide;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Memo() {
    }

    public Memo(
            String memoId,
            String boardId,
            String memoTypeId,
            String content,
            int posX,
            int posY,
            int width,
            int height,
            int zIndex,
            String textColor,
            String fontFamily,
            Integer fontSize
    ) {
        this.memoId = memoId;
        this.boardId = boardId;
        this.memoTypeId = memoTypeId;
        this.content = content;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        this.textColor = textColor;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.hide = false;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getZIndex() {
        return zIndex;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public boolean isHide() {
        return hide;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateMemoTypeId(String memoTypeId) {
        this.memoTypeId = memoTypeId;
    }

    public void updatePosition(Integer posX, Integer posY) {
        if (posX != null) {
            this.posX = posX;
        }
        if (posY != null) {
            this.posY = posY;
        }
    }

    public void updateSize(Integer width, Integer height) {
        if (width != null) {
            this.width = width;
        }
        if (height != null) {
            this.height = height;
        }
    }

    public void updateZIndex(Integer zIndex) {
        if (zIndex != null) {
            this.zIndex = zIndex;
        }
    }

    public void updateStyle(String textColor, String fontFamily, Integer fontSize) {
        if (textColor != null) {
            this.textColor = textColor;
        }
        if (fontFamily != null) {
            this.fontFamily = fontFamily;
        }
        if (fontSize != null) {
            this.fontSize = fontSize;
        }
    }

    public void markHidden() {
        this.hide = true;
    }
}
