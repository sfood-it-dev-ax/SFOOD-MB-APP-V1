package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "memo_types")
public class MemoType {

    @Id
    @Column(nullable = false, length = 50)
    private String memoTypeId;

    @Column(nullable = false, length = 100)
    private String label;

    @Column(nullable = false, length = 20)
    private String defaultColor;

    protected MemoType() {
    }

    public MemoType(String memoTypeId, String label, String defaultColor) {
        this.memoTypeId = memoTypeId;
        this.label = label;
        this.defaultColor = defaultColor;
    }

    public String memoTypeId() {
        return memoTypeId;
    }

    public String label() {
        return label;
    }

    public String defaultColor() {
        return defaultColor;
    }
}
