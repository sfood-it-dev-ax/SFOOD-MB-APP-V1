package com.sfood.mb.app.infrastructure.repository.impl.jpa;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.infrastructure.repository.BoardRepository;
import com.sfood.mb.app.infrastructure.repository.jpa.SpringDataBoardJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaBoardRepository implements BoardRepository {

    private final SpringDataBoardJpaRepository jpaRepository;

    public JpaBoardRepository(SpringDataBoardJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Board> findById(String boardId) {
        return jpaRepository.findById(boardId);
    }

    @Override
    public List<Board> findByUserId(String userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }

    @Override
    public Board save(Board board) {
        return jpaRepository.save(board);
    }

    @Override
    public boolean existsById(String boardId) {
        return jpaRepository.existsById(boardId);
    }
}
