package com.shop.seckill.timer;

import com.shop.seckill.dao.SeckillGoodsMapper;
import com.shop.seckill.pojo.SeckillGoods;
import com.shop.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import java.util.Date;
import java.util.List;
import java.util.Set;

/***
 * push goods info into redis cache
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // queue for preventing oversold
    private static final String GOODS_OVERSOLD_QUEUE_PREFIX = "GoodsOversoldQueue_";

    /***
     * push goods info into redis every 30 sec
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void loadGoodsInfo(){
        List<Date> dateMenus = DateUtil.getDateMenus();

        // get goods in the time intervals
        for (Date time : dateMenus) {
            String timeInterval = "SeckillGoods_" + DateUtil.date2Str(time);
            long timestamp = time.getTime();
            Date newDate = new Date(timestamp);
            // construct query
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", "1");
            criteria.andGreaterThan("stockCount", 0);
            criteria.andGreaterThanOrEqualTo("startTime", time);    // UTC timezone for DB
            Date endTime = DateUtil.addDateHour(time, 2);
            criteria.andLessThan("endTime", endTime);
            Set keys = redisTemplate.boundHashOps(timeInterval).keys();
            if(keys != null && keys.size() > 0) {
                criteria.andNotIn("id", keys);
            }

            // get goods info
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);


            // put into redis
            for (SeckillGoods goods : seckillGoods) {
                redisTemplate.boundHashOps(timeInterval).put(goods.getId(), goods);

                // prevent oversold
                redisTemplate.boundHashOps(GOODS_OVERSOLD_QUEUE_PREFIX).increment(goods.getId(), goods.getStockCount());
            }
        }
    }
}
