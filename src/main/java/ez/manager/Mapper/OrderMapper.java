package ez.manager.Mapper;


import ez.manager.Model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface OrderMapper {

    int deleteByPrimaryKey(String orderId);

    int insert(Order record);

    Order selectByPrimaryKey(String orderId);

    List<Order> list(Order order);

    List<Order> listRefund(Order order);

    List<Order> selectAll();

    Integer selectCurOrderNum();

    Integer selectLastOrderNum();

    Long selectCurPayment();

    Long selectLastPayment();

    Integer selectCurRefundOrder();

    Integer selectLastRefundOrder();

    Integer selectDayOrderSum(Order order);

    Integer selectDayOrderNum(Order order);

    Integer updateByPrimaryKey(Order record);

}
