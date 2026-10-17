package chen.study.mall.service.Impl;

import chen.study.mall.common.constants.Constants;
import chen.study.mall.dao.IOrderDao;
import chen.study.mall.domain.po.PayOrder;
import chen.study.mall.domain.req.ShopCartReq;
import chen.study.mall.domain.res.PayOrderRes;
import chen.study.mall.domain.vo.ProductVO;
import chen.study.mall.service.rpc.ProductRpc;
import chen.study.mall.service.weixin.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/17 13:49
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {
    @Resource
    private IOrderDao orderDao;
    @Resource
    private ProductRpc productRpc;
    @Override
    public PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception {
        //1. 查询当前用户是否存在未支付订单 or 掉单（有流水单没支付单）
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setUserId(shopCartReq.getUserId());
        payOrderReq.setProductId(shopCartReq.getProductId());

        PayOrder unpaidOrder = orderDao.queryUnPayOrder(payOrderReq);
        if (unpaidOrder != null && Constants.OrderStatusEnum.PAY_WAIT.getCode().equals(unpaidOrder.getStatus())) {
            log.info("创建订单-已存在，已存在未支付订单。userId:{}，productId:{},orderId:{}", shopCartReq.getUserId(), shopCartReq.getProductId(), unpaidOrder.getOrderId());
            return PayOrderRes.builder()
                    .orderId(unpaidOrder.getOrderId())
                    .payUrl(unpaidOrder.getPayUrl())
                    .build();
        } else if (null != unpaidOrder && Constants.OrderStatusEnum.CREATE.getCode().equals(unpaidOrder.getStatus())) {
            //todo 订单超时不存在
        }
        //2 查询商品&创建订单
        ProductVO productVO = productRpc.queryProductByProductId(shopCartReq.getProductId());
        String orderId = RandomStringUtils.randomNumeric(16);
        orderDao.insert(
                PayOrder.builder()
                        .orderId(orderId)
                        .productId(productVO.getProductId())
                        .productName(productVO.getProductName())
                        .userId(shopCartReq.getUserId())
                        .orderTime(new Date())
                        .totalAmount(productVO.getPrice())
                        .status(Constants.OrderStatusEnum.CREATE.getCode())
                        .build()
        );
        // 创建支付单，调用支付宝todo
        return PayOrderRes.builder()
                .orderId(orderId)
                .payUrl("暂无")
                .build();
    }
}
