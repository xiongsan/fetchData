package com.fable.enclosure.bussiness.util;

import java.util.Random;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/1/17
 * Time :11:16
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class AlgoritheOfPi {
    /*假设圆的半径为1，那么四分之一圆的面积为pi/4 包含四分之一圆的面积为1 ，现在此正方形上投射飞镖总飞镖数为n，落在四分之一圆内的飞镖为c
    * 那么得到pi/4:1=c:n 得到pi=4*c/n,现在我们要得到乱数坐标x y 使得 xx+yy<1能判断出在圆内
    * */
    public static void main(String[] args) {
        Random random = new Random();
        int sum=0;
        int N=500000000;
        double x;
        double y;
        for(int i=0;i<=N;i++){
            x = random.nextDouble();
            y = random.nextDouble();
            if((Math.pow(x,2.0)+Math.pow(y,2.0)<1)){
                sum++;
            }
        }
        System.out.println((double) 4*sum/N);

    }
}
