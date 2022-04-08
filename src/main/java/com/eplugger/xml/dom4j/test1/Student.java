package com.eplugger.xml.dom4j.test1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    //学生班级
    private String stuclass;
    //学生姓名
    private String stuname;
    //学生分数
    private String stuscore;
}