package com.sfood.mb.app.service.impl;

import com.sfood.mb.app.dto.response.MemoTypeResponse;
import com.sfood.mb.app.repository.MemoTypeRepository;
import com.sfood.mb.app.service.MemoTypeService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemoTypeServiceImpl implements MemoTypeService {
    private final MemoTypeRepository memoTypeRepository;

    public MemoTypeServiceImpl(MemoTypeRepository memoTypeRepository) {
        this.memoTypeRepository = memoTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemoTypeResponse> listActive() {
        return memoTypeRepository.findByActiveTrueOrderBySortOrderAsc().stream()
            .map(mt -> new MemoTypeResponse(mt.getTypeId(), mt.getTypeName(), mt.getDefaultColor(), mt.getShapeCss(), mt.getSortOrder()))
            .toList();
    }
}
