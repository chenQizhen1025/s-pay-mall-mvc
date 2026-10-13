package chen.study.mall.domain.res;


import lombok.Data;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/13 18:41
 */
@Data
public class WeixinQrCodeRes {

    private String ticket;
    private Long expire_seconds;
    private String url;

}
