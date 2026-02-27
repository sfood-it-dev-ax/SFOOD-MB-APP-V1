package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "mb_memo_types")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private int sortOrder;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    public MemoType(String typeId, String typeName, String defaultColor, String shapeCss, int sortOrder, boolean active) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.defaultColor = defaultColor;
        this.shapeCss = shapeCss;
        this.sortOrder = sortOrder;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
