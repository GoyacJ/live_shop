package vip.xiaonuo.live.modular.openim;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.net.http.HttpUtils;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.consts.SubConstants;
import vip.xiaonuo.common.enums.ErrorCodeEnum;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.util.ResponseUtil;
import vip.xiaonuo.live.api.openim.OpenImApi;
import vip.xiaonuo.live.api.openim.dto.*;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class OpenImImpl implements OpenImApi {

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject("${openim.secret}")
    private String secret;

    @Inject("${openim.userId}")
    private String userId;

    @Inject("${openim.ip}")
    private String ip;

    @Inject("${openim.systemUser}")
    private String systemUser;

    @Override
    public Map<String, String> getUserToken(int platformId, String userId) {
        // 定义请求URL
        String url = "http://" + ip + SubConstants.GET_USER_TOKEN_URL;
        // 创建请求体 组装参数
        String requestBody = "{\"platformID\": " + Convert.toStr(platformId) + ", \"userID\": \"" + userId + "\"}";
        try {
            String resp = HttpUtils.http(url).bodyOfJson(requestBody).post(true);
            System.out.println(resp);
            //解析响应
            Integer errCode = new JSONObject(resp).getInt("errCode");
            if (!resp.isEmpty() && errCode == 0) {
                HashMap<String, String> tokenMap = new HashMap<>();
                String data = new JSONObject(resp).getStr("data");
                String token = new JSONObject(data).getStr("token");
                String expireTimeSeconds = new JSONObject(data).getStr("expireTimeSeconds");
                tokenMap.put("token", token);
                tokenMap.put("expire", expireTimeSeconds);
                return tokenMap;
            } else {
                return ResponseUtil.errorResponse(errCode, ErrorCodeEnum.fromCode(errCode).getMessage(), new JSONObject(resp).get("data"));
            }
        } catch (Exception e) {
            return ResponseUtil.errorResponse(null, "发生异常 " + e.getMessage(), null);
        }
    }

    @Override
    public Map<String, String> getAdminToken() {
        // 定义请求URL
        String url = "http://" + ip + SubConstants.GET_ADMIN_TOKEN_URL;
        // 创建请求体
        String requestBody = "{\"secret\": \"" + secret + "\", \"userId\": \"" + userId + "\"}";
        try {
            String resp = HttpUtils.http(url).bodyOfJson(requestBody).post(true);
            //解析响应
            Integer errCode = new JSONObject(resp).getInt("errCode");
            if (!resp.isEmpty() && errCode == 0) {
                HashMap<String, String> tokenMap = new HashMap<>();
                String data = new JSONObject(resp).getStr("data");
                String token = new JSONObject(data).getStr("token");
                String expireTimeSeconds = new JSONObject(data).getStr("expireTimeSeconds");
                tokenMap.put("token", token);
                tokenMap.put("expireTimeSeconds", expireTimeSeconds);
                return tokenMap;
            } else {
                return ResponseUtil.errorResponse(errCode, ErrorCodeEnum.fromCode(errCode).getMessage(), null);
            }
        } catch (Exception e) {
            return ResponseUtil.errorResponse(HttpStatus.HTTP_BAD_GATEWAY, e.getMessage(), null);
        }
    }

    @Override
    public Map<String, String> registerUser(String userId, String nickname, String faceURL) {
        // 定义请求URL
        String url = "http://" + ip + SubConstants.USER_REGISTER_URL;
        // 创建请求体 组装参数
        Map<String, String> map = new HashMap<>();
        ArrayList<String> prarms = new ArrayList<>();
        map.put("userId", userId);
        map.put("nickname", nickname);
        map.put("faceURL", faceURL);
        prarms.add(new JSONObject(map).toString());
        String requestBody = "{\"users\": " + prarms + "}";
        try {
            String resp = HttpUtils.http(url).bodyOfJson(requestBody).post(true);
            int errCode = new JSONObject(resp).getInt("errCode");
            if (errCode == 0) {
                return ResponseUtil.successResponse(null);
            } else {
                return ResponseUtil.errorResponse(errCode, ErrorCodeEnum.fromCode(errCode).getMessage(), null);
            }
        } catch (Exception e) {
            return ResponseUtil.errorResponse(HttpStatus.HTTP_BAD_GATEWAY, e.getMessage(), null);
        }
    }

    @Override
    public Map<String, String> createGroup(String groupId, String groupName) {
        // 定义请求URL
        String url = "http://" + ip + SubConstants.CREATE_GROUP_URL;
        // 创建请求体 组装参数
        String adminUserIDs = "[\"5094228864\", \"3431782589\"]";
        // 组装参数
        String requestBody = "{\"ownerUserID\": \"" + systemUser + "\"," +
                "\"adminUserIDs\":" + adminUserIDs + "," +
                "\"groupInfo\": {" +
                "\"groupName\": \"" + groupName + "\"," +
                "\"groupID\": \"" + groupId + "\"," +
                "\"groupType\": 2," +
                "\"needVerification\": 2" +
                "}}";
        try {
            // 获取管理员token
            Map<String, String> adminTokenMap = getAdminToken();
            if (adminTokenMap.isEmpty() || adminTokenMap.get("token") == null) {
                log.error("群组【{}】创建失败，未获取到有效token", groupId);
            }

            // 发送请求
            String resp = HttpUtils.http(url).bodyOfJson(requestBody)
                    .header("token", adminTokenMap.get("token"))
                    .header("operationID", new Date().getTime() + "")
                    .post(true);
            Integer errCode = new JSONObject(resp).getInt("errCode");
            if (errCode == 0) {
                String data = new JSONObject(resp).getStr("data");
                String groupInfo = new JSONObject(data).getStr("groupInfo");
                String groupId1 = new JSONObject(groupInfo).getStr("groupID");
                return ResponseUtil.successResponse(groupId1);
            } else {
                return ResponseUtil.errorResponse(errCode, ErrorCodeEnum.fromCode(errCode).getMessage(), null);
            }
        } catch (Exception e) {
            return ResponseUtil.errorResponse(HttpStatus.HTTP_BAD_GATEWAY, e.getMessage(), null);
        }
    }

    @Override
    public Map<String, String> checkSensitiveWord(String txt) {
        String str = "";
        boolean contains = SensitiveWordHelper.contains(txt);
        if (!contains) {
            return ResponseUtil.successResponse(txt);
        } else {
            str = SensitiveWordHelper.replace(txt);
        }
        return ResponseUtil.successResponse(str);
    }

    @Override
    public Map<String, String> addUserToGroup(String groupId, String invitedUserID) {
        // 定义请求URL
        String url = "http://" + ip + SubConstants.ADD_USER_TO_GROUP;
        // 创建请求体 组装参数
        String requestBody = "{\"groupID\": \"" + groupId + "\",\"joinSource\":" + 3 + ", \"inviterUserID\": \"" + invitedUserID + "\"}";
        try {
            String resp = HttpUtils.http(url).bodyOfJson(requestBody).post(true);
            Integer errCode = new JSONObject(resp).getInt("errCode");
            if (errCode == 0) {
                return ResponseUtil.successResponse(null);
            } else {
                return ResponseUtil.errorResponse(errCode, ErrorCodeEnum.fromCode(errCode).getMessage(), null);
            }
        } catch (Exception e) {
            return ResponseUtil.errorResponse(HttpStatus.HTTP_BAD_GATEWAY, e.getMessage(), null);
        }
    }

    @Override
    public OpenImErrorResponseDTO updateGroupInfo(OpenImGroupUpdateRequestDTO dto) {
        final String url = "http://" + ip + SubConstants.SET_GROUPS_INFO;

        JSONObject request = new JSONObject();
        request.putOpt("groupID", dto.groupId());
        request.putOpt("groupName", dto.groupName());
        request.putOpt("notification", dto.notification());
        request.putOpt("introduction", dto.introduction());
        request.putOpt("faceURL", dto.faceUrl());
        request.putOpt("ex", dto.ex());
        request.putOpt("needVerification", dto.needVerification());
        request.putOpt("lookMemberInfo", dto.lookMemberInfo());
        request.putOpt("applyMemberFriend", dto.applyMemberFriend());


        // 发送请求
        try {
            String resp = HttpUtils.http(url).bodyOfJson(request.toJSONString(2))
                    .header("token", getAdminToken().get("token"))
                    .header("operationID", new Date().getTime() + "")
                    .post(true);

            if (StringUtils.isEmpty(resp)) {
                throw new CommonException("Open Im 响应失败");
            }

            log.info("OpenIm发送业务通知,响应:{}", resp);
            return OpenImErrorResponseDTO.of(resp);
        } catch (IOException e) {
            throw new CommonException(url + ": 请求失败", e);
        }
    }

    @Override
    public Map<String, String> quitGroup(String groupId, String userId) {
        // 定义请求URL
        String url = "http://" + ip + SubConstants.QUIT_GROUP;
        // 创建请求体 组装参数
        String requestBody = "{\"groupID\": \"" + groupId + "\", \"userID\": \"" + userId + "\"}";
        try {
            String resp = HttpUtils.http(url).bodyOfJson(requestBody).post(true);
            Integer errCode = new JSONObject(resp).getInt("errCode");
            if (errCode == 0) {
                return ResponseUtil.successResponse(null);
            } else {
                return ResponseUtil.errorResponse(errCode, ErrorCodeEnum.fromCode(errCode).getMessage(), null);
            }
        } catch (Exception e) {
            return ResponseUtil.errorResponse(HttpStatus.HTTP_BAD_GATEWAY, e.getMessage(), null);
        }
    }

    @Override
    public Map<String, String> sendBarrage(String groupId, String userId, String content) {
        // 定义请求URL
        String url = "http://" + ip + SubConstants.SEND_MSG;
        // 创建请求体 组装参数
        String requestBody = "{\"sendID\": \"" + userId + "\",\"groupID\": \"" + groupId + "\", \"content\": {\"content\": \"" + content + "\"},\"contentType\": 101 ,\"sessionType\": 3 }";
        try {
            String resp = HttpUtils.http(url).bodyOfJson(requestBody).post(true);
            Integer errCode = new JSONObject(resp).getInt("errCode");
            if (errCode == 0) {
                return ResponseUtil.successResponse(null);
            } else {
                return ResponseUtil.errorResponse(errCode, ErrorCodeEnum.fromCode(errCode).getMessage(), null);
            }
        } catch (Exception e) {
            return ResponseUtil.errorResponse(HttpStatus.HTTP_BAD_GATEWAY, e.getMessage(), null);
        }
    }

    @Override
    public List<OpenImGroupInfoDTO> getGroupsInfo(List<String> groupIds) {
        final String url = "http://" + ip + SubConstants.GROUPS_INFO;

        JSONObject request = new JSONObject();
        request.putOpt("groupIDs", groupIds);

        // 发送请求
        try {
            String resp = HttpUtils.http(url).bodyOfJson(request.toJSONString(2))
                    .header("token", getAdminToken().get("token"))
                    .header("operationID", new Date().getTime() + "")
                    .post(true);

            if (StringUtils.isEmpty(resp)) {
                throw new CommonException("Open Im 响应失败");
            }

            JSONObject resJson = JSONUtil.parseObj(resp);
            Integer errCode = resJson.getInt("errCode");
            if (errCode != 0) {
                log.error("OpenIm获取群组失败,响应:{}", resp);
                return Lists.newArrayList();
            }

            JSONObject data = resJson.getJSONObject("data");
            if (Objects.nonNull(data)) {
                JSONArray groupInfos = data.getJSONArray("groupInfos");
                return groupInfos.stream().map(g -> OpenImGroupInfoDTO.of(JSONUtil.parseObj(g))).toList();
            }
            log.error("OpenIm获取群组,响应:{}", resp);
            return Lists.newArrayList();
        } catch (IOException e) {
            throw new CommonException(url + ": 请求失败", e);
        }
    }

    @Override
    public List<OpenImGroupMemberInfoDTO> getMembersInfo(String groupId, Integer pageNumber, List<OpenImGroupMemberInfoDTO> memberInfos) {

        final String url = "http://" + ip + SubConstants.GROUP_MEMBER_INFO;

        JSONObject request = new JSONObject();
        request.putOpt("groupID", groupId);

        JSONObject pagination = new JSONObject();
        pagination.putOpt("pageNumber", pageNumber);
        pagination.putOpt("showNumber", 100);

        request.putOpt("pagination", pagination);

        // 发送请求
        try {
            String resp = HttpUtils.http(url).bodyOfJson(request.toJSONString(2))
                    .header("token", getAdminToken().get("token"))
                    .header("operationID", new Date().getTime() + "")
                    .post(true);

            if (StringUtils.isEmpty(resp)) {
                throw new CommonException("Open Im 响应失败");
            }

            JSONObject resJson = JSONUtil.parseObj(resp);
            Integer errCode = resJson.getInt("errCode");
            if (errCode != 0) {
                log.error("OpenIm获取群组失败,响应:{}", resp);
                return Lists.newArrayList();
            }

            JSONObject data = resJson.getJSONObject("data");
            if (Objects.nonNull(data)) {
                Integer total = data.getInt("total");
                if (total < 100) {
                    JSONArray groupInfos = data.getJSONArray("members");
                    return groupInfos.stream().map(g -> OpenImGroupMemberInfoDTO.of(JSONUtil.parseObj(g))).toList();
                } else {
                    getMembersInfo(groupId, pageNumber + 1, memberInfos);
                }
                return memberInfos;
            }
            log.error("OpenIm获取群组,响应:{}", resp);
            return Lists.newArrayList();
        } catch (IOException e) {
            throw new CommonException(url + ": 请求失败", e);
        }
    }

    @Override
    public List<OpenImUserOnlineInfoDTO> checkUserOnLine(List<String> userIds) {
        final String url = "http://" + ip + SubConstants.USERS_ONLINE_STATUS;

        JSONObject request = new JSONObject();
        request.putOpt("userIDs", userIds);

        // 发送请求
        try {
            String resp = HttpUtils.http(url).bodyOfJson(request.toJSONString(2))
                    .header("token", getAdminToken().get("token"))
                    .header("operationID", new Date().getTime() + "")
                    .post(true);

            if (StringUtils.isEmpty(resp)) {
                throw new CommonException("Open Im 响应失败");
            }

            JSONObject resJson = JSONUtil.parseObj(resp);
            Integer errCode = resJson.getInt("errCode");
            if (errCode != 0) {
                log.error("OpenIm获取群组失败,响应:{}", resp);
                return Lists.newArrayList();
            }

            JSONArray data = resJson.getJSONArray("data");
            if (Objects.nonNull(data)) {
                return data.stream().map(d -> OpenImUserOnlineInfoDTO.of(JSONUtil.parseObj(d))).toList();
            }
            log.error("OpenIm获取群组,响应:{}", resp);
            return Lists.newArrayList();
        } catch (IOException e) {
            throw new CommonException(url + ": 请求失败", e);
        }
    }

    @Override
    public OpenImSendBusinessNotificationResponseRecord sendBusinessNotification(String sendId, String groupId, String userId, String type, String message) {
        final String url = "http://" + ip + SubConstants.SEND_BUSINESS_NOTIFICATION;

        JSONObject request = new JSONObject();
        request.putOpt("sendUserID", sendId);
        if (StringUtils.isNotEmpty(groupId)) {
            request.putOpt("RecvGroupID", groupId);
            request.putOpt("RecvUserID", "1");
        } else if (StringUtils.isNotEmpty(userId)) {
            request.putOpt("RecvUserID", userId);
            request.putOpt("RecvGroupID", "");
        } else {
            throw new CommonException("发送通知参数错误");
        }
        request.putOpt("key", type);
        request.putOpt("data", message);

        // 是否已消息形式发送，默认: false
        request.putOpt("sendMsg", false);

        /*
            通知消息的可靠基本，
                1: 在线推送。
                2: 必达通知(断线重连或重新登录也会触发，用于必达的场景，该可靠性等级下，由于是顺序全量同步，建议不能发送过多，否则会影响客户端消息同步性能)
             默认: 1
         */
        request.putOpt("reliabilityLevel", 1);

        // 发送请求
        try {
            String resp = HttpUtils.http(url).bodyOfJson(request.toJSONString(2))
                    .header("token", getAdminToken().get("token"))
                    .header("operationID", new Date().getTime() + "")
                    .post(true);

            if (StringUtils.isEmpty(resp)) {
                throw new CommonException("Open Im 响应失败");
            }

            log.info("OpenIm发送业务通知,响应:{}", resp);
            return OpenImSendBusinessNotificationResponseRecord.of(resp);
        } catch (IOException e) {
            throw new CommonException(url + ": 请求失败", e);
        }
    }
}
