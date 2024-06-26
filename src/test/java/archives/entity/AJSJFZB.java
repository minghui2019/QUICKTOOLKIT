package archives.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import lombok.Data;

@Data
@XStreamAliasType("案卷数据封装包")
public class AJSJFZB {
    @XStreamAlias("基础信息")
    private JCXX jcxx;
    @XStreamAlias("案卷信息")
    private AJXX ajxx;

    public AJSJFZB() {
        this.jcxx = new JCXX();
        this.ajxx = new AJXX();
    }
}

@Data
class JCXX {
    @XStreamAlias("类目名称")
    private String lmmc;
    @XStreamAlias("聚合层次")
    private String jhcc;
    @XStreamAlias("XML唯一标识")
    private String xmlwybs;

    public JCXX() {
        this.lmmc = "";
        this.jhcc = "";
        this.xmlwybs = "";
    }
}

@Data
class AJXX {
    @XStreamAlias("来源")
    private LY ly;
    @XStreamAlias("案卷信息")
    private AJXX1 ajxx;
    @XStreamAlias("卷内列表")
    private JNLB jnlb;

    public AJXX() {
        this.ly = new LY();
        this.ajxx = new AJXX1();
        this.jnlb = new JNLB();
    }
}

@Data
class AJXX1 {
    @XStreamAlias("机构或问题")
    private String jghwt;
    @XStreamAlias("分类号")
    private String flh;
    @XStreamAlias("全宗号")
    private String qzh;
    @XStreamAlias("年度")
    private String nd;
    @XStreamAlias("案卷号")
    private String ajh;
    @XStreamAlias("类别号")
    private String lbh;
    @XStreamAlias("归档部门代号")
    private String gdbmdh;
    @XStreamAlias("保管期限")
    private String bgqx;

    public AJXX1() {
        this.jghwt = "";
        this.flh = "";
        this.qzh = "";
        this.nd = "";
        this.ajh = "";
        this.lbh = "";
        this.gdbmdh = "";
        this.bgqx = "";
    }
}

@Data
class JNLB {
    @XStreamAlias("卷内信息")
    private JNXX jnxx;

    public JNLB() {
        this.jnxx = new JNXX();
    }
}

@Data
class JNXX {
    @XStreamAlias("卷内信息")
    private JNXX1 jnxx;
    @XStreamAlias("形式特征")
    private XSTZ xstz;
    @XStreamAlias("电子属性")
    private DZSX dzsx;
    @XStreamAlias("电子签名")
    private DZQM dzqm;
    @XStreamAlias("存储位置")
    private CCWZ ccwz;
    @XStreamAlias("权限管理")
    private QXGL qxgl;
    @XStreamAlias("机构人员实体元数据")
    private JGRYSTYSJ jgrystysj;
    @XStreamAlias("业务实体元数据")
    private YWSTYSJ ywstysj;
    @XStreamAlias("实体关系元数据")
    private STGXYSJ stgxysj;
    @XStreamAlias("附件列表")
    private List<FJ> fjList;

    public JNXX() {
        this.jnxx = new JNXX1();
        this.xstz = new XSTZ();
        this.dzsx = new DZSX();
        this.dzqm = new DZQM();
        this.ccwz = new CCWZ();
        this.qxgl = new QXGL();
        this.jgrystysj = new JGRYSTYSJ();
        this.ywstysj = new YWSTYSJ();
        this.stgxysj = new STGXYSJ();
        this.fjList = Lists.newArrayList();
    }
}

@Data
class JGRYSTYSJ {
    @XStreamAlias("机构人员名称")
    private String jgrymc;
    @XStreamAlias("机构人员标识符")
    private String jgrybsf;
    @XStreamAlias("机构人员所属机构")
    private String jgryssjg;
    @XStreamAlias("机构人员类型")
    private String jgrylx;
    @XStreamAlias("机构人员代码")
    private String jgrydm;
    @XStreamAlias("机构人员联系方式")
    private String jgrylxfs;

    public JGRYSTYSJ() {
        this.jgrymc = "";
        this.jgrybsf = "";
        this.jgryssjg = "";
        this.jgrylx = "";
        this.jgrydm = "";
        this.jgrylxfs = "";
    }
}

@Data
class YWSTYSJ {
    @XStreamAlias("业务行为")
    private String ywxw;
    @XStreamAlias("业务状态")
    private String ywzt;
    @XStreamAlias("业务实体标识符")
    private String ywstbsf;
    @XStreamAlias("行为描述")
    private String xwms;
    @XStreamAlias("行为时间")
    private String xwsj;
    @XStreamAlias("行为依据")
    private String xwyj;

    public YWSTYSJ() {
        this.ywxw = "";
        this.ywzt = "";
        this.ywstbsf = "";
        this.xwms = "";
        this.xwsj = "";
        this.xwyj = "";
    }
}

@Data
class STGXYSJ {
    @XStreamAlias("关系描述")
    private String gxms;
    @XStreamAlias("关系类型")
    private String gxlx;
    @XStreamAlias("关系")
    private String gx;
    @XStreamAlias("目标标识")
    private String mbbs;
    @XStreamAlias("源标识符")
    private String ybsf;

    public STGXYSJ() {
        this.gxms = "";
        this.gxlx = "";
        this.gx = "";
        this.mbbs = "";
        this.ybsf = "";
    }
}

@Data
@XStreamAliasType("附件")
class FJ {
    @XStreamAlias("url")
    private String url;

    public FJ(String url) {
        this.url = url;
    }
}