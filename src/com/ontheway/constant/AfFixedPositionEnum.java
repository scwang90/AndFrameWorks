package com.ontheway.constant;

/**
 * @author SCWANG
 *
 */
public class AfFixedPositionEnum
{
    /**
     * GPS定位的状态
     */
    public static final int NONE = 0;        //还没有定位
    public static final int FIXEDING = 1;    //正在定位
    public static final int FIXED = 2;       //已经定位
    public static final int FAIL = 3;        //定位失败
}
