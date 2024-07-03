package com.carRental.pojo.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;

@Data
@ApiModel(description ="分页查询实体")
public class PageQuery implements Serializable {
    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo=1;

    @ApiModelProperty(value = "页容量",required = true)
    private Integer pageSize=5;

    @ApiModelProperty("排序字段")
    private String sortBy;

    @ApiModelProperty("是否升序")
    private Boolean isAsc=true;

    //创建配置对象
    public <T> Page<T> toMpPage(OrderItem... items){
        //分页条件
        Page<T> page=Page.of(pageNo,pageSize);
        //排序条件
        if(StrUtil.isNotBlank(sortBy)){
            if (isAsc) {
                page.addOrder(OrderItem.asc(sortBy));
            } else {
                page.addOrder(OrderItem.desc(sortBy));
            }
        }
        else if (items!=null){
            page.addOrder(items);
        }
        return page;
    }

    public <T> Page<T> toMpPageDefaultSortByCreateTime(){
        return toMpPage(OrderItem.desc("created_at"));
    }
}
