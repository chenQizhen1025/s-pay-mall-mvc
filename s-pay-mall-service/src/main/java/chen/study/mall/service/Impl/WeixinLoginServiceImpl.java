package chen.study.mall.service.Impl;


import chen.study.mall.common.exception.AppException;
import chen.study.mall.domain.po.WeixinTemplateMessageVO;
import chen.study.mall.domain.req.WeixinQrCodeReq;
import chen.study.mall.domain.res.WeixinQrCodeRes;
import chen.study.mall.domain.res.WeixinTokenRes;
import chen.study.mall.service.ILoginService;
import chen.study.mall.service.weixin.IWeixinApiService;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.cache.Cache;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author 25986
 * @description
 * @createTime 2026/10/13 18:37
 */
@Service
public class WeixinLoginServiceImpl implements ILoginService {
    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template_id}")
    private String template_id;
    @Resource
    private Cache<String, String> weixinAccessToken;
    @Resource
    private IWeixinApiService weixinApiService;
    @Resource
    public Cache<String, String> openidToken;
    @Resource
    private RedissonClient redissonClient;

    private static final String ACCESS_TOKEN_KEY = "weixin:access_token:%s";
    //提前五分钟丢弃
    private static final long REFRESH_AHEAD_SECONDS = 300;


//    private String getAccessToken() throws Exception {
//        String key = String.format(ACCESS_TOKEN_KEY, appid);
//        RBucket<String> bucket = redissonClient.getBucket(key);   // ① 拿到这个 key 的存储桶
//
//        // ② 先读缓存
//        String token = bucket.get();
//        if (token != null) {
//            return token;
//        }
//
//        // ③ 没缓存 → 加分布式锁，防止并发/多实例同时去微信刷新（会把 token 互相作废）
//        RLock lock = redissonClient.getLock(key + ":lock");
//        try {
//            if (!lock.tryLock(5, 30, TimeUnit.SECONDS)) {
//                Thread.sleep(200);              // 抢不到锁 = 别人正在刷新，稍等重试
//                return getAccessToken();
//            }
//            // ④ 双重检查：拿到锁后再读一次，可能已被别的线程刷新
//            token = bucket.get();
//            if (token != null) {
//                return token;
//            }
//            // ⑤ 调微信接口刷新
//            WeixinTokenRes res = weixinApiService
//                    .getToken("client_credential", appid, appSecret)//构建请求
//                    .execute().body();//拿到响应体
//            if (res == null || (res.getErrcode() != null && !"0".equals(res.getErrcode()))) {
//                throw new AppException("wx_token_error", "获取微信 access_token 失败: " + res);
//            }
//            token = res.getAccess_token();
//            long ttl = Math.max(res.getExpires_in() - REFRESH_AHEAD_SECONDS, 1);
//            // ⑥ Redisson 写回并设置过期时间
//            bucket.set(token, ttl, TimeUnit.SECONDS);
//            return token;
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

    @Override
    public String createQrCodeTicket() throws Exception {
        String accessToken = weixinAccessToken.getIfPresent(appid);
//        String accessToken = getAccessToken();
        if (null == accessToken) {
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            accessToken = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid, accessToken);
        }

        // 2. 生成 ticket
        WeixinQrCodeReq weixinQrCodeReq = WeixinQrCodeReq.builder()
                .expire_seconds(2592000)
                .action_name(WeixinQrCodeReq.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeReq.ActionInfo.builder()
                        .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                .scene_id(100601)
                                .build())
                        .build())
                .build();

        Call<WeixinQrCodeRes> call = weixinApiService.createQrCode(accessToken, weixinQrCodeReq);
        WeixinQrCodeRes weixinQrCodeRes = call.execute().body();
        assert null != weixinQrCodeRes;
        return weixinQrCodeRes.getTicket();
    }


    @Override
    public String checkLogin(String ticket) {
        return openidToken.getIfPresent(ticket);
    }

    @Override
    public void saveLoginState(String ticket, String openid) throws IOException {
        openidToken.put(ticket, openid);

        // 1. 获取 accessToken 【实际业务场景，按需处理下异常】
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if (null == accessToken){
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            accessToken = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid, accessToken);
        }

        // 2. 发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageVO.put(data, WeixinTemplateMessageVO.TemplateKey.USER, openid);

        WeixinTemplateMessageVO templateMessageDTO = new WeixinTemplateMessageVO(openid, template_id);
        templateMessageDTO.setUrl("https://gaga.plus");
        templateMessageDTO.setData(data);

        Call<Void> call = weixinApiService.sendMessage(accessToken, templateMessageDTO);
        call.execute();

    }
}
