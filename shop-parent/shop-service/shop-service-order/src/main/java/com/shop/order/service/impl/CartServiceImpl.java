package com.shop.order.service.impl;

import com.shop.entity.Result;
import com.shop.goods.feign.SkuFeign;
import com.shop.goods.feign.SpuFeign;
import com.shop.goods.pojo.Sku;
import com.shop.goods.pojo.Spu;
import com.shop.order.pojo.OrderItem;
import com.shop.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SkuFeign skuFeign;

    @Autowired
    SpuFeign spuFeign;

    @Override
    public void add(Integer num, Long skuId , String username) {
        // handle negative number
        if(num <= 0) {
            redisTemplate.boundHashOps("Cart_" + username).delete(skuId);

            // remove empty cart
            List values = redisTemplate.boundHashOps("Cart_" + username).values();
            if(values != null && values.size() <= 0) {
                redisTemplate.delete("Cart_" + username);
            }
            return;
        }

        // get spu and sku
        Result<Sku> skuResult = skuFeign.findById(skuId + "");
        Sku sku = skuResult.getData();

        Result<Spu> spuResult = spuFeign.findById(sku.getSpuId());
        Spu spu = spuResult.getData();

        OrderItem orderItem = createOrderItem(num, sku, spu);

        // save into redis
        redisTemplate.boundHashOps("Cart_" + username).put(skuId, orderItem);
    }

    /***
     * list order items in user's cart
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        return redisTemplate.boundHashOps("Cart_" + username).values();
    }

    private OrderItem createOrderItem(Integer num, Sku sku, Spu spu) {
        // convert to order item
        OrderItem orderItem = new OrderItem();
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSkuId(Long.parseLong(sku.getId()));
        orderItem.setSpuId(Long.parseLong(spu.getId()));
        orderItem.setNum(num);
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setMoney(num * orderItem.getPrice());
        orderItem.setImage(spu.getImage());
        return orderItem;
    }
}
