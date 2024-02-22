package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * 档号
 */
@Setter
@Getter
public class DH implements ITransferString {
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

    @Override
    public void emptyToNull() {
        jghwt = Strings.emptyToNull(jghwt);
        flh = Strings.emptyToNull(flh);
        qzh = Strings.emptyToNull(qzh);
        nd = Strings.emptyToNull(nd);
        ajh = Strings.emptyToNull(ajh);
        lbh = Strings.emptyToNull(lbh);
        gdbmdh = Strings.emptyToNull(gdbmdh);
        bgqx = Strings.emptyToNull(bgqx);
        jh = Strings.emptyToNull(jh);
    }

    @Override
    public void nullToEmpty() {
        jghwt = Strings.nullToEmpty(jghwt);
        flh = Strings.nullToEmpty(flh);
        qzh = Strings.nullToEmpty(qzh);
        nd = Strings.nullToEmpty(nd);
        ajh = Strings.nullToEmpty(ajh);
        lbh = Strings.nullToEmpty(lbh);
        gdbmdh = Strings.nullToEmpty(gdbmdh);
        bgqx = Strings.nullToEmpty(bgqx);
        jh = Strings.nullToEmpty(jh);
    }
}