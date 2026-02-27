package com.sfood.mb.app.config;

import com.sfood.mb.app.domain.MemoType;
import com.sfood.mb.app.repository.jpa.MemoTypeJpaDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoTypeDataInitializer {

    @Bean
    CommandLineRunner memoTypeInitializer(MemoTypeJpaDataRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.save(new MemoType("TYPE_BASIC", "기본 메모", "#FEF08A", "memo-basic", 1, true));
            repository.save(new MemoType("TYPE_ROUND", "둥근 메모", "#BBF7D0", "memo-round", 2, true));
            repository.save(new MemoType("TYPE_SHADOW", "그림자 메모", "#BFDBFE", "memo-shadow", 3, true));
        };
    }
}
