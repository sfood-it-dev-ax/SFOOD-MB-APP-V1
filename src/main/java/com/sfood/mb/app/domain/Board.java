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
@Table(name = "mb_boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @Column(name = "board_id", nullable = false, length = 255)
    private String boardId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "parent_board_id", length = 255)
    private String parentBoardId;

    @Column(name = "board_name", nullable = false, length = 20)
    private String boardName;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_hide", nullable = false)
    private boolean hide;

    public Board(String boardId, String userId, String parentBoardId, String boardName, int sortOrder, LocalDateTime createdAt, LocalDateTime updatedAt, boolean hide) {
        this.boardId = boardId;
        this.userId = userId;
        this.parentBoardId = parentBoardId;
        this.boardName = boardName;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.hide = hide;
    }

    public void rename(String boardName) {
        this.boardName = boardName;
        this.updatedAt = LocalDateTime.now();
    }

    public void move(String parentBoardId, int sortOrder) {
        this.parentBoardId = parentBoardId;
        this.sortOrder = sortOrder;
        this.updatedAt = LocalDateTime.now();
    }

    public void hide() {
        this.hide = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isHide() {
        return hide;
    }
}
