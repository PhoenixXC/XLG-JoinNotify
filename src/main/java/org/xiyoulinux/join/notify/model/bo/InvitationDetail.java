package org.xiyoulinux.join.notify.model.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.xiyoulinux.join.notify.model.po.Invitation;
import org.xiyoulinux.join.notify.model.po.Join;
import org.xiyoulinux.join.notify.model.po.Sender;

/**
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/11/16 上午10:52
 */
@Data
@NoArgsConstructor
public class InvitationDetail {

    private Invitation invitation;

    private Join join;

    private Sender sender;

    public InvitationDetail(Invitation invitation) {
        this.invitation = invitation;
    }

}
