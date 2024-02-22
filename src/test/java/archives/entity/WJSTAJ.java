package archives.entity;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XStreamAliasType("文件实体")
public class WJSTAJ implements ITransferString {
    @XStreamAlias("分类名称")
    private String flmc;
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
    @XStreamAlias("机构人员实体块")
    private List<JGRYST> jgrysts;
    @XStreamAlias("业务实体块")
    private List<YWST> ywsts;
    @XStreamAlias("实体关系块")
    private List<STGX> stgxs;
    @XStreamAlias("审批流程块")
    private List<SPLC> splcs;

    public WJSTAJ() {
        this.nrms = new NRMS();
        this.wds = Lists.newArrayList();
        this.xstz = new XSTZ();
        this.ccwz = new CCWZ();
        this.qxgl = new QXGL();
        this.jgrysts = Lists.newArrayList();
        this.ywsts = Lists.newArrayList();
        this.stgxs = Lists.newArrayList();
        this.splcs = Lists.newArrayList();
    }

    @Override
    public void emptyToNull() {
        flmc = Strings.emptyToNull(flmc);
        nrms.emptyToNull();
        for (WD e : wds) {
            e.emptyToNull();
        }
        xstz.emptyToNull();
        ccwz.emptyToNull();
        qxgl.emptyToNull();
        for (JGRYST e : jgrysts) {
            e.emptyToNull();
        }
        for (YWST e : ywsts) {
            e.emptyToNull();
        }
        for (STGX e : stgxs) {
            e.emptyToNull();
        }
        for (SPLC e : splcs) {
            e.emptyToNull();
        }
    }

    @Override
    public void nullToEmpty() {
        flmc = Strings.nullToEmpty(flmc);
        nrms.nullToEmpty();
        for (WD e : wds) {
            e.nullToEmpty();
        }
        xstz.nullToEmpty();
        ccwz.nullToEmpty();
        qxgl.nullToEmpty();
        for (JGRYST e : jgrysts) {
            e.nullToEmpty();
        }
        for (YWST e : ywsts) {
            e.nullToEmpty();
        }
        for (STGX e : stgxs) {
            e.nullToEmpty();
        }
        for (SPLC e : splcs) {
            e.nullToEmpty();
        }
    }
}