package archives.entity;

import java.io.Writer;

import com.google.common.base.CharMatcher;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.junit.Test;

public class ArchivesTest {
    @Test
    public void testName() {
        AJSJFZB ajsjfzb = new AJSJFZB();
        ajsjfzb.getJcxx().setXmlwybs("111111111111111111");
        ajsjfzb.getAjxx().getLy().setGdbmmc("科研处");
        ajsjfzb.getAjxx().getLy().setYjbbs("111111111111111111");

        ajsjfzb.getAjxx().getJnlb().getJnxx().getXstz().setWjzhlx("多件");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getXstz().setYz("汉语");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getXstz().setGb("正本");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getXstz().setJs("1");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getXstz().setYs("1");

        ajsjfzb.getAjxx().getJnlb().getJnxx().getDzsx().setGsxx("PDF");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getDzsx().setXxxtms("电子文件由学校科研系统产生，该系统由北京易普拉格科技股份有限公司开发");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getDzsx().setJsjwjm("附件名");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getDzsx().setJsjwjdx("计算");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getDzsx().setWdcjcx("Adobe Acrobat Reader DC");

        ajsjfzb.getAjxx().getJnlb().getJnxx().getJgrystysj().setJgrymc("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getJgrystysj().setJgrybsf("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getJgrystysj().setJgryssjg("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getJgrystysj().setJgrylx("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getJgrystysj().setJgrydm("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getJgrystysj().setJgrylxfs("");

        ajsjfzb.getAjxx().getJnlb().getJnxx().getYwstysj().setYwxw("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getYwstysj().setYwzt("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getYwstysj().setYwstbsf("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getYwstysj().setXwms("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getYwstysj().setXwsj("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getYwstysj().setXwyj("");

        ajsjfzb.getAjxx().getJnlb().getJnxx().getStgxysj().setGxms("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getStgxysj().setGxlx("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getStgxysj().setGx("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getStgxysj().setMbbs("");
        ajsjfzb.getAjxx().getJnlb().getJnxx().getStgxysj().setYbsf("");

        ajsjfzb.getAjxx().getJnlb().getJnxx().getFjList().add(new FJ("/attach/DAT 92—2022电子档案单套管理一般要求.pdf"));
        ajsjfzb.getAjxx().getJnlb().getJnxx().getFjList().add(new FJ("/attach/DAT 93—2022电子档案移交接收操作规程.pdf"));

        XStream xStream = new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out, new char[] { }) {
                    @Override
                    protected String getNewLine() {
                        return "";
                    }
                };
            }
        });
//        XStream xStream = new XStream(new XppDriver() {
//            @Override
//            public HierarchicalStreamWriter createWriter(Writer out) {
//                return new PrettyPrintWriter(out, new char[] { ' ', ' ', ' ', ' ' }) {
//                    @Override
//                    protected String getNewLine() {
//                        return "";
//                    }
//                };
//            }
//        });
//        XStream xStream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));

        xStream.autodetectAnnotations(true);
        String s = xStream.toXML(ajsjfzb);
        String s1 = CharMatcher.is('<').replaceFrom(CharMatcher.is('>').replaceFrom(s, "&lt;"), "&gt;");
        System.out.println(s1);
    }
}
