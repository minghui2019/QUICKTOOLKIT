package archives.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import cn.hutool.core.date.DateUtil;
import com.google.common.base.CharMatcher;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.junit.Test;

public class ArchivesTest {
    @Test
    public void testName() throws IOException {
        DZWJFZB zb = new DZWJFZB();

        zb.setFzblx("原始型");
        zb.setFzblxms("本封装包包含电子文件数据及其元数据，原始封装，未经修改");
        zb.setFzbcjsj(DateUtil.now());
        zb.setFzbcjdw("广州番禺职业技术学院科技处");

        zb.getFznr().setWjstbsf("KYZX202303280001");
        zb.getFznr().setJhcc("案卷"); //案卷/文件(类似项目之类的有多个阶段的档案选择案卷,类如通知类的选择文件)

        zb.getFznr().getLy().setHqfs("从业务系统自动获取");
        zb.getFznr().getLy().setLyms("广州番禺职业技术学院科技处");
        zb.getFznr().getLy().setYjbbs("");
        zb.getFznr().getLy().setGdbmmc("科研处");
        zb.getFznr().getLy().setQzmc("广州番禺职业技术学院");

        //档号部分由档案室来生成
        zb.getFznr().getDh();

        //文件实体块可以包含多个文件实体，如果是科研项目，每个阶段的文档为一个文件实体，例如立项申请所有的材料都属于一个文件实体，一个文件实体里包含多个文档
        WJST wjst = new WJST();
        //内容描述有相关数据的均需填写，无对应数据的留空
        NRMS nrms = wjst.getNrms();
        nrms.setAjtm("党的二十大精神之中华优秀传统文化融入高职院校工科课堂引领思政教育的理论研究"); //如果是项目归档，项目名称作为案卷题名
        nrms.setTm("党的二十大精神之中华优秀传统文化融入高职院校工科课堂引领思政教育的理论研究"); //聚合层次如果是案卷，这里是卷内题名，如果是文件，这里就是文件题名
        nrms.setXmmc("党的二十大精神之中华优秀传统文化融入高职院校工科课堂引领思政教育的理论研究");
        nrms.setXmbh("202235553");
        nrms.setFzrlx("教师");
        nrms.setFzr("张占辉");
        nrms.setXmjb("市级");
        nrms.setPzh("穗教科〔2023〕1号");
        nrms.setXmlydw("广州市教育局");
        nrms.setLxrq(DateUtil.parse("2023-02-21").toSqlDate());
        nrms.setJxrq(DateUtil.parse("2025-02-21").toSqlDate());
        nrms.setCgxs("");
        nrms.setHtjf(0d);
        nrms.setPtjf(0d);
        nrms.setCyxm("张占辉");
        nrms.setYjlb("基础研究");
        nrms.setHzxs("");

        XSTZ xstz = wjst.getXstz();
        xstz.setWjzhlx("组合文件"); //单件/组合文件
        xstz.setJs(2);
        xstz.setYs(2);
        xstz.setYz("汉语");
        xstz.setGb("正本");

        //存储位置可以均留空
        wjst.getCcwz();

        //权限管理可留空
        wjst.getQxgl();

        //文件数据下可以有多个文档
        WD wd = new WD();
        wd.setWdxh(1);
        wd.setWdlj("/附件/HNLGDX-00100-2020-Y-XZ1211-003-040.001.PDF");
        wd.getDzsx().setGsxx("PDF");
        wd.getDzsx().setJsjwjm("HNLGDX-00100-2020-Y-XZ1211-003-040.001.PDF");
        wd.getDzsx().setJsjwjdx("4852550 字节");
        wd.getDzsx().setWdcjcx("Adobe Acrobat Reader DC");

        //电子签名留空
        wd.getDzqm();
        wjst.getWds().add(wd);

        //--------------------------
        wd = new WD();
        wd.setWdxh(2);
        wd.setWdlj("/附件/HNLGDX-00100-2020-Y-XZ1211-003-040.002.PDF");
        wd.getDzsx().setGsxx("PDF");
        wd.getDzsx().setJsjwjm("HNLGDX-00100-2020-Y-XZ1211-003-040.002.PDF");
        wd.getDzsx().setJsjwjdx("4852550 字节");
        wd.getDzsx().setWdcjcx("Adobe Acrobat Reader DC");

        //电子签名留空
        wd.getDzqm();
        wjst.getWds().add(wd);

        zb.getFznr().getWjsts().add(wjst);

        //机构人员实体块主要是电子文件涉及到的相关人员信息，可以是多个
        JGRYST jgryst = new JGRYST();
        jgryst.setJgrymc("张占辉");
        jgryst.setJgrybsf("2020090101");
        jgryst.setJgryssjg("智慧汽车学院");
        jgryst.setJgrylx("教师");
        jgryst.setJgrydm("2020090101");
        jgryst.setLxfs("17728101097");
        zb.getFznr().getJgrysts().add(jgryst);

        //业务实体是电子文件在历史记录中涉及到的业务操作，可以是多个
        YWST ywst = new YWST();
        ywst.setYwstbsf("125056784562624566");
        ywst.setYwzt("历史行为");
        ywst.setYwxw("科研项目立项");
        ywst.setXwsj(DateUtil.parse("2023-02-21").toTimestamp());
        ywst.setXwyj("项目业务流程规范");
        ywst.setXwms("张占辉" + "于" + "2023-02-21" + "对该项目进行立项");
        zb.getFznr().getYwsts().add(ywst);

        //机构人员与业务之间的关系记录，可以是多条
        STGX stgx = new STGX();
        stgx.setGxms("张占辉" + "完成了科研项目立项");
        stgx.setGxlx("个人-业务");
        stgx.setGx("完成"); //完成/被完成
        stgx.setMbbsf(zb.getFznr().getYwsts().get(0).getYwstbsf());
        stgx.setYbsf(zb.getFznr().getJgrysts().get(0).getJgrybsf());
        zb.getFznr().getStgxs().add(stgx);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n\n");

        XStream xStream = new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out, new char[] { }) {
//                    @Override
//                    protected String getNewLine() {
//                        return "";
//                    }
                };
            }
        });

        xStream.autodetectAnnotations(true);
        xStream.toXML(zb, writer);
        System.out.println(outputStream.toString("UTF-8"));
        String s = xStream.toXML(zb);
        System.out.println(s);
        String s1 = CharMatcher.is('<').replaceFrom(CharMatcher.is('>').replaceFrom(s, "&lt;"), "&gt;");
        System.out.println(s1);
    }
}
