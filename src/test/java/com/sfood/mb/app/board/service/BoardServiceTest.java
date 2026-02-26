package com.sfood.mb.app.board.service;

import com.sfood.mb.app.board.domain.Board;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.board.dto.CreateBoardRequest;
import com.sfood.mb.app.board.dto.UpdateBoardNameRequest;
import com.sfood.mb.app.common.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void createBoard_success() {
        CreateBoardRequest request = new CreateBoardRequest(
                "user@test.com_board_1",
                "user@test.com",
                "My Board",
                null,
                null
        );

        var response = boardService.createBoard(request);

        assertEquals("user@test.com_board_1", response.boardId());
        assertEquals("My Board", response.name());
    }

    @Test
    void getBoards_returnsOnlyVisibleBoards() {
        boardRepository.save(new Board("b1", "user@test.com", "Board 1", null, 0));
        Board hidden = boardRepository.save(new Board("b2", "user@test.com", "Board 2", null, 0));
        hidden.markHidden();

        List<?> result = boardService.getBoards("user@test.com");

        assertEquals(1, result.size());
    }

    @Test
    void updateBoardName_notFound_throwsException() {
        UpdateBoardNameRequest request = new UpdateBoardNameRequest("New Name");

        assertThrows(BusinessException.class, () -> boardService.updateBoardName("missing", request));
    }

    @Test
    void deleteBoard_marksAsHidden() {
        boardRepository.save(new Board("b3", "user@test.com", "Board 3", null, 0));

        boardService.deleteBoard("b3");

        Board board = boardRepository.findById("b3").orElseThrow();
        assertTrue(board.isHide());
    }
}
