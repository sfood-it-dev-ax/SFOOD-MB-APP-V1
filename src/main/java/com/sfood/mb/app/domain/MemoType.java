package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "memo_types")
public class MemoType {
    @Id
    @Column(name = "type_id", nullable = false, length = 50)
    private String typeId;

    @Column(name = "type_name", nullable = false, length = 50)
    private String typeName;

    @Column(name = "default_color", nullable = false, length = 20)
    private String defaultColor;

    @Column(name = "shape_css", nullable = false, length = 255)
    private String shapeCss;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    public static MemoType create(String typeId, String typeName, String defaultColor, String shapeCss, Integer sortOrder) {
        MemoType memoType = new MemoType();
        memoType.typeId = typeId;
        memoType.typeName = typeName;
        memoType.defaultColor = defaultColor;
        memoType.shapeCss = shapeCss;
        memoType.sortOrder = sortOrder;
        memoType.active = true;
        return memoType;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDefaultColor() {
        return defaultColor;
    }

    public String getShapeCss() {
        return shapeCss;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
}
