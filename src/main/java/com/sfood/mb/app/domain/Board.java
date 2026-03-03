package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @Column(name = "board_id", nullable = false, length = 120)
    private String boardId;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "is_hide", nullable = false)
    private boolean isHide;

    @Column(name = "parent_board_id", length = 120)
    private String parentBoardId;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Board() {
    }

    public Board(String boardId, String userId, String name) {
        this.boardId = boardId;
        this.userId = userId;
        this.name = name;
        this.isHide = false;
        this.parentBoardId = null;
        this.sortOrder = 0;
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

    public void rename(String name) {
        this.name = name;
    }

    public void hide() {
        this.isHide = true;
    }

    public void move(String parentBoardId, int sortOrder) {
        this.parentBoardId = parentBoardId;
        this.sortOrder = sortOrder;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public boolean isHide() {
        return isHide;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getParentBoardId() {
        return parentBoardId;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
