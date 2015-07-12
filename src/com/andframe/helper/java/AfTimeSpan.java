package com.andframe.helper.java;

import java.util.Date;
/**
 * java 时间跨度计算
 * @author 树朾
 */
public class AfTimeSpan
{
    private long time;

    public static final long TIME_SECOND = 1000;
    public static final long TIME_MINUTE = 1000*60;
    public static final long TIME_HOUR = 1000*60*60;
    public static final long TIME_DAY = 1000*60*60*24;
    
//    private static double TIME_RECIPROCAL_SECOND = 1/TIME_SECOND;
//    private static double TIME_RECIPROCAL_MINUTE = 1/(TIME_MINUTE);
//    private static double TIME_RECIPROCAL_HOUR = 1/(TIME_HOUR);
//    private static double TIME_RECIPROCAL_DAY = 1/(TIME_DAY);
    //
    // 摘要:
    //     将新的 System.TimeSpan 初始化为指定的刻度数。
    //
    // 参数:
    //   ticks:
    //     以 1 毫秒为单位表示的时间段。
    public AfTimeSpan(long ticks)
    {
        time = ticks;
    }
    //
    // 摘要:
    //     将新的 System.TimeSpan 初始化为指定的小时数、分钟数和秒数。
    //
    // 参数:
    //   hours:
    //     小时数。
    //
    //   minutes:
    //     分钟数。
    //
    //   seconds:
    //     秒数。
    //
    // 异常:
    //   System.ArgumentOutOfRangeException:
    //     该参数指定一个小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue 的 System.TimeSpan
    //     值。
    public AfTimeSpan(int hours, int minutes, int seconds)
    {
        time = 0;
        time += seconds*TIME_SECOND;
        time += minutes*TIME_MINUTE;
        time += hours*TIME_HOUR;
    }
    //
    // 摘要:
    //     将新的 System.TimeSpan 初始化为指定的天数、小时数、分钟数和秒数。
    //
    // 参数:
    //   days:
    //     天数。
    //
    //   hours:
    //     小时数。
    //
    //   minutes:
    //     分钟数。
    //
    //   seconds:
    //     秒数。
    //
    // 异常:
    //   System.ArgumentOutOfRangeException:
    //     该参数指定一个小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue 的 System.TimeSpan
    //     值。
    public AfTimeSpan(int days, int hours, int minutes, int seconds)
    {
        time = 0;
        time += seconds*TIME_SECOND;
        time += minutes*TIME_MINUTE;
        time += hours*TIME_HOUR;
        time += days*TIME_DAY;
    }
    //
    // 摘要:
    //     将新的 System.TimeSpan 初始化为指定的天数、小时数、分钟数、秒数和毫秒数。
    //
    // 参数:
    //   days:
    //     天数。
    //
    //   hours:
    //     小时数。
    //
    //   minutes:
    //     分钟数。
    //
    //   seconds:
    //     秒数。
    //
    //   milliseconds:
    //     毫秒数。
    //
    // 异常:
    //   System.ArgumentOutOfRangeException:
    //     该参数指定一个小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue 的 System.TimeSpan
    //     值。
    public AfTimeSpan(int days, int hours, int minutes, int seconds, int milliseconds)
    {
        time = milliseconds;
        time += seconds*TIME_SECOND;
        time += minutes*TIME_MINUTE;
        time += hours*TIME_HOUR;
        time += days*TIME_DAY;
    }

    // 摘要:
    //     获取当前 System.TimeSpan 结构所表示的时间间隔的天数部分。
    //
    // 返回结果:
    //     此实例的天数部分。返回值可以是正数也可以是负数。
    public int getDays() { 
        return (int)(time/TIME_DAY);
    }
    //
    // 摘要:
    //     获取当前 System.TimeSpan 结构所表示的时间间隔的小时数部分。
    //
    // 返回结果:
    //     当前 System.TimeSpan 结构的小时分量。返回值的范围为 -23 到 23。
    public int getHours() { 
        return (int)(time/TIME_HOUR)%24;
    }
    //
    // 摘要:
    //     获取当前 System.TimeSpan 结构所表示的时间间隔的分钟数部分。
    //
    // 返回结果:
    //     当前 System.TimeSpan 结构的分钟分量。返回值的范围为 -59 到 59。
    public int getMinutes() { 
        return (int)(time/TIME_MINUTE)%60;
    }
    //
    // 摘要:
    //     获取当前 System.TimeSpan 结构所表示的时间间隔的秒数部分。
    //
    // 返回结果:
    //     当前 System.TimeSpan 结构的秒分量。返回值的范围为 -59 到 59。
    public int getSeconds() { 
        return (int)(time/TIME_SECOND)%60;
    }
    //
    // 摘要:
    //     获取当前 System.TimeSpan 结构所表示的时间间隔的毫秒数部分。
    //
    // 返回结果:
    //     当前 System.TimeSpan 结构的毫秒分量。返回值的范围为 -999 到 999。
    public int getMilliseconds() { 
        return (int)(time%TIME_SECOND);
    }
    //
    // 摘要:
    //     获取表示当前 System.TimeSpan 结构的值的刻度数。
    //
    // 返回结果:
    //     此实例包含的刻度数。
    public long getTicks() { 
        return time;
    }
    //
    // 摘要:
    //     获取以整天数和天的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总天数。
    public double getTotalDays() { 
        return 1.0*time/TIME_DAY;
    }
    //
    // 摘要:
    //     获取以整天数和天的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总天数。
    public double getUnSignedTotalDays() { 
        return Math.abs(1.0*time/TIME_DAY);
    }
    //
    // 摘要:
    //     获取以整天数和天的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总天数。
    public long getUnSignedIntegerTotalDays() { 
        return Math.abs(time/TIME_DAY);
    }
    //
    // 摘要:
    //     获取以整小时数和小时的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总小时数。
    public double getTotalHours() { 
        return 1.0*time/TIME_HOUR;
    }
    //
    // 摘要:
    //     获取以整小时数和小时的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总小时数。
    public double getUnSignedTotalHours() { 
        return Math.abs(1.0*time/TIME_HOUR);
    }
    //
    // 摘要:
    //     获取以整小时数和小时的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总小时数。
    public long getUnSignedIntegerTotalHours() { 
        return Math.abs(time/TIME_HOUR);
    }
    //
    // 摘要:
    //     获取以整分钟数和分钟的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总分钟数。
    public double getTotalMinutes() {
        return 1.0*time/TIME_MINUTE;
    }
    //
    // 摘要:
    //     获取以整分钟数和分钟的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总分钟数。
    public double getUnSignedTotalMinutes() {
        return Math.abs(1.0*time/TIME_MINUTE);
    }
    //
    // 摘要:
    //     获取以整分钟数和分钟的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总分钟数。
    public long getUnSignedIntegerTotalMinutes() {
        return Math.abs(time/TIME_MINUTE);
    }
    //
    // 摘要:
    //     获取以整秒数和秒的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总秒数。
    public double getTotalSeconds(){
        return 1.0*time/TIME_SECOND;
    }
    //
    // 摘要:
    //     获取以整毫秒数和毫秒的小数部分表示的当前 System.TimeSpan 结构的值。
    //
    // 返回结果:
    //     此实例表示的总毫秒数。
    public long getTotalMilliseconds(){
        return this.time;
    }

    // 摘要:
    //     将指定的 System.TimeSpan 添加到此实例中。
    //
    // 参数:
    //   ts:
    //     要加上的时间间隔。
    //
    // 返回结果:
    //     一个对象，表示此实例的值加 ts 的值。
    //
    // 异常:
    //   System.OverflowException:
    //     所生成的 System.TimeSpan 小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue。
    public AfTimeSpan Add(AfTimeSpan ts)
    {
        return new AfTimeSpan(this.time+ts.time);
    }

    // 摘要:
    //     将指定的 System.TimeSpan 添加到此实例中。
    //
    // 参数:
    //   ts:
    //     要减去的时间间隔。
    //
    // 返回结果:
    //     一个对象，表示此实例的值减 ts 的值。
    //
    // 异常:
    //   System.OverflowException:
    //     所生成的 System.TimeSpan 小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue。
    public AfTimeSpan Minus(AfTimeSpan ts)
    {
        return new AfTimeSpan(this.time-ts.time);
    }
    //
    // 摘要:
    //     比较两个 System.TimeSpan 值，并返回一个整数，该整数指示第一个值是短于、等于还是长于第二个值。
    //
    // 参数:
    //   t1:
    //     要比较的第一个时间间隔。
    //
    //   t2:
    //     要比较的第二个时间间隔。
    //
    // 返回结果:
    //     以下值之一。值说明-1 t1 短于 t2。0t1 等于 t2。1t1 长于 t2。
    public static int Compare(AfTimeSpan t1, AfTimeSpan t2)
    {
        if(t1.time<t2.time) return -1;
        if(t1.time>t2.time) return 1;
        return 0;
    }
    //
    // 摘要:
    //     比较是否小于 AfTimeSpan t 
    //
    // 参数:
    //   t: 要比较的第时间间隔。
    //
    // 返回结果:
    //     以下值之一。true 小于 t  : false 大于等于 t
    public boolean LessThan(AfTimeSpan t)
    {
        return this.time < t.time;
    }
    //
    // 摘要:
    //     比较是否大于 AfTimeSpan t 
    //
    // 参数:
    //   t: 要比较的第时间间隔。
    //
    // 返回结果:
    //     以下值之一。true 大于 t  : false 小于等于 t
    public boolean GreaterThan(AfTimeSpan t)
    {
        return this.time > t.time;
    }
    //
    // 摘要:
    //     将此实例与指定对象进行比较，并返回一个整数，该整数指示此实例是短于、等于还是长于指定对象。
    //
    // 参数:
    //   value:
    //     要比较的对象，或为 null。
    //
    // 返回结果:
    //     以下值之一。值说明-1此实例短于 value。0此实例等于 value。1此实例长于 value。- 或 -value 为 null。
    //
    // 异常:
    //   System.ArgumentException:
    //     value 不是 System.TimeSpan。
    public int CompareTo(Object value)
    {
        if (value == null)
        {
            return 1;
        }
        try
        {
            long num = ((AfTimeSpan) value).time;
            if (this.time > num)
            {
                return 1;
            }
            if (this.time < num)
            {
                return -1;
            }
        }
        catch(Exception ex)
        {
            return 1;
        }
        return 0;

    }
    //
    // 摘要:
    //     将此实例与指定的 System.TimeSpan 对象进行比较，并返回一个整数，该整数指示此实例是短于、等于还是长于 System.TimeSpan
    //     对象。
    //
    // 参数:
    //   value:
    //     要与此实例进行比较的对象。
    //
    // 返回结果:
    //     一个有符号数字，该数字指示此实例与 value 的相对值。值说明负整数此实例短于 value。零此实例等于 value。正整数此实例长于 value。
    public int Compare(AfTimeSpan value)
    {
        long num = value.time;
        if (this.time > num)
        {
            return 1;
        }
        if (this.time < num)
        {
            return -1;
        }
        return 0;

    }
    //
    // 摘要:
    //     返回新的 System.TimeSpan 对象，其值是当前 System.TimeSpan 对象的绝对值。
    //
    // 返回结果:
    //     一个新对象，其值是当前 System.TimeSpan 对象的绝对值。
    //
    // 异常:
    //   System.OverflowException:
    //     此实例的值为 System.TimeSpan.MinValue。
    public AfTimeSpan Duration()
    {
        return new AfTimeSpan((this.time >= 0L) ? this.time : -this.time);
    }
    //
    // 摘要:
    //     返回一个值，该值指示此实例是否与指定的对象相等。
    //
    // 参数:
    //   value:
    //     与此实例进行比较的对象。
    //
    // 返回结果:
    //     如果 value 是表示与当前 System.TimeSpan 结构具有相同时间间隔的 System.TimeSpan 对象，则为 true；否则为
    //     false。
    public boolean Equals(Object value)
    {
    	if (value instanceof AfTimeSpan) {
            return ((AfTimeSpan)value).time == this.time;
		}
       return false;
    }
    //
    // 摘要:
    //     返回一个值，该值指示此实例是否与指定的 System.TimeSpan 对象相等。
    //
    // 参数:
    //   obj:
    //     与此实例进行比较的对象。
    //
    // 返回结果:
    //     如果 obj 表示的时间间隔与此实例相同，则为 true；否则为 false。
    public boolean Equals(AfTimeSpan obj)
    {
        return obj.time == this.time;
    }
    //
    // 摘要:
    //     返回一个值，该值指示 System.TimeSpan 的两个指定实例是否相等。
    //
    // 参数:
    //   t1:
    //     要比较的第一个时间间隔。
    //
    //   t2:
    //     要比较的第二个时间间隔。
    //
    // 返回结果:
    //     如果 t1 和 t2 的值相等，则为 true；否则为 false。
    public static boolean Equals(AfTimeSpan t1, AfTimeSpan t2)
    {
        return t1.time == t2.time;
    }
    //
    // 摘要:
    //     返回表示指定天数的 System.TimeSpan，其中对天数的指定精确到最接近的毫秒。
    //
    // 参数:
    //   value:
    //     天数，精确到最接近的毫秒。
    //
    // 返回结果:
    //     表示 value 的对象。
    //
    // 异常:
    //   System.OverflowException:
    //     value 小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue。- 或 -value
    //     为 System.Double.PositiveInfinity。- 或 -value 为 System.Double.NegativeInfinity。
    //
    //   System.ArgumentException:
    //     value 等于 System.Double.NaN。
    public static AfTimeSpan FromDays(double value){
        return Interval(value, 0x5265c00); 
    }
    //
    // 摘要:
    //     返回表示指定小时数的 System.TimeSpan，其中对小时数的指定精确到最接近的毫秒。
    //
    // 参数:
    //   value:
    //     精确到最接近的毫秒的小时数。
    //
    // 返回结果:
    //     表示 value 的对象。
    //
    // 异常:
    //   System.OverflowException:
    //     value 小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue。- 或 -value
    //     为 System.Double.PositiveInfinity。- 或 -value 为 System.Double.NegativeInfinity。
    //
    //   System.ArgumentException:
    //     value 等于 System.Double.NaN。
    public static AfTimeSpan FromHours(double value)
    {
        return Interval(value, 0x36ee80);
    }
    //
    // 摘要:
    //     返回表示指定毫秒数的 System.TimeSpan。
    //
    // 参数:
    //   value:
    //     毫秒数。
    //
    // 返回结果:
    //     表示 value 的对象。
    //
    // 异常:
    //   System.OverflowException:
    //     value 小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue。- 或 -value
    //     为 System.Double.PositiveInfinity。- 或 -value 为 System.Double.NegativeInfinity。
    //
    //   System.ArgumentException:
    //     value 等于 System.Double.NaN。
    public static AfTimeSpan FromMilliseconds(double value)
    {
        return Interval(value, 1);
    }
    //
    // 摘要:
    //     返回表示指定分钟数的 System.TimeSpan，其中对分钟数的指定精确到最接近的毫秒。
    //
    // 参数:
    //   value:
    //     分钟数，精确到最接近的毫秒。
    //
    // 返回结果:
    //     表示 value 的对象。
    //
    // 异常:
    //   System.OverflowException:
    //     value 小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue。- 或 -value
    //     为 System.Double.PositiveInfinity。- 或 -value 为 System.Double.NegativeInfinity。
    //
    //   System.ArgumentException:
    //     value 等于 System.Double.NaN。
    public static AfTimeSpan FromMinutes(double value)
    {
        return Interval(value, 0xea60);
    }
    //
    // 摘要:
    //     返回表示指定秒数的 System.TimeSpan，其中对秒数的指定精确到最接近的毫秒。
    //
    // 参数:
    //   value:
    //     秒数，精确到最接近的毫秒。
    //
    // 返回结果:
    //     表示 value 的对象。
    //
    // 异常:
    //   System.OverflowException:
    //     value 小于 System.TimeSpan.MinValue 或大于 System.TimeSpan.MaxValue。- 或 -value
    //     为 System.Double.PositiveInfinity。- 或 -value 为 System.Double.NegativeInfinity。
    //
    //   System.ArgumentException:
    //     value 等于 System.Double.NaN。
    public static AfTimeSpan FromSeconds(double value)
    {
        return Interval(value, 0x3e8);
    }
    //
    // 摘要:
    //     返回表示指定时间的 System.TimeSpan，其中对时间的指定以刻度为单位。
    //
    // 参数:
    //   value:
    //     表示时间的刻度数。
    //
    // 返回结果:
    //     表示 value 的对象。
    public static AfTimeSpan FromTicks(long value){
        return new AfTimeSpan(value);
    }
    //
    // 摘要:
    //     返回表示指定时间的 System.TimeSpan，其中对时间的指定以刻度为单位。
    //
    // 参数:
    //   t1 t2:
    //     一前一后的时间。
    //
    // 返回结果:
    //     表示 t2 - t1 的时间差。
    public static AfTimeSpan FromDate(Date t1,Date t2){
        return new AfTimeSpan(t2.getTime() - t1.getTime());
    }
    //
    // 摘要:
    //     返回表示指定时间的 System.TimeSpan，其中对时间的指定以刻度为单位。
    //
    // 参数:
    //   last:
    //     上一次时间。
    //
    // 返回结果:
    //     表示 last - now（现在） 的时间差。
    public static AfTimeSpan FromLast(Date last){
        return new AfTimeSpan(new Date().getTime() - last.getTime());
    }
    
    private static AfTimeSpan Interval(double value, int scale)
    {
        double num = value * scale;
        double num2 = num + ((value >= 0.0) ? 0.5 : -0.5);
        return new AfTimeSpan(((long) num2));
    }
    /**
     * time 到现在的时间段
     * @param time
     * @return AfTimeSpan
     */
    public static AfTimeSpan After(Date time) {
        return AfTimeSpan.FromDate(time,new Date());
    }
}
