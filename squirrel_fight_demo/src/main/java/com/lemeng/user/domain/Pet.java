package com.lemeng.user.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/19
 * Time: 11:27
 */
@Setter
@Getter
public class Pet extends BaseDomain{

    private String name;
    private String type;
    private Integer price;
}
