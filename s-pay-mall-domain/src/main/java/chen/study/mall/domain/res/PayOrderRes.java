package chen.study.mall.domain.res;


import chen.study.mall.common.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/17 13:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderRes {
    private String orderId;
    private String userId;
    private String payUrl;
    //订单状态信息
    private Constants.OrderStatusEnum orderStatus;


}
