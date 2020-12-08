package com.shop.search.service;

import java.util.Map;

public interface SkuService {
    /***
     * import sku info from goods service and construct index
     */
    void importData();

    /****
     * search by conditions
     * @param searchMap
     * @return
     */
    Map<String, Object> search(Map<String, Object> searchMap);
}
