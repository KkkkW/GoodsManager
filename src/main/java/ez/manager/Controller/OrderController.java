package ez.manager.Controller;


import ez.manager.Mapper.OrderItemMapper;
import ez.manager.Mapper.OrderMapper;
import ez.manager.Model.Order;
import ez.manager.Model.OrderItem;
import ez.manager.Util.DateUtil;
import ez.manager.Util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 订单管理
 */
@Controller
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;


    List<Order> orderList;

    @RequestMapping("/user/OrderManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String orderManage(Order order, @PathVariable Integer pageCurrent,
                              @PathVariable Integer pageSize,
                              @PathVariable Integer pageCount,
                              Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;
        order.setMinOrderTime(DateUtil.strToDate(order.getMinOrderTimeStr()));
        order.setMaxOrderTime(DateUtil.strToDate(order.getMaxOrderTimeStr()));
        int rows = orderMapper.list(order).size();
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        order.setStart((pageCurrent - 1) * pageSize);
        order.setEnd(pageSize);
        orderList = orderMapper.list(order);
        for (Order order1 : orderList) {
            String orderId = order1.getOrderId();
            if (orderItemMapper.selectByPrimaryOrderKey(orderId) != null) {
                OrderItem orderItem = orderItemMapper.selectByPrimaryOrderKey(orderId);
                order1.setItemTitle(orderItem.getTitle());
                order1.setTotalFee(orderItem.getTotalFee());
                order1.setNum(orderItem.getNum());
                order1.setStatusStr(getStatusStrById(order1.getStatus()));
                order1.setDateStr1(DateUtil.getDateStr(order1.getCreateTime()));
                order1.setPaymentTypeStr(getPaymentTypeById(order1.getPaymentType()));
            }
        }
        model.addAttribute("orderList", orderList);
        String pageHTML = PageUtil.getPageContent("MerchandiseManage_{pageCurrent}_{pageSize}_{pageCount}?title=" + order.getItemTitle() + "&orderId" + order.getOrderId() + "&minOrderTimeStr" + order.getMinOrderTimeStr() + "&maxOrderTimeStr" + order.getMaxOrderTimeStr() + "&status" + order.getStatus(), pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("order", order);
        return "OrderManage/OrderManage";
    }


    @GetMapping("/user/OrderDetails")
    public String orderDetailsGet(Model model, Order order) {
        String orderId = order.getOrderId();
        Order order1 = orderMapper.selectByPrimaryKey(orderId);
        if (orderItemMapper.selectByPrimaryOrderKey(orderId) != null) {
            OrderItem orderItem = orderItemMapper.selectByPrimaryOrderKey(orderId);
            order1.setItemTitle(orderItem.getTitle());
            order1.setTotalFee(orderItem.getTotalFee());
            order1.setNum(orderItem.getNum());
            order1.setStatusStr(getStatusStrById(order1.getStatus()));
            order1.setItemId(orderItem.getItemId());
            order1.setBuyerRateStr(getbuyerRateStrById(order1.getBuyerRate()));
            order1.setDateStr1(DateUtil.getDateStr(order1.getCreateTime()));
            order1.setDateStr2(DateUtil.getDateStr(order1.getUpdateTime()));
            order1.setDateStr3(DateUtil.getDateStr(order1.getPaymentTime()));
            order1.setDateStr4(DateUtil.getDateStr(order1.getConsignTime()));
            order1.setDateStr5(DateUtil.getDateStr(order1.getEndTime()));
            order1.setPaymentTypeStr(getPaymentTypeById(order1.getPaymentType()));

            model.addAttribute("order", order1);
        }
        return "OrderManage/OrderDetails";
    }

    @RequestMapping("/user/OrderRefund_{pageCurrent}_{pageSize}_{pageCount}")
    public String RefundManage(Order order, @PathVariable Integer pageCurrent,
                               @PathVariable Integer pageSize,
                               @PathVariable Integer pageCount,
                               Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;


        int rows = orderMapper.list(order).size();
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        order.setStart((pageCurrent - 1) * pageSize);
        order.setEnd(pageSize);
        List<Order> orderList = orderMapper.listRefund(order);
        for (Order order1 : orderList) {
            String orderId = order1.getOrderId();
            if (orderItemMapper.selectByPrimaryOrderKey(orderId) != null) {
                OrderItem orderItem = orderItemMapper.selectByPrimaryOrderKey(orderId);
                order1.setItemTitle(orderItem.getTitle());
                order1.setTotalFee(orderItem.getTotalFee());
                order1.setNum(orderItem.getNum());
                order1.setStatusStr(getStatusStrById(order1.getStatus()));
                order1.setDateStr1(DateUtil.getDateStr(order1.getCreateTime()));
                order1.setPaymentTypeStr(getPaymentTypeById(order1.getPaymentType()));
                order1.setRefundStatusStr(getRefundStatusStr(order1.getRefundStatus()));
            }
        }
        model.addAttribute("orderList", orderList);
        String pageHTML = PageUtil.getPageContent("OrderRefund_{pageCurrent}_{pageSize}_{pageCount}?refundStatus=" + order.getRefundStatus(), pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("order", order);
        return "OrderManage/OrderRefund";
    }

    @GetMapping("/user/OrderCheck")
    public String orderCheckGet(Model model, Order order) {
        String orderId = order.getOrderId();
        Order order1 = orderMapper.selectByPrimaryKey(orderId);
        if (orderItemMapper.selectByPrimaryOrderKey(orderId) != null) {
            OrderItem orderItem = orderItemMapper.selectByPrimaryOrderKey(orderId);
            order1.setItemTitle(orderItem.getTitle());
            order1.setTotalFee(orderItem.getTotalFee());
            order1.setNum(orderItem.getNum());
            order1.setStatusStr(getStatusStrById(order1.getStatus()));
            order1.setItemId(orderItem.getItemId());
            order1.setBuyerRateStr(getbuyerRateStrById(order1.getBuyerRate()));
            order1.setDateStr1(DateUtil.getDateStr(order1.getCreateTime()));
            order1.setDateStr3(DateUtil.getDateStr(order1.getPaymentTime()));
            order1.setPaymentTypeStr(getPaymentTypeById(order1.getPaymentType()));
            order1.setRefundStatus(null);
            model.addAttribute("order", order1);
        }
        return "OrderManage/OrderCheck";
    }

    @PostMapping("/user/OrderCheck")
    public String orderCheckPost(Model model, @RequestParam MultipartFile[] imageFile, Order order, HttpSession httpSession) {

        if (order.getRefundStatus() == null) {
            order.setRefundStatus(0);
        }
        if (order.getOrderId() != null) {
            orderMapper.updateByPrimaryKey(order);
        }
        return "redirect:OrderRefund_0_0_0";
    }


    public String getbuyerRateStrById(int i) {
        switch (i) {
            case 0:
                return "否";
            case 1:
                return "是";
        }
        return null;
    }


    public String getStatusStrById(int i) {
        switch (i) {
            case 1:
                return "未付款";
            case 2:
                return "已付款";
            case 3:
                return "未发货";
            case 4:
                return "已发货";
            case 5:
                return "交易成功";
            case 6:
                return "交易关闭";
        }
        return null;
    }

    public String getPaymentTypeById(int i) {
        switch (i) {
            case 1:
                return "在线支付";
            case 2:
                return "货到付款";
        }
        return null;
    }

    public String getRefundStatusStr(int i) {
        switch (i) {
            case 1:
                return "申请退款";
            case 2:
                return "退款失败";
            case 3:
                return "退款成功";
        }
        return null;
    }

}
