package com.sfood.mb.app.repository.adapter;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.repository.BoardRepository;
import com.sfood.mb.app.repository.jpa.BoardJpaDataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memory")
public class JpaBoardRepositoryAdapter implements BoardRepository {

    private final BoardJpaDataRepository dataRepository;

    public JpaBoardRepositoryAdapter(BoardJpaDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public Optional<Board> findById(String boardId) {
        return dataRepository.findById(boardId);
    }

    @Override
    public List<Board> findActiveByUserId(String userId) {
        return dataRepository.findByUserIdAndHideFalseOrderBySortOrderAsc(userId);
    }

    @Override
    public Board save(Board board) {
        return dataRepository.save(board);
    }
}
