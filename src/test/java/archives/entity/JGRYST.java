package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XStreamAliasType("机构人员实体")
public class JGRYST implements ITransferString {
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

    @Override
    public void emptyToNull() {
        jgrymc = Strings.emptyToNull(jgrymc);
        jgrybsf = Strings.emptyToNull(jgrybsf);
        jgryssjg = Strings.emptyToNull(jgryssjg);
        jgrylx = Strings.emptyToNull(jgrylx);
        jgrydm = Strings.emptyToNull(jgrydm);
        lxfs = Strings.emptyToNull(lxfs);
    }

    @Override
    public void nullToEmpty() {
        jgrymc = Strings.nullToEmpty(jgrymc);
        jgrybsf = Strings.nullToEmpty(jgrybsf);
        jgryssjg = Strings.nullToEmpty(jgryssjg);
        jgrylx = Strings.nullToEmpty(jgrylx);
        jgrydm = Strings.nullToEmpty(jgrydm);
        lxfs = Strings.nullToEmpty(lxfs);
    }
}