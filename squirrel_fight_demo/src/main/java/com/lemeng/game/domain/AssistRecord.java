package com.lemeng.game.domain;

import com.lemeng.common.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**助攻记录
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 11:38
 */
@Getter
@Setter
public class AssistRecord extends BaseDomain{

    private Integer assistId;       //助攻人id
    private Integer enemyId;        //敌人id，被攻击人id
    private Date createDate;        //创建时间



    //添加到hashset中去重
    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        AssistRecord other = (AssistRecord) obj;
        return this.getId().equals(other.getId());
    }
}
