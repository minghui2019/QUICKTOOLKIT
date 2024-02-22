package archives.entity;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装内容
 */
@Setter
@Getter
public class FZNR implements ITransferString {
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
    @XStreamAlias("审批流程块")
    private List<SPLC> splcs;

    @XStreamAlias("文件实体块")
    private List<WJSTAJ> wjstajs;

    public FZNR() {
        this.ly = new LY();
        this.dh = new DH();
        this.wjsts = Lists.newArrayList();
        this.jgrysts = Lists.newArrayList();
        this.ywsts = Lists.newArrayList();
        this.stgxs = Lists.newArrayList();
        this.splcs = Lists.newArrayList();
        this.wjstajs = Lists.newArrayList();
    }

    @Override
    public void emptyToNull() {
        wjstbsf = Strings.emptyToNull(wjstbsf);
        jhcc = Strings.emptyToNull(jhcc);
        ly.emptyToNull();
        dh.emptyToNull();
        for (WJST e : wjsts) {
            e.emptyToNull();
        }
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
        for (WJSTAJ e : wjstajs) {
            e.emptyToNull();
        }
        if ("案卷".equals(jhcc)) {
            this.setWjsts(null);
            this.setJgrysts(null);
            this.setYwsts(null);
            this.setStgxs(null);
            this.setSplcs(null);
        } else {
            this.setWjstajs(null);
        }
    }

    @Override
    public void nullToEmpty() {
        wjstbsf = Strings.nullToEmpty(wjstbsf);
        jhcc = Strings.nullToEmpty(jhcc);
        ly.nullToEmpty();
        dh.nullToEmpty();
        for (WJST e : wjsts) {
            e.nullToEmpty();
        }
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
        for (WJSTAJ e : wjstajs) {
            e.nullToEmpty();
        }
        if ("案卷".equals(jhcc)) {
            this.setWjsts(null);
            this.setJgrysts(null);
            this.setYwsts(null);
            this.setStgxs(null);
            this.setSplcs(null);
        } else {
            this.setWjstajs(null);
        }
    }
}