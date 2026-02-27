package com.sfood.mb.app.service.impl;

import com.sfood.mb.app.dto.response.MemoTypeResponse;
import com.sfood.mb.app.repository.MemoTypeRepository;
import com.sfood.mb.app.service.MemoTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemoTypeServiceImpl implements MemoTypeService {

    private final MemoTypeRepository memoTypeRepository;

    public MemoTypeServiceImpl(MemoTypeRepository memoTypeRepository) {
        this.memoTypeRepository = memoTypeRepository;
    }

    @Override
    public List<MemoTypeResponse> getMemoTypes() {
        return memoTypeRepository.findActive().stream()
                .map(type -> new MemoTypeResponse(
                        type.getTypeId(),
                        type.getTypeName(),
                        type.getDefaultColor(),
                        type.getShapeCss(),
                        type.getSortOrder()))
                .toList();
    }
}
