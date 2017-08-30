package com.mtesitoo.helper;

import java.util.HashMap;

/**
 * Created by ptyagi on 8/29/17.
 */

public class ProductPriceHelper {
    static HashMap<String, String> mCurrencyCode = new HashMap<String, String>();

    static {
        mCurrencyCode.put("GMD", "D");
    }

    /**
     * Returns display price at client side
     * @param currencyCode
     * @param price
     * @return
     */
    public static String getDisplayPrice(String currencyCode, String price) {
        String currencySymbol = mCurrencyCode.get(currencyCode);
        if (currencySymbol != null && !currencySymbol.isEmpty()) {
            return currencySymbol + price;
        }

        return price;
    }

    /**
     * Returns "GMD" as default currencyCode.
     * This has to be improved as requirement evolves around supporting more currencies
     * @return
     */
    public static String getDefaultCurrencyCode() {
        return "GMD";
    }

    /**
     * Returns currencySymbol for given currencyCode
     * @param currencyCode
     * @return
     */
    public static String getCurrencySymbol(String currencyCode) {
        return mCurrencyCode.get(currencyCode);
    }
}
