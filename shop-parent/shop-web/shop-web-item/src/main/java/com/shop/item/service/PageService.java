package com.shop.item.service;

public interface PageService {
    // generate goods info from spuId
    void generateHtml(String spuId);

    void deleteHtml(String spuId);
}
