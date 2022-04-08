package com.eplugger.xml.dom4j.test1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqSchool {
    //学校编号
    private String number;
    //学校名称
    private String name;
    //学校省份
    private String province;
    //学校地址
    private String address;
    //学生班级
    private String stuclass;
    //学生姓名
    private String stuname;
    //学生分数
    private String stuscore;
    //省略set和get方法  
}