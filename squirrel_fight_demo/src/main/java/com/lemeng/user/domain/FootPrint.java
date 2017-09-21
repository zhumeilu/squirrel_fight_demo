package com.lemeng.user.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:脚印
 * User: zhumeilu
 * Date: 2017/9/21
 * Time: 11:31
 */
@Getter
@Setter
public class FootPrint extends BaseDomain{

    private String name;
    private String type;
    private Integer price;
}
