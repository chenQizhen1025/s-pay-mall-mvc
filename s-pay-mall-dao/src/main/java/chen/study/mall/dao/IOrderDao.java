package chen.study.mall.dao;


import chen.study.mall.domain.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 25986
 * @description
 * @createTime 2026/10/17 13:36
 */
@Mapper
public interface IOrderDao {
    void insert(PayOrder payOrder);
    PayOrder queryUnPayOrder(PayOrder payOrder);
}
