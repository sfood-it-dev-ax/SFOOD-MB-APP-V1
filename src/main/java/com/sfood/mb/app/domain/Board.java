package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "boards")
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
    private Integer sortOrder;

    @Column(name = "is_hide", nullable = false)
    private boolean hide;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public static Board create(String boardId, String userId, String parentBoardId, String boardName, Integer sortOrder) {
        Board board = new Board();
        board.boardId = boardId;
        board.userId = userId;
        board.parentBoardId = parentBoardId;
        board.boardName = boardName;
        board.sortOrder = sortOrder;
        board.hide = false;
        return board;
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

    public String getBoardId() {
        return boardId;
    }

    public String getUserId() {
        return userId;
    }

    public String getParentBoardId() {
        return parentBoardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public boolean isHide() {
        return hide;
    }

    public void rename(String boardName) {
        this.boardName = boardName;
    }

    public void move(String parentBoardId, Integer sortOrder) {
        this.parentBoardId = parentBoardId;
        this.sortOrder = sortOrder;
    }

    public void hide() {
        this.hide = true;
    }
}
