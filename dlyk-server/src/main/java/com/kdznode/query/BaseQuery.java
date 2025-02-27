package com.kdznode.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kdz
 * @create 2025-02-22-15:57
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseQuery {
    private String token;//jwt

    private String filterSQL;//数据权限对sql过滤条件，tu.id=2
}
