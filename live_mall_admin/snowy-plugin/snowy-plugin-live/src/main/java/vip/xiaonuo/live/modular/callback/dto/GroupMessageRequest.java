package vip.xiaonuo.live.modular.callback.dto;

import cn.hutool.json.JSON;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupMessageRequest {

        private String callbackCommand;

        private String userID;

        private String groupID;

        private JSON contentType;
}
