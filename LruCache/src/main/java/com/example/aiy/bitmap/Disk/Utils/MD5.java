package com.example.aiy.bitmap.Disk.Utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>功能简述：将字符串进行MD5加密后形成的byte数组，之后再转换成十六进制的string返回
 * <p>Created by Aiy on 2017/8/13.
 */

public class MD5 {
    /**
     * 方法简述： 核心方法，实际是使用JDK封装的MessageDigest类使用。
     */
    public static String urlKeyToMD5(String key){
        //最终要返回的string
        String cacheKey;
        try{
            //不是用new的，而是用getInstance的方法
            final MessageDigest digest=MessageDigest.getInstance("MD5");
            //把digest的内容进行更新，这里传入是byte[]，所以把string转成bytes
            digest.update(key.getBytes());
            //digest()方法是可以获得通过MD5转换后的bytes，我们要用的是string所以还要把bytes转换成string.
            cacheKey=bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            //该异常是在特点环境中这个算法不可用（暂时理解成JDK版本太低的话没办法使用？）
            //这时候就把hashcode返回
            cacheKey=String.valueOf(key.hashCode());
        }
        return cacheKey;
    }
    /**
     * 方法简述： 这是byte[]转换成十六进制String
     */
    private static String bytesToHexString(byte[] bytes){
        //需要经常添加删除的时候就要用到sb 线程安全的是StringBuffer
        StringBuilder sb=new StringBuilder();
        //循环取出每个byte
        for (byte aByte : bytes) {
            //这里是比较复杂,因为Byte没有直接转成HexString的方法，所以我们采用的是Integer的方法，而byte是8bit,int是32bit.
            //0xff二进制就是前面24个0，然后8个1,然后与byte &运算，所以结果还是前面24个0，然后byte的值（与1 &运算结果当然是本身了）.
            //如果不与0xff进行运算 强转int的话是与32个1 &运算
            String hex=Integer.toHexString(aByte&0xff);
            //因为byte值是-128-127转换成16进制后只有两位，所以sb的每两位是一个char，hex值是个位数的话就补个0，不理解请看下面把hexString转成byte方法就知道了
            if (hex.length()==1){
                sb.append(0);
            }
            //添加到sb里面
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * 方法简述： 这个是把hexString转成byte[]
     */
    private static byte[] hexStringToBytes(String hexString){
        //文本不为空
        if (TextUtils.isEmpty(hexString)){
            return null;
        }
        //先转成大写,也就是把a-f转成大写而已，对值没有影响，这里是方便判断（等会需要调用charToByte这个方法）
        hexString=hexString.toUpperCase();
        //因为每两个是组成一个char的，所以实际长度就是1/2
        int length=hexString.length()/2;
        //把string转成char数组
        char[] chars=hexString.toCharArray();
        //这是最终需要的bytes，可以指定长度是length，减少没必要的浪费空间
        byte[] bytes=new byte[length];
        for (int i=0;i<length;i++){
            //pos=0,2,4,6偶数
            int pos=i*2;
            //因为我们是两个位置来存储的,所以对应的每个位置是4bit，比如FC，前位置为F，后为C
            //所以前面的位置应该是1111 0000 后面是0000 1100
            //那么F要变成1111 0000应该是左移4位 (<<4) 后者不用移动，之后再把他们进行或运算（|)就得出结果了.
            bytes[i]=(byte)(charToByte(chars[pos])<<4|charToByte(chars[pos+1]));
        }
        return bytes;

    }
    /**
     * 方法简述： 这里是把对应的十六进制字符转换成对应的数字值，因为我们上面存储的比如‘A’,那么强转成byte是ASCLL码了，也就是65，但是我们的A是10才对，所以这里用一段String保存着，然后从0-15，总共15个数字，之后通过返回 值对应的位置就能得到对应的值了.
     */
    private static byte charToByte(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
}
