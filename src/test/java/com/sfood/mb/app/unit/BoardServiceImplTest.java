package com.sfood.mb.app.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.dto.board.BoardCreateRequest;
import com.sfood.mb.app.infrastructure.repository.BoardRepository;
import com.sfood.mb.app.infrastructure.service.impl.BoardServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BoardServiceImplTest {

    @Test
    void deleteBoardShouldApplyLogicalDelete() {
        BoardRepository boardRepository = Mockito.mock(BoardRepository.class);
        BoardServiceImpl boardService = new BoardServiceImpl(boardRepository);
        Board board = new Board("board-1", "user-1", "Board One");

        when(boardRepository.existsById("board-1")).thenReturn(false);
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(boardRepository.findById("board-1")).thenReturn(Optional.of(board));
        when(boardRepository.findByUserId("user-1")).thenReturn(List.of(board));

        boardService.createBoard("user-1", new BoardCreateRequest("board-1", "Board One"));
        boardService.deleteBoard("user-1", "board-1");

        assertThat(board.isHide()).isTrue();
        assertThat(boardService.getBoards("user-1")).isEmpty();
    }
}
