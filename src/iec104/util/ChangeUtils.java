package iec104.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import iec104.IeShortFloat;

/* 数据转换类工具包 */

public class ChangeUtils {
	private static final char[] HEX_CHAR_TABLE = {
		'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'	
	};
	
	public static String toHexString(Byte[] data){
        byte[] resultBytes = new byte[data.length];
        for(int i =0 ;i<data.length;i++){
            resultBytes[i] = data[i];
        }
        return toHexString(resultBytes);
    }
	
	public static String toHexString(byte[] data){
        if(data == null || data.length == 0)
            return null;
        byte[] hex = new byte[data.length * 2];
        int index = 0;
        for(byte b : data){
            int v = b & 0xFF;
            hex[index++] = (byte) HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = (byte) HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex);
    }
	
	
	/* 16进制字符串转换为对应的字节流数据 */
	public static byte[] hexStringToBytes(String data) {
		// 字符串为null或空时，返回null
		if(data == null || "".equals(data)) {
			return null;
		}
		// 字符串大写转换
		data = data.toUpperCase();
		// 字符串数/2等于字节数
		int length = data.length()/2;
		// 转换为字符串数组
		char[] dataChars = data.toCharArray();
		// 创建字节数组
		byte[] byteData = new byte[length];
		for (int i = 0; i<length; i++) {
			int pos = i*2;
			byteData[i] = (byte)(charToByte(dataChars[pos])<<4 | charToByte(dataChars[pos+1]));
		}
		return byteData;
	};
	/* 字符转换对应字节 */
	public static byte charToByte(char c) {
		return (byte)"0123456789ABCDEF".indexOf(c);
	}
	
	public static String byteAppend(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=bytes.length-1;i>=0;i--){
            stringBuffer.append(String.format("%02d",bytes[i]));
        }
        return stringBuffer.toString();
    }
	public static String floatToHexstr(float value){
        byte[] buffer = new byte[4];
        new IeShortFloat(value).encode(buffer,0);
        return toHexString(buffer);
    }

    public static String encode(float value){
        int tempVal = Float.floatToIntBits(value);
        byte[] buffer = new byte[4];
        buffer[0] = (byte) tempVal;
        buffer[1] = (byte) (tempVal >> 8);
        buffer[2] = (byte) (tempVal >> 16);
        buffer[3] = (byte) (tempVal >> 24);
        int[] s = new int[4];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buffer.length; i++){
            s[i] = (buffer[i] & 0xff);
            if (Integer.toHexString(s[i]).length() == 1){
                sb.append("0" + Integer.toHexString(s[i]) + " ");
            }else {
                sb.append(Integer.toHexString(s[i]) + " ");
            }
        }
        return sb.toString().toUpperCase();
    }

    public static String encodeInfomationAddress(int address) {
        byte[] buffer = new byte[3];
        buffer[0] = (byte) address;
        buffer[1] = (byte) (address >> 8);
        buffer[2] = (byte) (address >> 16);

        int[] s = new int[4];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buffer.length; i++){
            s[i] = (buffer[i] & 0xff);
            if (Integer.toHexString(s[i]).length() == 1){
                sb.append("0" + Integer.toHexString(s[i]) + " ");
            }else {
                sb.append(Integer.toHexString(s[i]) + " ");
            }
        }
        return sb.toString().toUpperCase();
    }

    // private static int startSendNum  =  1;

    public static void main(String[] args) {
       /* Runnable runnable = new Runnable() {
            public void run() {
                try {
                    byte[] recNum = new byte[2];
                    recNum[0] = (byte) (startSendNum << 1);
                    recNum[1] = (byte) (startSendNum >> 7);
                    String recStr = Utils.toHexString(recNum);
                    System.out.println("******************"+ recStr);
                    startSendNum++;
                    System.out.println("******************"+ startSendNum);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.SECONDS);*/

        Calendar calendar=new GregorianCalendar();
        int year = Integer.parseInt(String.valueOf(calendar.get(Calendar.YEAR)).substring(2));
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int milliSecond = calendar.get(Calendar.MILLISECOND);
        int handleS = second * 1000 + milliSecond;

        String yearStr = Integer.toHexString(year);
        yearStr = yearStr.length() > 1?yearStr:"0".concat(yearStr);
        String monthStr = Integer.toHexString(month);
        monthStr = monthStr.length() > 1?monthStr:"0".concat(monthStr);
        String dayStr = Integer.toHexString(day);
        dayStr = dayStr.length() > 1?dayStr:"0".concat(dayStr);
        String hourStr = Integer.toHexString(hour);
        hourStr = hourStr.length() > 1?hourStr:"0".concat(hourStr);
        String minuteStr = Integer.toHexString(minute);
        minuteStr = minuteStr.length() > 1?minuteStr:"0".concat(minuteStr);
        String handleSStr = Integer.toHexString(handleS);
        if(handleSStr.length() == 1){
            handleSStr = "000" + handleSStr;
        }else if(handleSStr.length() == 2){
            handleSStr = "00" + handleSStr;
        }else if(handleSStr.length() == 3){
            handleSStr = "0" + handleSStr;
        }
        handleSStr = handleSStr.substring(2) + handleSStr.substring(0,2);
        System.out.println(yearStr);
        System.out.println(monthStr);
        System.out.println(dayStr);
        System.out.println(hourStr);
        System.out.println(minuteStr);
        System.out.println(handleSStr);
    }
}
