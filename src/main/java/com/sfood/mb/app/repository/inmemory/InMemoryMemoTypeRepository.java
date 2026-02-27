package com.sfood.mb.app.repository.inmemory;

import com.sfood.mb.app.domain.MemoType;
import com.sfood.mb.app.repository.MemoTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("memory")
public class InMemoryMemoTypeRepository implements MemoTypeRepository {

    private final Map<String, MemoType> types = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        types.put("TYPE_BASIC", new MemoType("TYPE_BASIC", "기본 메모", "#FEF08A", "memo-basic", 1, true));
        types.put("TYPE_ROUND", new MemoType("TYPE_ROUND", "둥근 메모", "#BBF7D0", "memo-round", 2, true));
        types.put("TYPE_SHADOW", new MemoType("TYPE_SHADOW", "그림자 메모", "#BFDBFE", "memo-shadow", 3, true));
    }

    @Override
    public List<MemoType> findActive() {
        return types.values().stream()
                .filter(MemoType::isActive)
                .sorted(Comparator.comparing(MemoType::getSortOrder))
                .toList();
    }

    @Override
    public Optional<MemoType> findById(String typeId) {
        return Optional.ofNullable(types.get(typeId));
    }
}
