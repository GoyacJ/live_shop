package vip.xiaonuo.live.modular.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupMessageResponse {

    @JsonProperty("actionCode")
    private int actionCode;

    @JsonProperty("errCode")
    private int errCode;

    @JsonProperty("errMsg")
    private String errMsg;

    @JsonProperty("errDlt")
    private String errDlt;

    @JsonProperty("nextCode")
    private int nextCode;

    public static GroupMessageResponse createSuccess(int actionCode,int errCode,String errMsg,String errDlt,int nextCode){
        GroupMessageResponse groupMessageResponse = new GroupMessageResponse();
        groupMessageResponse.setActionCode(actionCode);
        groupMessageResponse.setErrCode(errCode);
        groupMessageResponse.setErrMsg(errMsg);
        groupMessageResponse.setErrDlt(errDlt);
        groupMessageResponse.setNextCode(nextCode);
        return groupMessageResponse;
    }

    public static GroupMessageResponse createError(int actionCode,int errCode,String errMsg,String errDlt,int nextCode){
        GroupMessageResponse groupMessageResponse = new GroupMessageResponse();
        groupMessageResponse.setActionCode(actionCode);
        groupMessageResponse.setErrCode(errCode);
        groupMessageResponse.setErrMsg(errMsg);
        groupMessageResponse.setErrDlt(errDlt);
        groupMessageResponse.setNextCode(nextCode);
        return groupMessageResponse;
    }

}
