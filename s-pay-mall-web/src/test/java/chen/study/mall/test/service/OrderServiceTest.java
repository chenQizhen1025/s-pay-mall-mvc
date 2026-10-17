package chen.study.mall.test.service;


import chen.study.mall.domain.req.ShopCartReq;
import chen.study.mall.domain.res.PayOrderRes;
import chen.study.mall.service.weixin.IOrderService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.Resource;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/17 14:20
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
    @Resource
    private IOrderService orderService;

    @Test
    public void test_createOrder() throws Exception {
        ShopCartReq shopCartReq = new ShopCartReq();
        shopCartReq.setUserId("cqz");
        shopCartReq.setProductId("10011");
        PayOrderRes payOrderRes = orderService.createOrder(shopCartReq);
        log.info("request：{}", JSON.toJSONString(shopCartReq));
        log.info("测试结果：{}", JSON.toJSONString(payOrderRes));
    }
}
