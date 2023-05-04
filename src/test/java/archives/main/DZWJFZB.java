package archives.main;

import java.sql.Timestamp;
import java.util.List;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@XStreamAliasType("电子文件封装包")
public class DZWJFZB {
    @XStreamAsAttribute
    final String xmlns = "http://www.saac.gov.cn/standards/ERM/encapsulation";
    @XStreamAlias("xmlns:xsi")
    @XStreamAsAttribute
    final String xmlnsXsi = " http://www.w3.org/2001/XMLSchema-instance";
    @XStreamAlias("封装包类型")
    private String fzblx;
    @XStreamAlias("封装包类型描述")
    private String fzblxms;
    @XStreamAlias("封装包创建时间")
    private String fzbcjsj;
    @XStreamAlias("封装包创建单位")
    private String fzbcjdw;
    @XStreamAlias("封装内容")
    private FZNR fznr;

    public DZWJFZB() {
        this.fznr = new FZNR();
    }
}

@Setter
@Getter
class FZNR {
    @XStreamAlias("文件实体标识符")
    private String wjstbsf;
    @XStreamAlias("聚合层次")
    private String jhcc;
    @XStreamAlias("来源")
    private LY ly;
    @XStreamAlias("档号")
    private DH dh;
    @XStreamAlias("文件实体块")
    private List<WJST> wjsts;
    @XStreamAlias("机构人员实体块")
    private List<JGRYST> jgrysts;
    @XStreamAlias("业务实体块")
    private List<YWST> ywsts;
    @XStreamAlias("实体关系块")
    private List<STGX> stgxs;

    public FZNR() {
        this.ly = new LY();
        this.dh = new DH();
        this.wjsts = Lists.newArrayList();
        this.jgrysts = Lists.newArrayList();
        this.ywsts = Lists.newArrayList();
        this.stgxs = Lists.newArrayList();
    }
}

@Setter
@Getter
class LY {
    @XStreamAlias("获取方式")
    private String hqfs;
    @XStreamAlias("来源描述")
    private String lyms;
    @XStreamAlias("移交包标识")
    private String yjbbs;
    @XStreamAlias("归档部门名称")
    private String gdbmmc;
    @XStreamAlias("全宗名称")
    private String qzmc;

    public LY() {
        this.qzmc = "";
        this.hqfs = "";
        this.lyms = "";
        this.gdbmmc = "";
        this.yjbbs = "";
    }
}

@Setter
@Getter
class DH {
    @XStreamAlias("归档部门代号")
    private String gdbmdh;
    @XStreamAlias("全宗号")
    private String qzh;
    @XStreamAlias("年度")
    private String nd;
    @XStreamAlias("保管期限")
    private String bgqx;
    @XStreamAlias("机构或问题")
    private String jghwt;
    @XStreamAlias("类别号")
    private String lbh;
    @XStreamAlias("分类号")
    private String flh;
    @XStreamAlias("案卷号")
    private String ajh;
    @XStreamAlias("件号")
    private String jh;

    public DH() {
        this.jghwt = "";
        this.flh = "";
        this.qzh = "";
        this.nd = "";
        this.ajh = "";
        this.lbh = "";
        this.gdbmdh = "";
        this.bgqx = "";
        this.jh = "";
    }
}

@Setter
@Getter
@XStreamAliasType("文件实体")
class WJST {
    @XStreamAlias("内容描述")
    private NRMS nrms;
    @XStreamAlias("形式特征")
    private XSTZ xstz;
    @XStreamAlias("存储位置")
    private CCWZ ccwz;
    @XStreamAlias("权限管理")
    private QXGL qxgl;
    @XStreamAlias("文件数据")
    private List<WD> wds;

    public WJST() {
        this.nrms = new NRMS();
        this.wds = Lists.newArrayList();
        this.xstz = new XSTZ();
        this.ccwz = new CCWZ();
        this.qxgl = new QXGL();
    }
}

@Setter
@Getter
class XSTZ {
    @XStreamAlias("文件组合类型")
    private String wjzhlx;
    @XStreamAlias("件数")
    private Integer js;
    @XStreamAlias("页数")
    private Integer ys;
    @XStreamAlias("语种")
    private String yz;
    @XStreamAlias("稿本")
    private String gb;

    public XSTZ() {
        this.wjzhlx = "";
        this.yz = "";
        this.gb = "";
        this.js = 1;
        this.ys = 1;
    }
}

@Setter
@Getter
class DZSX {
    @XStreamAlias("格式信息")
    private String gsxx;
    @XStreamAlias("计算机文件名")
    private String jsjwjm;
    @XStreamAlias("计算机文件大小")
    private String jsjwjdx;
    @XStreamAlias("文档创建程序")
    private String wdcjcx;

    public DZSX() {
        this.gsxx = "";
        this.jsjwjm = "";
        this.jsjwjdx = "";
        this.wdcjcx = "";
    }
}

@Setter
@Getter
class DZQM {
    @XStreamAlias("签名规则")
    private String qmgz;
    @XStreamAlias("签名时间")
    private String qmsj;
    @XStreamAlias("签名人")
    private String qmr;
    @XStreamAlias("签名结果")
    private String qmjg;
    @XStreamAlias("证书")
    private String zs;
    @XStreamAlias("证书引证")
    private String zsyz;
    @XStreamAlias("签名算法标识")
    private String qmsfbs;

    public DZQM() {
        this.qmr = "";
        this.zs = "";
        this.qmsfbs = "";
        this.qmsj = "";
        this.qmjg = "";
        this.zsyz = "";
        this.qmgz = "";
    }
}

@Setter
@Getter
class CCWZ {
    @XStreamAlias("当前位置")
    private String dqwz;
    @XStreamAlias("脱机载体编号")
    private String tjztbh;
    @XStreamAlias("脱机载体存址")
    private String tjztcz;
    @XStreamAlias("缩微号")
    private String swh;

    public CCWZ() {
        this.tjztcz = "";
        this.dqwz = "";
        this.tjztbh = "";
        this.swh = "";
    }
}

@Setter
@Getter
class QXGL {
    @XStreamAlias("知识产权说明")
    private String zscqsm;
    @XStreamAlias("授权对象")
    private String sqdx;
    @XStreamAlias("授权行为")
    private String sqxw;
    @XStreamAlias("控制标识")
    private String kzbs;

    public QXGL() {
        this.kzbs = "";
        this.zscqsm = "";
        this.sqxw = "";
        this.sqdx = "";
    }
}

@Setter
@Getter
@XStreamAliasType("文档")
class WD {
    @XStreamAlias("文档序号")
    private Integer wdxh;
    @XStreamAlias("文档链接")
    private String wdlj;
    @XStreamAlias("电子属性")
    private DZSX dzsx;
    @XStreamAlias("电子签名")
    private DZQM dzqm;

    public WD() {
        this.dzsx = new DZSX();
        this.dzqm = new DZQM();
    }
}

@Setter
@Getter
@XStreamAliasType("机构人员实体")
class JGRYST {
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
    @XStreamAlias("联系方式")
    private String lxfs;

    public JGRYST() {
        this.jgrymc = "";
        this.jgrybsf = "";
        this.jgryssjg = "";
        this.jgrylx = "";
        this.jgrydm = "";
        this.lxfs = "";
    }
}

@Setter
@Getter
@XStreamAliasType("业务实体")
class YWST {
    @XStreamAlias("业务行为")
    private String ywxw;
    @XStreamAlias("业务状态")
    private String ywzt;
    @XStreamAlias("业务实体标识符")
    private String ywstbsf;
    @XStreamAlias("行为描述")
    private String xwms;
    @XStreamAlias("行为时间")
    private Timestamp xwsj;
    @XStreamAlias("行为依据")
    private String xwyj;

    public YWST() {
        this.ywxw = "";
        this.ywzt = "";
        this.ywstbsf = "";
        this.xwms = "";
        this.xwsj = null;
        this.xwyj = "";
    }
}

@Setter
@Getter
@XStreamAliasType("实体关系")
class STGX {
    @XStreamAlias("关系描述")
    private String gxms;
    @XStreamAlias("关系类型")
    private String gxlx;
    @XStreamAlias("关系")
    private String gx;
    @XStreamAlias("目标标识符")
    private String mbbsf;
    @XStreamAlias("源标识符")
    private String ybsf;

    public STGX() {
        this.gxms = "";
        this.gxlx = "";
        this.gx = "";
        this.mbbsf = "";
        this.ybsf = "";
    }
}
