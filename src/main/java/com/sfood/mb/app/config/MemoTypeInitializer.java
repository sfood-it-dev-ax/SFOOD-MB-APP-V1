package com.sfood.mb.app.config;

import com.sfood.mb.app.domain.MemoType;
import com.sfood.mb.app.repository.MemoTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MemoTypeInitializer implements CommandLineRunner {
    private final MemoTypeRepository memoTypeRepository;

    public MemoTypeInitializer(MemoTypeRepository memoTypeRepository) {
        this.memoTypeRepository = memoTypeRepository;
    }

    @Override
    public void run(String... args) {
        if (memoTypeRepository.count() > 0) {
            return;
        }
        memoTypeRepository.save(MemoType.create("TYPE_BASIC", "기본 메모", "#FEF08A", "memo-basic", 1));
        memoTypeRepository.save(MemoType.create("TYPE_ROUND", "둥근 메모", "#BBF7D0", "memo-round", 2));
        memoTypeRepository.save(MemoType.create("TYPE_SHADOW", "그림자 메모", "#BFDBFE", "memo-shadow", 3));
    }
}
