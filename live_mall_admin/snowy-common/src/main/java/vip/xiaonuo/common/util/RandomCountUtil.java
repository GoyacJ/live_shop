package vip.xiaonuo.common.util;

import java.util.Random;

public class RandomCountUtil {

    /**
     * 生成随机数 用作订单编号
     * @return
     */
    public static String getRandomCount() {
        Random random=new Random();
        char[] numbers=new char[12];
        for (int i = 0; i <numbers.length; i++) {
            if(i==0){
                numbers[i]=(char)(random.nextInt(9)+49);
            }else{
                numbers[i]=(char)(random.nextInt(10)+48);
            }
        }
        return new String(numbers);
    }
}
