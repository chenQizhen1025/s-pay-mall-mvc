package chen.study.mall.service.rpc;


import chen.study.mall.domain.vo.ProductVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/17 14:04
 */
@Service
public class ProductRpc {
    public ProductVO queryProductByProductId(String productId) {
        //模拟查询
        ProductVO productVO = new ProductVO();
        productVO.setProductId("10001");
        productVO.setProductName("测试商品");
        productVO.setProductDesc("this is test product");
        productVO.setPrice(new BigDecimal("1.11"));
        return productVO;
    }
}
