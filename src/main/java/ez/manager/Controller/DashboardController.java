package ez.manager.Controller;


import ez.manager.Mapper.OrderMapper;
import ez.manager.Model.Order;
import ez.manager.Model.ResObject;
import ez.manager.Model.Stats;
import ez.manager.Model.User;
import ez.manager.Redis.Dashboardkey;
import ez.manager.Redis.RedisService;
import ez.manager.Util.Constant;
import ez.manager.Util.RunnableThreadWebCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;

    @GetMapping("/user/dashboard")
    public String dashboard(Model model, Stats stats) {

        Long mIncome, lastIncome;
        Integer curOrderNum, preOrderNum, curRefundOrder, lastRefundOrder, orderNum, orderSum;
        mIncome = orderMapper.selectCurPayment();
        //全部加缓存
//        mIncome = redisService.get(Dashboardkey.board, "mIncome", Long.class);
//        if (mIncome == null) {
//
//            mIncome = mIncome == null ? 0L : mIncome;
//            redisService.set(Dashboardkey.board, "mIncome", mIncome);
//        }
        lastIncome = orderMapper.selectLastPayment();
//        lastIncome = redisService.get(Dashboardkey.board, "lastIncome", Long.class);
//        if (lastIncome == null) {
//
//            lastIncome = lastIncome == null ? 0L : lastIncome;
//            redisService.set(Dashboardkey.board, "lastIncome", lastIncome);
//        }
        curOrderNum = orderMapper.selectCurOrderNum();
//        curOrderNum = redisService.get(Dashboardkey.board, "curOrderNum", Integer.class);
//        if (curOrderNum == null) {
//
//            curOrderNum = curOrderNum == null ? 0 : curOrderNum;
//            redisService.set(Dashboardkey.board, "curOrderNum", curOrderNum);
//        }
        preOrderNum = orderMapper.selectLastOrderNum();
//        preOrderNum = redisService.get(Dashboardkey.board, "preOrderNum", Integer.class);
//        if (preOrderNum == null) {
//
//            preOrderNum = preOrderNum == null ? 0 : preOrderNum;
//            redisService.set(Dashboardkey.board, "preOrderNum", preOrderNum);
//        }
        curRefundOrder = orderMapper.selectCurRefundOrder();
//        curRefundOrder = redisService.get(Dashboardkey.board, "preOrderNum", Integer.class);
//        if (curRefundOrder == null) {
//
//            curRefundOrder = curRefundOrder == null ? 0 : curRefundOrder;
//            redisService.set(Dashboardkey.board, "curRefundOrder", curRefundOrder);
//        }
        lastRefundOrder = orderMapper.selectLastRefundOrder();
//        lastRefundOrder = redisService.get(Dashboardkey.board, "lastRefundOrder", Integer.class);
//        if (lastRefundOrder == null) {
//
//            lastRefundOrder = lastRefundOrder == null ? 0 : lastRefundOrder;
//            redisService.set(Dashboardkey.board, "lastRefundOrder", lastRefundOrder);
//        }

        int count = RunnableThreadWebCount.addCount((User)httpSession.getAttribute("user"));


        stats.setPv(count);//访问量
        stats.setOrderNumPer(getPer(curOrderNum, preOrderNum));//月订单数环比
        stats.setMOrderNum(orderMapper.selectCurOrderNum());//月订单数
        //stats.setMIncome(mIncome);//月收入
        //stats.setIncomePer(getPer(mIncome, lastIncome));//月收入环比
        //stats.setMOrderRefund(orderMapper.selectCurRefundOrder());
        //stats.setMOrderRefundPer(getPer(curRefundOrder, lastRefundOrder));

        model.addAttribute("dashboard", stats);

        List<Integer> data2 = new ArrayList<>();
        List<Integer> data3 = new ArrayList<>();

        Date now = new Date();
        //获取三十天前日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(now);
        theCa.add(theCa.DATE, -31);//最后一个数字30可改，30天的意思

        Date temp = new Date();
        Order order = new Order();
        for (int i = 0; i < 31; i++) {
            theCa.add(theCa.DATE, 1);
            temp = theCa.getTime();
            order.setCreateTime(temp);

            orderNum = orderMapper.selectDayOrderNum(order);
            //每天的订单数
//            orderNum = redisService.get(Dashboardkey.board, "orderNum", Integer.class);
//            if (orderNum == null) {
//                orderNum = orderMapper.selectDayOrderNum(order);
//                orderNum = orderNum == null ? 0 : orderNum;
//                redisService.set(Dashboardkey.board, "orderNum", orderNum);
//            }
            //每天的收入
            orderSum = orderMapper.selectDayOrderSum(order);
//            orderSum = redisService.get(Dashboardkey.board, "orderSum", Integer.class);
//            if (orderSum == null) {
//
//                orderSum = orderSum == null ? 0 : orderSum;
//                redisService.set(Dashboardkey.board, "orderSum", orderSum);
//            }
            data2.add(orderNum);
            data3.add(orderSum);
        }

        model.addAttribute("data2", data2);
        model.addAttribute("data3", data3);
        return "dashboard";
    }

    public String getPer(long a, long b) {
        StringBuilder orderNumPer = new StringBuilder();
        double differ = a - b;
        double d = differ / a;
        String s = String.format("%.2f", d);
        orderNumPer.append(s).append("%");
        return orderNumPer.toString();
    }

}
