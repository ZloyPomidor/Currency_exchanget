package org.example.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalUtil {
    public static final int DEFAULT_SCALE = 6;
    public static final int DEFAULT_TOTAL_SCALE = 10;
    public static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.HALF_UP;
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

             if(rate.compareTo(MAX_VALUE)>0 && rate.compareTo(BigDecimal.ZERO)<0){
                 return false;
             }

             return true;

         }catch (NumberFormatException e){
             return false;
         }
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return dividend.divide(divisor, DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }

    public static BigDecimal multiply(BigDecimal multiplied, BigDecimal multiplier) {
        return multiplied.multiply(multiplier, MathContext.DECIMAL32);
    }


}
