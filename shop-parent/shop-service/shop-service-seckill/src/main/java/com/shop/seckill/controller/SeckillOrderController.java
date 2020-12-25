package com.shop.seckill.controller;

import com.shop.entity.Result;
import com.shop.entity.SeckillStatus;
import com.shop.entity.StatusCode;
import com.shop.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckill/order")
@CrossOrigin
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    private String username = "zhangsan";

    /***
     * get user order's status in the queue
     */
    @GetMapping(value = "/query")
    public Result queryStatus(){
        SeckillStatus status = seckillOrderService.queryStatus(username);
        if(status != null) {
            return new Result(true, StatusCode.OK, "Get order status successfully." , status);
        }
        return new Result(false, StatusCode.ERROR, "Fail to get the limited-time offer" );
    }

    /***
     * add seckill order
     */
    @RequestMapping("/add")
    public Result add(String time, Long id) {
        seckillOrderService.add(time, id, username);
        return new Result(true, StatusCode.OK, "You are in the line now......");
    }
}
