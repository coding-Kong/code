package com.kdznode.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kdz
 * @create 2025-03-05-21:45
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityQuery extends BaseQuery{

   private Integer  ownerId ;
   private String  name ;
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date  startTime;
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date  endTime;
   private BigDecimal cost;
   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date createTime;
   private String description;
}
