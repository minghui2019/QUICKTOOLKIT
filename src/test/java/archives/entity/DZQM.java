package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DZQM implements ITransferString {
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

    @Override
    public void emptyToNull() {
        qmr = Strings.emptyToNull(qmr);
        zs = Strings.emptyToNull(zs);
        qmsfbs = Strings.emptyToNull(qmsfbs);
        qmsj = Strings.emptyToNull(qmsj);
        qmjg = Strings.emptyToNull(qmjg);
        zsyz = Strings.emptyToNull(zsyz);
        qmgz = Strings.emptyToNull(qmgz);
    }

    @Override
    public void nullToEmpty() {
        qmr = Strings.nullToEmpty(qmr);
        zs = Strings.nullToEmpty(zs);
        qmsfbs = Strings.nullToEmpty(qmsfbs);
        qmsj = Strings.nullToEmpty(qmsj);
        qmjg = Strings.nullToEmpty(qmjg);
        zsyz = Strings.nullToEmpty(zsyz);
        qmgz = Strings.nullToEmpty(qmgz);
    }
}