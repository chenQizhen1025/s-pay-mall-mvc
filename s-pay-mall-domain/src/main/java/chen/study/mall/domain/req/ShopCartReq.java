package chen.study.mall.domain.req;


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
public class ShopCartReq {
    private String userId;
    private String productId;
}
