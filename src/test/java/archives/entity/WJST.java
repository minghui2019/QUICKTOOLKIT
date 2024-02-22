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
public class WJST implements ITransferString {
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

    public WJST() {
        this.nrms = new NRMS();
        this.wds = Lists.newArrayList();
        this.xstz = new XSTZ();
        this.ccwz = new CCWZ();
        this.qxgl = new QXGL();
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
    }
}