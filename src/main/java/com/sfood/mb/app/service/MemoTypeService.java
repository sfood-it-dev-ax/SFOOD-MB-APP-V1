package com.sfood.mb.app.service;

import com.sfood.mb.app.dto.response.MemoTypeResponse;
import java.util.List;

public interface MemoTypeService {
    List<MemoTypeResponse> listActive();
}
