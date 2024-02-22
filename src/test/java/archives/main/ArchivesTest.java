package archives.main;

import java.io.IOException;
import java.io.Writer;

import archives.entity.DZWJFZB;
import archives.entity.ITransferString;
import archives.entity.JGRYST;
import archives.entity.NRMS;
import archives.entity.STGX;
import archives.entity.WD;
import archives.entity.WJSTAJ;
import archives.entity.XSTZ;
import archives.entity.YWST;
import cn.hutool.core.date.DateUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
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

//        WJSTKAJ wjstkaj = new WJSTKAJ();


        //文件实体块可以包含多个文件实体，如果是科研项目，每个阶段的文档为一个文件实体，例如立项申请所有的材料都属于一个文件实体，一个文件实体里包含多个文档
//        WJST wjst = new WJST();
        WJSTAJ wjstaj = new WJSTAJ();
        zb.getFznr().getWjstajs().add(wjstaj);

        //内容描述有相关数据的均需填写，无对应数据的留空
        NRMS nrms = wjstaj.getNrms();
        nrms.addItem("案卷题名", "党的二十大精神之中华优秀传统文化融入高职院校工科课堂引领思政教育的理论研究");
        nrms.addItem("题名", "党的二十大精神之中华优秀传统文化融入高职院校工科课堂引领思政教育的理论研究");
        nrms.addItem("副题名", "党的二十大精神之中华优秀传统文化融入高职院校工科课堂引领思政教育的理论研究");
        nrms.addItem("关键词", null);
        nrms.addItem("文件编号", null);
        nrms.addItem("成文日期", DateUtil.today());
        nrms.addItem("保密期限", null);
        nrms.addItem("密级", null);

        nrms.addItem("负责人类型", "教师");
        nrms.addItem("负责人", "张占辉");
        nrms.addItem("项目级别", "市级");
        nrms.addItem("批准号", "穗教科〔2023〕1号");
        nrms.addItem("项目来源单位立项单位", "广州市教育局");

        XSTZ xstz = wjstaj.getXstz();
        xstz.setWjzhlx("组合文件"); //单件/组合文件
        xstz.setJs("2");
        xstz.setYs(2);
        xstz.setYz("汉语");
        xstz.setGb("正本");

        //存储位置可以均留空
        wjstaj.getCcwz();

        //权限管理可留空
        wjstaj.getQxgl();

        //文件数据下可以有多个文档
        WD wd = new WD();
        wd.setWdxh("1");
        wd.setWdlj("/附件/HNLGDX-00100-2020-Y-XZ1211-003-040.001.PDF");
        wd.getDzsx().setGsxx("PDF");
        wd.getDzsx().setJsjwjm("HNLGDX-00100-2020-Y-XZ1211-003-040.001.PDF");
        wd.getDzsx().setJsjwjdx("4852550 字节");
        wd.getDzsx().setWdcjcx("Adobe Acrobat Reader DC");

        //电子签名留空
        wd.getDzqm();
        wjstaj.getWds().add(wd);

        //--------------------------
        wd = new WD();
        wd.setWdxh("1");
        wd.setWdlj("/附件/HNLGDX-00100-2020-Y-XZ1211-003-040.002.PDF");
        wd.getDzsx().setGsxx("PDF");
        wd.getDzsx().setJsjwjm("HNLGDX-00100-2020-Y-XZ1211-003-040.002.PDF");
        wd.getDzsx().setJsjwjdx("4852550 字节");
        wd.getDzsx().setWdcjcx("Adobe Acrobat Reader DC");

        //电子签名留空
        wd.getDzqm();
        wjstaj.getWds().add(wd);

//        wjstkaj.getWjsts().add(wjstaj);

        //机构人员实体块主要是电子文件涉及到的相关人员信息，可以是多个
        JGRYST jgryst = new JGRYST();
        jgryst.setJgrymc("张占辉");
        jgryst.setJgrybsf("2020090101");
        jgryst.setJgryssjg("智慧汽车学院");
        jgryst.setJgrylx("教师");
        jgryst.setJgrydm("2020090101");
        jgryst.setLxfs("17728101097");

        wjstaj.getJgrysts().add(jgryst);
//        zb.getFznr().getJgrysts().add(jgryst);

        //业务实体是电子文件在历史记录中涉及到的业务操作，可以是多个
        YWST ywst = new YWST();
        ywst.setYwstbsf("125056784562624566");
        ywst.setYwzt("历史行为");
        ywst.setYwxw("科研项目立项");
        ywst.setXwsj(DateUtil.parse("2023-02-21").toString());
        ywst.setXwyj("项目业务流程规范");
        ywst.setXwms("张占辉" + "于" + "2023-02-21" + "对该项目进行立项");
        wjstaj.getYwsts().add(ywst);
//        zb.getFznr().getYwsts().add(ywst);

        //机构人员与业务之间的关系记录，可以是多条
        STGX stgx = new STGX();
        stgx.setGxms("张占辉" + "完成了科研项目立项");
        stgx.setGxlx("个人-业务");
        stgx.setGx("完成"); //完成/被完成
        stgx.setMbbsf(wjstaj.getYwsts().get(0).getYwstbsf());
        stgx.setYbsf(wjstaj.getJgrysts().get(0).getJgrybsf());
        wjstaj.getStgxs().add(stgx);

        if (zb instanceof ITransferString) {
            ((ITransferString) zb).nullToEmpty();
        }
        XStream xStream = createXStream();

        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + xStream.toXML(zb));
    }

    private static XStream createXStream() {
        XStream xStream = new XStream(new DomDriver("UTF-8", new NoNameCoder()) {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out);
            }
        });
        xStream.registerConverter(new NRMSConverter());
//        xStream.registerConverter(new FZNRConverter());
        xStream.autodetectAnnotations(true);
        return xStream;
    }
}
