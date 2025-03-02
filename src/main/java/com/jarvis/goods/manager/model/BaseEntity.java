package com.jarvis.goods.manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity
 */
@Data
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -7502181746434161019L;

    @Id
    @Schema(description = "상품아이디")
    private Long id;

    @Column(value = "IS_DEL")
    @Schema(description = "삭제여부")
    private Boolean isDel = false;

    @Schema(description = "생성날짜")
    private LocalDateTime createdAt;

    @Schema(description = "생성자")
    private String createdBy;

    @Schema(description = "수정날짜")
    private LocalDateTime modifiedAt;

    @Schema(description = "수정자")
    private String modifiedBy;

    public boolean isNew() {
        return getId() == null || getId() < 1;
    }

}
