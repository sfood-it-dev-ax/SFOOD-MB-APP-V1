package com.sfood.mb.app.memo.service;

import com.sfood.mb.app.board.domain.Board;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.common.BusinessException;
import com.sfood.mb.app.memo.domain.Memo;
import com.sfood.mb.app.memo.domain.MemoRepository;
import com.sfood.mb.app.memo.dto.CreateMemoRequest;
import com.sfood.mb.app.memo.dto.UpdateMemoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class MemoServiceTest {

    @Autowired
    private MemoService memoService;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        boardRepository.save(new Board("board-a", "user@test.com", "Board A", null, 0));
    }

    @Test
    void createMemo_success() {
        CreateMemoRequest request = new CreateMemoRequest(
                "memo-1",
                "board-a",
                "type-basic",
                "hello",
                10,
                20,
                300,
                200,
                1,
                "#111111",
                "Arial",
                14
        );

        var response = memoService.createMemo(request);

        assertEquals("memo-1", response.memoId());
        assertEquals("board-a", response.boardId());
    }

    @Test
    void createMemo_boardNotFound_throwsException() {
        CreateMemoRequest request = new CreateMemoRequest(
                "memo-2",
                "missing-board",
                "type-basic",
                "hello",
                0,
                0,
                300,
                200,
                0,
                null,
                null,
                null
        );

        assertThrows(BusinessException.class, () -> memoService.createMemo(request));
    }

    @Test
    void updateMemo_success() {
        memoRepository.save(new Memo(
                "memo-3",
                "board-a",
                "type-basic",
                "before",
                0,
                0,
                300,
                200,
                0,
                null,
                null,
                null
        ));

        var response = memoService.updateMemo("memo-3", new UpdateMemoRequest(
                "type-task",
                "after",
                100,
                120,
                320,
                240,
                3,
                "#222222",
                "Pretendard",
                16
        ));

        assertEquals("after", response.content());
        assertEquals(100, response.posX());
        assertEquals(3, response.zIndex());
    }

    @Test
    void deleteMemo_marksAsHidden() {
        memoRepository.save(new Memo(
                "memo-4",
                "board-a",
                "type-basic",
                "to-delete",
                0,
                0,
                300,
                200,
                0,
                null,
                null,
                null
        ));

        memoService.deleteMemo("memo-4");

        Memo memo = memoRepository.findById("memo-4").orElseThrow();
        assertTrue(memo.isHide());
    }
}
