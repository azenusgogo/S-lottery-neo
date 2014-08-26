package com.sogou.lottery.util;
import java.util.HashMap;

/**
 * 描述：银行卡校验规则(Luhn算法)
 * 检验数字算法（Luhn Check Digit Algorithm），也叫做模数10公式，是一种简单的算法，用于验证银行卡、信用卡号码的有效性的算法。对所有大型信用卡公司发行的信用卡都起作用，这些公司包括美国Express、护照、万事达卡、Discover和用餐者俱乐部等。这种算法最初是在20世纪60年代由一组数学家制定，现在Luhn检验数字算法属于大众，任何人都可以使用它。
 * 算法：将每个奇数加倍和使它变为单个的数字，如果必要的话通过减去9和在每个偶数上加上这些值。如果此卡要有效，那么，结果必须是10的倍数。
 *  @author haojiaqi
 */
public class BankCardVeryUtils {
    /**
     * 校验银行卡卡号
     * @param cardId
     * @return
     */
    public static boolean  checkBankCard(String cardId) {
        if(cardId == null){
            return false;
        }
        char  bit = getBankCardCheckCode(cardId.substring( 0 , cardId.length() -  1 ));
//        System.out.println("bit:"+bit);
        if (bit ==  'N' ){
            return   false ;
        }
        return  cardId.charAt(cardId.length() -  1 ) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char  getBankCardCheckCode(String nonCheckCodeCardId){
        if (nonCheckCodeCardId ==  null  || nonCheckCodeCardId.trim().length() ==  0
                || !nonCheckCodeCardId.matches("\\d+" )) {
            //如果传的不是数据返回N
            return   'N' ;
        }
        char [] chs = nonCheckCodeCardId.trim().toCharArray();
        int  luhmSum =  0 ;
        for ( int  i = chs.length -  1 , j =  0 ; i >=  0 ; i--, j++) {
            int  k = chs[i] -  '0' ;
            if (j %  2  ==  0 ) {
                k *= 2 ;
                k = k / 10  + k %  10 ;
            }
            luhmSum += k;
        }
        return  (luhmSum %  10  ==  0 ) ?  '0'  : ( char )(( 10  - luhmSum %  10 ) +  '0' );
    }

    public static void main(String[] args) {

        // TODO Auto-generated method stub
        boolean flag = new BankCardVeryUtils().checkBankCard("6225880127281195");
        System.out.println("flag----->"+flag);
    }

}
