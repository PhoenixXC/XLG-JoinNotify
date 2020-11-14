package org.xiyoulinux.join.notify.manager.strategy;

import lombok.NonNull;
import org.xiyoulinux.join.notify.model.dao.Invitation;
import org.xiyoulinux.join.notify.model.dao.Join;
import org.xiyoulinux.join.notify.model.strategy.StrategyConfig;
import org.xiyoulinux.join.notify.model.strategy.StrategyType;
import org.xiyoulinux.join.notify.model.dao.Sender;

import java.util.List;

/**
 * 分配策略：
 * - 随机
 * - 报名顺序
 * - 专业班级
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午5:05
 */
public interface DispatchStrategy {

    /**
     * 分配策略
     *
     * @param senderList   消息通知者列表
     * @param joinList     报名信息
     * @param strategyConfig 策略配置
     * @return 分配后的面试邀请
     */
    List<Invitation> dispatch(List<Sender> senderList,
                              List<Join> joinList,
                              @NonNull StrategyConfig strategyConfig
    );

    /**
     * 策略类型
     *
     * @return 策略类型
     */
    StrategyType getType();

}
