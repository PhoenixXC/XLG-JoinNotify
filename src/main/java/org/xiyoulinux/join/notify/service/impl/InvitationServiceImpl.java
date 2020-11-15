package org.xiyoulinux.join.notify.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.xiyoulinux.join.notify.mapper.InvitationMapper;
import org.xiyoulinux.join.notify.model.bo.InviteStatus;
import org.xiyoulinux.join.notify.model.dto.result.PageResult;
import org.xiyoulinux.join.notify.model.po.Invitation;
import org.xiyoulinux.join.notify.model.po.Join;
import org.xiyoulinux.join.notify.service.InvitationService;
import org.xiyoulinux.join.notify.utils.ToolUtils;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/10 下午8:13
 */
@Log4j2
@Service
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {

    private final InvitationMapper invitationMapper;

    public InvitationServiceImpl(InvitationMapper invitationMapper) {
        this.invitationMapper = invitationMapper;
    }

    @Override
    public boolean batchInsert(List<Invitation> invitationList) {
        if (CollectionUtils.isEmpty(invitationList)) {
            return false;
        }
        return saveBatch(invitationList, ToolUtils.DEFAULT_PAGE_SIZE);
    }

    @Override
    public PageResult<Invitation> getInvitation(@NonNull Invitation condition, int pageNum, int pageSize) {
        Page<Invitation> page = page(new Page<>(pageNum, pageSize), Wrappers.lambdaQuery(condition));
        return PageResult.pageBuilder(page.getRecords())
                .success()
                .setTotal(page.getTotal())
                .setCurPage(pageNum)
                .setHasNext(page.hasNext());
    }

    @Override
    public PageResult<Join> getUnDispatchJoin(@NonNull Integer processId, int pageNum, int pageSize) {
        final long pageStart = (pageNum - 1) * pageSize;
        final long total = invitationMapper.countUnDispatchJoin(processId);
        if (total <= 0) {
            return PageResult.<Join>pageBuilder().success();
        }
        return PageResult.pageBuilder(invitationMapper.getUnDispatchJoin(processId, pageStart, pageSize))
                .success()
                .setCurPage(pageNum)
                .setTotal(total)
                .setAllPage((int) Math.ceil(1.0 * total / pageSize));
    }

    @Override
    public boolean updateById(@NonNull Long id, @NonNull Invitation newInvitation) {
        // NOTE: mybatis-plus 的 IService 写着有点混...
        return super.updateById(newInvitation.setId(id));
    }

    @Override
    public boolean updateStatus(@NonNull Integer processId,
                                @NonNull InviteStatus status,
                                @NonNull InviteStatus newStatus) {
        Invitation invitation = new Invitation();
        invitation.setStatus(newStatus);
        invitation.setProcessId(processId);
        return update(
                invitation,
                Wrappers.<Invitation>lambdaUpdate()
                        .eq(Invitation::getStatus, status)
                        .eq(Invitation::getProcessId, processId)
        );
    }

    @Override
    public boolean hasUnsettled(Long senderId, @NonNull Integer processId) {
        return CollectionUtils.isNotEmpty(
                list(
                        Wrappers.<Invitation>lambdaQuery()
                                .eq(Invitation::getSenderId, senderId)
                                .eq(Invitation::getProcessId, processId)
                                .notIn(Invitation::getStatus, InviteStatus.unDealStatus())
                                // 只是判断有没有已经在处理的邀请，获取一个就可以了
                                .last(" limit 1 ")
                )
        );
    }

    @Override
    public int countByStatus(@NonNull Integer processId, @NonNull InviteStatus status) {
        return count(
                Wrappers.<Invitation>lambdaQuery()
                        .eq(Invitation::getStatus, status)
                        .eq(Invitation::getProcessId, processId)
        );
    }

    @Override
    public boolean clean(@NonNull Invitation condition) {
        return remove(
                Wrappers.lambdaQuery(condition)
        );
    }

}
