package com.lemeng.game.service;

import com.lemeng.common.Const;
import com.lemeng.common.SystemManager;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.AssistRecord;
import com.lemeng.game.domain.Player;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.service.AbstractUdpService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**击中事件服务
 * Created by zhumeilu on 17/9/26.
 */
public class HitService extends AbstractUdpService {

    @Autowired
    private JedisClusterUtil jedisClusterUtil;
    public void run() {

        try{
            byte[] body = message.getBody();

            GameCommand.HitCommand hitCommand = GameCommand.HitCommand.parseFrom(body);

            int id = hitCommand.getId();
            int enemyId = hitCommand.getEnemyId();
            int damage = hitCommand.getDamage();
            //获取攻击者
            Player player = (Player) jedisClusterUtil.getObject(Const.PlayerPrefix + id);
            //获取被攻击者
            Player enemy = (Player) jedisClusterUtil.getObject(Const.PlayerPrefix + enemyId);

            //计算伤害
            Integer attack = player.getAttack();
            if(attack.equals(damage)){
                if(enemy.getHp()>damage){
                    //被攻击未死亡,添加助攻记录，修改被攻击者血量
                    enemy.setHp(enemy.getHp()-damage);
                    //添加攻击记录
                    AssistRecord assistRecord = new AssistRecord();
                    assistRecord.setId(SystemManager.getInstance().getIdGenertor().generateAssistId());
                    assistRecord.setEnemyId(enemyId);
                    assistRecord.setAssistId(player.getId());
                    assistRecord.setCreateDate(new Date());
                    HashSet<AssistRecord> assistRecordHashSet = (HashSet<AssistRecord>) jedisClusterUtil.getObject(Const.AssistPrefix + enemy.getId());
                    assistRecordHashSet.add(assistRecord);
                    jedisClusterUtil.setObject(Const.AssistPrefix+enemy.getId(),assistRecordHashSet);
                }else{
                    //被攻击者死亡，攻击者杀人数+1，助攻者助攻数+1，被攻击者死亡数+1
                    //修改被攻击者状态，血量
                    enemy.setHp(0);
                    enemy.setStatue(2);
                    //结算
                    player.setKillNum(player.getKillNum()+1);//杀人数+1
                    //奖励
                    //todo

                    //获取助攻
                    HashSet<AssistRecord> assistRecordList = (HashSet<AssistRecord>) jedisClusterUtil.getObject(Const.AssistPrefix + enemy.getId());

                    //遍历，助攻数+1
                    for (AssistRecord assistRecord : assistRecordList) {
                        //判断时间是否过期
                        if(System.currentTimeMillis()-assistRecord.getCreateDate().getTime()<=30*1000){
                            Integer assistId = assistRecord.getAssistId();
                            Player assist = (Player)jedisClusterUtil.getObject(Const.PlayerPrefix + assistId);
                            assist.setAssistNum(assist.getAssistNum()+1);
                            //助攻奖励,广播
                            //todo

                            jedisClusterUtil.setObject(Const.PlayerPrefix+assist.getId(),assist);
                        }
                    }
                    //删除助攻记录
                    jedisClusterUtil.delete(Const.AssistPrefix+enemy.getId());
                }
                //更新enemy状态
                jedisClusterUtil.setObject(Const.PlayerPrefix+enemy.getId(),enemy);
                //更新player状态
                jedisClusterUtil.setObject(Const.PlayerPrefix+player.getId(),player);

                //广播击杀事件
                //广播玩家状态

            }


        }catch (Exception e){
            logStackTrace(e);
        }

    }
}
