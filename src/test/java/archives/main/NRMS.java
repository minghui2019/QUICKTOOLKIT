package archives.main;

import java.sql.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NRMS {
    @XStreamAlias("案卷题名")
    private String ajtm;
    @XStreamAlias("题名")
    private String tm;
    @XStreamAlias("副题名")
    private String ftm;
    @XStreamAlias("关键词")
    private String gjc;
    @XStreamAlias("文件编号")
    private String wjbh;
    @XStreamAlias("责任者")
    private String zrz;
    @XStreamAlias("日期")
    private String rq;
    @XStreamAlias("保密期限")
    private String bmqx;
    @XStreamAlias("密级")
    private String mj;
    @XStreamAlias("项目名称")
    private String xmmc;
    @XStreamAlias("项目编号")
    private String xmbh;
    @XStreamAlias("负责人类型")
    private String fzrlx;
    @XStreamAlias("负责人")
    private String fzr;
    @XStreamAlias("项目级别")
    private String xmjb;
    @XStreamAlias("批准号")
    private String pzh;
    @XStreamAlias("项目来源单位")
    private String xmlydw;
    @XStreamAlias("立项日期")
    private Date lxrq;
    @XStreamAlias("结项日期")
    private Date jxrq;
    @XStreamAlias("成果形式")
    private String cgxs;
    @XStreamAlias("合同经费")
    private Double htjf;
    @XStreamAlias("配套经费")
    private Double ptjf;
    @XStreamAlias("成员姓名")
    private String cyxm;
    @XStreamAlias("合同名称")
    private String htmc;
    @XStreamAlias("合同编号")
    private String htbh;
    @XStreamAlias("合同类型")
    private String htlx;
    @XStreamAlias("承接单位")
    private String cjdw;
    @XStreamAlias("合同金额")
    private String htje;
    @XStreamAlias("支付方式")
    private String zffs;
    @XStreamAlias("开始日期")
    private String ksrq;
    @XStreamAlias("终止日期")
    private String zzrq;
    @XStreamAlias("签订日期")
    private String qdrq;
    @XStreamAlias("甲方名称")
    private String jfmc;
    @XStreamAlias("研究类别")
    private String yjlb;
    @XStreamAlias("合作形式")
    private String hzxs;
    @XStreamAlias("论文类型")
    private String lwlx;
    @XStreamAlias("论文题目")
    private String lwtm;
    @XStreamAlias("第一作者类型")
    private String dyzzlx;
    @XStreamAlias("第一作者")
    private String dyzz;
    @XStreamAlias("发表时间")
    private String fbsj;
    @XStreamAlias("发表刊物")
    private String fbkw;
    @XStreamAlias("刊物级别")
    private String kwjb;
    @XStreamAlias("收录名称")
    private String slmc;
    @XStreamAlias("ISSN号")
    private String issnh;
    @XStreamAlias("ISBN号")
    private String isbnh;
    @XStreamAlias("CN号")
    private String cnh;
    @XStreamAlias("成果类型")
    private String cglx;
    @XStreamAlias("主办单位")
    private String zbdw;
    @XStreamAlias("展览或收藏级别")
    private String zlhszjb;
    @XStreamAlias("著作名称")
    private String zzmc;
    @XStreamAlias("出版单位")
    private String cbdw;
    @XStreamAlias("出版时间")
    private String cbsj;
    @XStreamAlias("著作类别")
    private String zzlb;
    @XStreamAlias("鉴定部门")
    private String jdbm;
    @XStreamAlias("鉴定日期")
    private String jdrq;
    @XStreamAlias("转化时间")
    private String zhsj;
    @XStreamAlias("实际收入")
    private String sjsr;
    @XStreamAlias("受让方类型")
    private String srflx;
    @XStreamAlias("成果登记号")
    private String cgdjh;
    @XStreamAlias("发证时间")
    private String fzsj;
    @XStreamAlias("发证单位")
    private String fzdw;
    @XStreamAlias("成果提现形式")
    private String cgtxxs;
    @XStreamAlias("成果水平")
    private String cgsp;
    @XStreamAlias("转让范围")
    private String zrfw;
    @XStreamAlias("标准名称")
    private String bzmc;
    @XStreamAlias("出版社")
    private String cbs;
    @XStreamAlias("专利名称")
    private String zlmc;
    @XStreamAlias("第一发明人类型")
    private String dyfmrlx;
    @XStreamAlias("第一发明人")
    private String dyfmr;
    @XStreamAlias("专利类型")
    private String zllx;
    @XStreamAlias("申请号")
    private String sqh;
    @XStreamAlias("公开号")
    private String gkh;
    @XStreamAlias("授权日期")
    private String sqrq;
    @XStreamAlias("学校署名")
    private String xxsm;
    @XStreamAlias("支付金额")
    private String zfje;
    @XStreamAlias("支付时间")
    private String zfsj;
    @XStreamAlias("著作权编号")
    private String zzqbh;
    @XStreamAlias("第一著作人类型")
    private String dyzzrlx;
    @XStreamAlias("第一著作人")
    private String dyzzr;
    @XStreamAlias("著作权名称")
    private String zzqmc;
    @XStreamAlias("所属单位")
    private String ssdw;
    @XStreamAlias("著作权类型")
    private String zzqlx;
    @XStreamAlias("出版日期")
    private String cbrq;
    @XStreamAlias("授权范围")
    private String sqfw;
    @XStreamAlias("证书号")
    private String zsh;
    @XStreamAlias("奖励名称")
    private String jlmc;
    @XStreamAlias("第一完成人类型")
    private String dywcrlx;
    @XStreamAlias("第一完成人")
    private String dywcr;
    @XStreamAlias("指导老师")
    private String zdls;
    @XStreamAlias("成果名称")
    private String cgmc;
    @XStreamAlias("发证机关")
    private String fzjg;
    @XStreamAlias("获奖日期")
    private String hjrq;
    @XStreamAlias("获奖级别")
    private String hjjb;
    @XStreamAlias("获奖等级")
    private String hjdj;
    @XStreamAlias("奖励类别")
    private String jllb;

    public NRMS() {
        this.ajtm = "";
        this.tm = "";
        this.ftm = "";
        this.gjc = "";
        this.wjbh = "";
        this.zrz = "";
        this.rq = "";
        this.bmqx = "";
        this.mj = "";
        this.xmmc = "";
        this.xmbh = "";
        this.fzrlx = "";
        this.fzr = "";
        this.xmjb = "";
        this.pzh = "";
        this.xmlydw = "";
        this.lxrq = null;
        this.jxrq = null;
        this.cgxs = "";
        this.htjf = 0d;
        this.ptjf = 0d;
        this.cyxm = "";
        this.htmc = "";
        this.htbh = "";
        this.htlx = "";
        this.cjdw = "";
        this.htje = "";
        this.zffs = "";
        this.ksrq = "";
        this.zzrq = "";
        this.qdrq = "";
        this.jfmc = "";
        this.yjlb = "";
        this.hzxs = "";
        this.lwlx = "";
        this.lwtm = "";
        this.dyzzlx = "";
        this.dyzz = "";
        this.fbsj = "";
        this.fbkw = "";
        this.kwjb = "";
        this.slmc = "";
        this.issnh = "";
        this.isbnh = "";
        this.cnh = "";
        this.cglx = "";
        this.zbdw = "";
        this.zlhszjb = "";
        this.zzmc = "";
        this.cbdw = "";
        this.cbsj = "";
        this.zzlb = "";
        this.jdbm = "";
        this.jdrq = "";
        this.zhsj = "";
        this.sjsr = "";
        this.srflx = "";
        this.cgdjh = "";
        this.fzsj = "";
        this.fzdw = "";
        this.cgtxxs = "";
        this.cgsp = "";
        this.zrfw = "";
        this.bzmc = "";
        this.cbs = "";
        this.zlmc = "";
        this.dyfmrlx = "";
        this.dyfmr = "";
        this.zllx = "";
        this.sqh = "";
        this.gkh = "";
        this.sqrq = "";
        this.xxsm = "";
        this.zfje = "";
        this.zfsj = "";
        this.zzqbh = "";
        this.dyzzrlx = "";
        this.dyzzr = "";
        this.zzqmc = "";
        this.ssdw = "";
        this.zzqlx = "";
        this.cbrq = "";
        this.sqfw = "";
        this.zsh = "";
        this.jlmc = "";
        this.dywcrlx = "";
        this.dywcr = "";
        this.zdls = "";
        this.cgmc = "";
        this.fzjg = "";
        this.hjrq = "";
        this.hjjb = "";
        this.hjdj = "";
        this.jllb = "";
    }
}
