package chen.study.mall.service.weixin;


import chen.study.mall.domain.req.ShopCartReq;
import chen.study.mall.domain.res.PayOrderRes;

import javax.annotation.Resource;

/**
 * @author 25986
 * @description 订单服务接口
 * @createTime 2026/10/17 13:43
 */
public interface IOrderService {
    PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception;

}
