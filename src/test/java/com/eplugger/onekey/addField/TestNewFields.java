package com.eplugger.onekey.addField;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import top.tobak.common.io.FileUtils;
import com.eplugger.onekey.addModule.AddModuleFun;
import com.eplugger.onekey.entity.Field;
import com.eplugger.onekey.entity.Fields;
import com.eplugger.onekey.entity.Module;
import com.eplugger.onekey.entity.ModuleInfo;
import com.eplugger.onekey.utils.javaFile.ProduceJavaFactory;
import com.eplugger.onekey.utils.jspFile.ProduceJspFiles;
import com.eplugger.onekey.utils.sqlFile.ProduceSqlFactory;
import com.eplugger.onekey.utils.xmlFile.ProduceXmlFiles;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

public class TestNewFields {
    @Test
    public void testName() {
        Module module = new Module();
        ModuleInfo moduleInfo = new ModuleInfo();
        module.setMainModule(moduleInfo);
        moduleInfo.setPackageName("com.eplugger.business.pub");
        HashMap<String, String> map = Maps.newHashMap();
        map.put("entity", "EntityImpl");
        map.put("bo", "BusinessBO");
        map.put("action", "BusinessAction");
        moduleInfo.setSuperClassMap(map);
        List<Field> fieldList = Lists.newArrayList();
        Fields fields = new Fields();
        fields.setFieldList(fieldList);
        moduleInfo.setFields(fields);
        fieldList.add(new Field("合同名称", "htmc", "String"));
        fieldList.add(new Field("合同编号", "htbh", "String"));
        fieldList.add(new Field("承办部门", "cbbm", "String"));
        fieldList.add(new Field("经办人", "jbr", "String"));
        fieldList.add(new Field("经办人电话", "jbrdh", "String"));
        fieldList.add(new Field("项目负责人", "xmfzr", "String"));
        fieldList.add(new Field("项目组成员", "xmzcy", "String"));
        fieldList.add(new Field("合同类型", "htlx", "String"));
        fieldList.add(new Field("采购文件编号", "cgwjbh", "String"));
        fieldList.add(new Field("甲方", "jf", "String"));
        fieldList.add(new Field("乙方", "yf", "String"));
        fieldList.add(new Field("丙方", "bf", "String"));
        fieldList.add(new Field("合同金额（万元）", "htje", "Double"));
        fieldList.add(new Field("其中外拨经费（万元）", "wbje", "Double"));
        fieldList.add(new Field("拟签订时间", "qdsj", "Date"));
        fieldList.add(new Field("合同开始有效期", "kssj", "Date"));
        fieldList.add(new Field("合同结束有效期", "jssj", "Date"));
        fieldList.add(new Field("合同起草方式", "htqcfs", "String"));
        fieldList.add(new Field("合同类别", "htlb", "String"));
        fieldList.add(new Field("备注说明", "bz", "String"));
        fieldList.add(new Field("项目负责人及承诺", "xmfzrcn", "String"));
        fieldList.add(new Field("合同当事人权利义务对等情况", "qlyw", "String"));
        fieldList.add(new Field("合同的付款方式", "fkfs", "String"));
        fieldList.add(new Field("合同违约条款和违约责任", "tkzr", "String"));
        fieldList.add(new Field("合同生效、变更、解除和终止的条件", "bgtj", "String"));
        fieldList.add(new Field("合同争议解决方式（每项内容设置勾选按钮，不勾选不能提交审批）", "jjfs", "String"));
        fieldList.add(new Field("成果归属情况", "cggs", "String"));
        fieldList.add(new Field("资产归属", "zcgs", "String"));
        fieldList.add(new Field("是否需要在研究成果上标注广州番禺职业技术学院及相关院、部、处室研制或监制等字样", "cgbz", "String"));
        fieldList.add(new Field("专利名称", "zlmc", "String"));
        fieldList.add(new Field("专利申请号或授权号", "zlh", "String"));
        fieldList.add(new Field("专利发明人（所有成员）", "zlfmr", "String"));
        fieldList.add(new Field("是否使用本部门或学校其他部门资产设备", "sfsy", "String"));
        fieldList.add(new Field("总费用为", "fyje", "String"));
        fieldList.add(new Field("是否已取得授权", "sfsq", "String"));
        fieldList.add(new Field("承办部门负责人意见", "cbbmyj", "String"));
        fieldList.add(new Field("归口部门经办人意见", "jbryj", "String"));
        fieldList.add(new Field("归口部门领导意见", "gkldyj", "String"));
        fieldList.add(new Field("校长审核意见", "xzshyj", "String"));
        fieldList.add(new Field("科技处经办人签署合同说明，并将信息反馈到经办人", "kjcqs", "String"));
        fieldList.add(new Field("推送时间", "tssj", "Date"));
        moduleInfo.setModuleName("PubContract");
        moduleInfo.setModuleZHName("横向合同审批表");
        moduleInfo.setTableName("BIZ_PUB_CONTRACT");
        moduleInfo.setBeanId("pubContract");
        ProduceJavaFactory.getInstance().produceJavaFiles(module, "honor");

        ProduceSqlFactory.getInstance().produceCreateTableSqlFiles(module.getMainModule(), module.getAuthorModule(), false);

        ProduceXmlFiles.produceXmlFile(module.getMainModule().getPackageName(), module.getMainModule(), module.getAuthorModule(), false, "honor");

        ProduceJspFiles.produceJspFiles(module.getMainModule(), "honor");
        try {
            Desktop.getDesktop().open(new File(FileUtils.getUserHomeDirectory() + "AddModule"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
