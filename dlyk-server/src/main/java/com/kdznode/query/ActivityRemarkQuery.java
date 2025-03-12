package com.kdznode.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kdz
 * @create 2025-03-10-22:34
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRemarkQuery extends BaseQuery{

    public Integer activityId;
    public String noteContent;
}
