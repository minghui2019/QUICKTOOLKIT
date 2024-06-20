package com.eplugger.test;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

public class ExpressionTransformer {
    public static void main(String[] args) {
        String input;
        // p1:{};p2:{yyyy};p3:{parentNum:4};;p4:{}
        //p1:{};p2:{yyyy};p3:{infix:target.parentCode};p4:{sn:p1+p2+p3+p5,4};p5:{}
        System.out.println(input = "p1:{};p2:{parentNum:4};;p3:{}");
        System.out.println(transformExpression(input));
        System.out.println(input = "p1:{};p2:{yyyy};p3:{parentNum:4};;p4:{}");
        System.out.println(transformExpression(input));
    }

    public static String transformExpression(String input) {
        // 拆分原始表达式
        String[] exp = input.split(";");
        StringBuilder result = new StringBuilder();

        int parentNum = 0;
        int parentNumIndex = -1;

        for (int i = 0; i < exp.length; i++) {
            String subExp = exp[i];
            if (Strings.isNullOrEmpty(subExp)) {
                continue;
            }
            if (subExp.contains("parentNum")) {
                String key = subExp.split(":")[0];
                String parentNumObj = StringUtils.substringBetween(subExp, "{", "}");
                // 提取parentNum的值
                parentNum = Integer.parseInt(parentNumObj.replaceAll("[^0-9]", ""));
                // 修改当前部分的内容
//                subExp = subExp.replace("parentNum:" + parentNum, "infix:target.parentCode");
                subExp = key + ":{infix:" + "target.parentCode" + "}";
                parentNumIndex = i;
                System.out.println(subExp);
            }
            if (i == exp.length - 1) {
                continue;
            }
            result.append(subExp);
            if (i < exp.length - 1) {
                result.append(";");
            }
        }

        // 修正多余的分号问题
        String modifiedResult = result.toString().replaceAll(";;", ";");

        if (parentNumIndex != -1) {
            // 计算新表达式的位置
            int newIndex = parentNumIndex + 1;
            StringBuilder newExpression = new StringBuilder("p" + (newIndex + 1) + ":{sn:");
            for (int i = 0; i <= parentNumIndex; i++) {
                newExpression.append("p" + (i + 1));
                if (i < parentNumIndex) {
                    newExpression.append("+");
                }
            }
            newExpression.append("+p" + (newIndex + 2) + "," + parentNum + "};p" + (newIndex + 2) + ":{}");

            // 检查并删除不必要的空表达式
            modifiedResult = modifiedResult.replaceAll("p" + newIndex + ":\\{\\};", "");

            modifiedResult += newExpression.toString();
        }

        return modifiedResult;
    }
}
