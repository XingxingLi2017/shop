package com.shop.search.controller;

import com.shop.entity.Page;
import com.shop.search.feign.SkuFeign;
import com.shop.search.pojo.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/search")
public class SkuController {

    @Autowired
    SkuFeign skuFeign;

    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map<String, Object> searchMap , Model model) {
        Map resultMap = skuFeign.search(searchMap);
        model.addAttribute("result" , resultMap);
        model.addAttribute("searchMap", searchMap);

        // remain the origin request url used in frontend page
        String[] urls = url(searchMap);
        model.addAttribute("url",urls[0]);
        model.addAttribute("sortUrl",urls[1]);

        // pagination
        Page<SkuInfo> infoPage = new Page<SkuInfo>(
                Long.valueOf(resultMap.get("total").toString()),
                Integer.valueOf(resultMap.get("pageNum").toString()),
                Integer.valueOf(resultMap.get("pageSize").toString())
        );

        model.addAttribute("page",infoPage);

        return "search";
    }

    private String[] url(Map<String, Object> searchMap){
        String ret = "/search/list";
        String sortRet = "/search/list";
        StringBuilder sortUrl = new StringBuilder(ret);
        StringBuilder url = new StringBuilder(sortRet);
        if(searchMap != null && searchMap.size() > 0) {
            url.append("?");
            sortUrl.append("?");
            for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                String key = entry.getKey();    // keywords / brand  / category
                String value = (String) entry.getValue();

                // don't need to keep pageNum in response
                if(key.equals("pageNum")){
                    continue;
                }
                url.append(key+"="+value+"&");
                if(!key.equalsIgnoreCase("sortField") && !key.equalsIgnoreCase("sortRule")) {
                    sortUrl.append(key+"="+value+"&");
                }
            }
            ret = url.toString();
            if(ret.lastIndexOf("&")!=-1){
                ret = ret.substring(0, ret.lastIndexOf("&"));
            }
            sortRet = sortUrl.toString();
            if(sortRet.lastIndexOf("&")!=-1){
                sortRet = sortRet.substring(0, ret.lastIndexOf("&"));
            }
        }
        return new String[]{ret, sortRet};
    }
}
