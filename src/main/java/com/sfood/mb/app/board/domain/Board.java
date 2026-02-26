package com.sfood.mb.app.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @Column(name = "board_id", nullable = false, length = 120)
    private String boardId;

    @Column(name = "user_email", nullable = false, length = 255)
    private String userEmail;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "parent_board_id", length = 120)
    private String parentBoardId;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "hide", nullable = false)
    private boolean hide;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Board() {
    }

    public Board(String boardId, String userEmail, String name, String parentBoardId, int sortOrder) {
        this.boardId = boardId;
        this.userEmail = userEmail;
        this.name = name;
        this.parentBoardId = parentBoardId;
        this.sortOrder = sortOrder;
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

    public String getBoardId() {
        return boardId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getName() {
        return name;
    }

    public String getParentBoardId() {
        return parentBoardId;
    }

    public int getSortOrder() {
        return sortOrder;
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

    public void rename(String name) {
        this.name = name;
    }

    public void markHidden() {
        this.hide = true;
    }
}
