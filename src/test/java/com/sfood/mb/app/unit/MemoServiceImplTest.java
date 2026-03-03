package com.sfood.mb.app.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.domain.Memo;
import com.sfood.mb.app.domain.MemoType;
import com.sfood.mb.app.dto.memo.MemoCreateRequest;
import com.sfood.mb.app.infrastructure.repository.BoardRepository;
import com.sfood.mb.app.infrastructure.repository.MemoRepository;
import com.sfood.mb.app.infrastructure.repository.MemoTypeRepository;
import com.sfood.mb.app.infrastructure.service.impl.MemoServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemoServiceImplTest {

    @Test
    void deleteMemoShouldApplyLogicalDelete() {
        BoardRepository boardRepository = Mockito.mock(BoardRepository.class);
        MemoRepository memoRepository = Mockito.mock(MemoRepository.class);
        MemoTypeRepository memoTypeRepository = Mockito.mock(MemoTypeRepository.class);
        MemoServiceImpl memoService = new MemoServiceImpl(memoRepository, boardRepository, memoTypeRepository);

        Board board = new Board("board-1", "user-1", "Board One");
        Memo memo = new Memo("memo-1", "board-1", "basic-yellow", "hello", 1, 2, 300, 200, 1);

        when(boardRepository.findById("board-1")).thenReturn(Optional.of(board));
        when(memoRepository.existsById("memo-1")).thenReturn(false);
        when(memoTypeRepository.findById("basic-yellow")).thenReturn(Optional.of(new MemoType("basic-yellow", "Basic Yellow", "#FFE66D")));
        when(memoRepository.save(any(Memo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(memoRepository.findById("memo-1")).thenReturn(Optional.of(memo));
        when(memoRepository.findByBoardId("board-1")).thenReturn(List.of(memo));

        memoService.createMemo("user-1", new MemoCreateRequest(
                "memo-1", "board-1", "basic-yellow", "hello", 1, 2, 300, 200, 1
        ));
        memoService.deleteMemo("user-1", "memo-1");

        assertThat(memo.isHide()).isTrue();
        assertThat(memoService.getMemos("user-1", "board-1")).isEmpty();
    }
}
