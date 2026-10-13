package chen.study.mall.domain.res;


import lombok.Data;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/13 18:41
 */
@Data
public class WeixinTokenRes {

    private String access_token;
    private int expires_in;
    private String errcode;
    private String errmsg;

}

