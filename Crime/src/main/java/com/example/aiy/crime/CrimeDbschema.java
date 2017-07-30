package com.example.aiy.crime;

/**
 *任务描述： sqlite表中的成员定义常量
 *创建时间： 2017/7/30 19:39
 */

public class CrimeDbschema {//用来调用一个表里面的成员
    public static final class CrimeTable{
        public static final String NAME="Crime"; //表名

        public static final class Cols{ //成员变量
            public static final String UUID="uuid";//UUID
            public static final String TITLE="title";//标题
            public static final String DATE="date";//按钮—时间
            public static final String SOLVED="solved";//checkbox
            public static final String SUSPECT="suspect";
        }
    }
}
