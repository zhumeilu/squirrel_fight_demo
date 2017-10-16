package com.zml.model;

import com.zml.command.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/10/16
 * Time: 18:14
 */
@Getter
@Setter
public class Player extends BaseModel{
    private Float position_x;       //位置
    private Float position_z;
}
