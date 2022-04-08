package com.eplugger.xml.dom4j.test1;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RspSchool {
    //学校编号
    private String number;
    //学校名称
    private String name;
    //学校省份
    private String province;
    //学校地址
    private String address;
    //多个学生
    private List<Student> students; //模拟测试数据，返回多个学生
}