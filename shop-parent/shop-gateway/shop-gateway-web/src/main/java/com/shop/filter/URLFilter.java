package com.shop.filter;

/***
 * release urls accessing public resources
 */
public class URLFilter {

    // requests accessing public resource : user register, user login
    private static final String publicUrls = "/api/user/add,/user/login";

    public static boolean isPublicResources(String url) {
        String[] split = publicUrls.split(",");
        for (String s : split) {
            if (s.equals(url)) {
                return true;
            }
        }
        return false;
    }
}
