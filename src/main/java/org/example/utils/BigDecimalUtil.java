package org.example.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalUtil {
    private static final int DEFAULT_SCALE = 6;
    private static final int DEFAULT_TOTAL_SCALE = 10;
    private static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.HALF_UP;
    private static final BigDecimal DEFAULT_DIVIDEND = new BigDecimal("1");
    private static final BigDecimal MAX_VALUE= new BigDecimal("9999.999999");

    private BigDecimalUtil(){}

    public static boolean isValidRateValue(String reqRate) {
         if(reqRate == null|| reqRate.trim().isEmpty()){
             return false;
         }

         String tempRate = reqRate.trim();

         if(tempRate.contains(",")){
         tempRate = tempRate.replace(",", ".");
         }

         try {
             BigDecimal rate = new BigDecimal(tempRate);

             if(rate.scale() > DEFAULT_SCALE||rate.precision()>DEFAULT_TOTAL_SCALE){
                 return false;
             }

             return rate.compareTo(MAX_VALUE) <= 0 && rate.compareTo(BigDecimal.ZERO) > 0;
         }catch (NumberFormatException e){
             return false;
         }
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return dividend.divide(divisor, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }
    public static BigDecimal divideWithDefaultDividend(BigDecimal divisor) {
        return BigDecimalUtil.divide(DEFAULT_DIVIDEND,divisor);
    }

    public static BigDecimal multiply(BigDecimal multiplied, BigDecimal multiplier) {
        return multiplied.multiply(multiplier, MathContext.DECIMAL32);
    }


}
