package com.sfood.mb.app.config;

import com.sfood.mb.app.domain.MemoType;
import com.sfood.mb.app.infrastructure.repository.jpa.SpringDataMemoTypeJpaRepository;
import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MemoTypeDataInitializer {

    private final SpringDataMemoTypeJpaRepository memoTypeJpaRepository;

    public MemoTypeDataInitializer(SpringDataMemoTypeJpaRepository memoTypeJpaRepository) {
        this.memoTypeJpaRepository = memoTypeJpaRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedMemoTypes() {
        if (memoTypeJpaRepository.count() > 0) {
            return;
        }

        memoTypeJpaRepository.saveAll(List.of(
                new MemoType("basic-yellow", "Basic Yellow", "#FFE66D"),
                new MemoType("basic-green", "Basic Green", "#C7F9CC"),
                new MemoType("basic-blue", "Basic Blue", "#A9DEF9")
        ));
    }
}
