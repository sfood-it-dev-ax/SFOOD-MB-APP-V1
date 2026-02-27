package com.sfood.mb.app.repository.inmemory;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.repository.BoardRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("memory")
public class InMemoryBoardRepository implements BoardRepository {

    private final Map<String, Board> boards = new ConcurrentHashMap<>();

    @Override
    public Optional<Board> findById(String boardId) {
        return Optional.ofNullable(boards.get(boardId));
    }

    @Override
    public List<Board> findActiveByUserId(String userId) {
        return boards.values().stream()
                .filter(board -> board.getUserId().equals(userId) && !board.isHide())
                .sorted(Comparator.comparing(Board::getSortOrder))
                .toList();
    }

    @Override
    public Board save(Board board) {
        boards.put(board.getBoardId(), board);
        return board;
    }
}
