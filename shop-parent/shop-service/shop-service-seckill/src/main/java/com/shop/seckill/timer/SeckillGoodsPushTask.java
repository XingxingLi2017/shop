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

/***
 * push goods info into redis cache
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * push goods info into redis every 30 sec
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void loadGoodsInfo(){
        List<Date> dateMenus = DateUtil.getDateMenus();

        // get goods in the time intervals
        for (Date time : dateMenus) {
            String timeInterval = DateUtil.date2Str(time);

            // construct query
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", "1");
            criteria.andGreaterThan("stockCount", 0);
            criteria.andGreaterThanOrEqualTo("startTime", time);
            criteria.andLessThan("endTime", DateUtil.addDateHour(time, 2));
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

            // put into redis
            for (SeckillGoods goods : seckillGoods) {
                redisTemplate.boundHashOps(timeInterval).put(goods.getId(), goods);
            }
        }
    }
}
