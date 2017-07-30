package zero.management;

import java.util.UUID;

/**
 *任务描述： Bean，是一个学生的个人信息
 *创建时间： 2017/7/30 11:48
 */

public class Person {
    private int num;
    private String password;
    private String name;
    private String sex;
    private String from;
    private int score;
    private String subject;
    private UUID uuid;

    public Person(UUID id){
        this.uuid=id;
    }
    public Person(){
       this(UUID.randomUUID());
        password="123456";
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UUID getUuid() {
        return uuid;
    }

}
