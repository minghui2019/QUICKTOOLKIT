package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QXGL implements ITransferString {
    @XStreamAlias("知识产权说明")
    private String zscqsm;
    @XStreamAlias("授权对象")
    private String sqdx;
    @XStreamAlias("授权行为")
    private String sqxw;
    @XStreamAlias("控制标识")
    private String kzbs;

    @Override
    public void emptyToNull() {
        kzbs = Strings.emptyToNull(kzbs);
        zscqsm = Strings.emptyToNull(zscqsm);
        sqxw = Strings.emptyToNull(sqxw);
        sqdx = Strings.emptyToNull(sqdx);
    }

    @Override
    public void nullToEmpty() {
        kzbs = Strings.nullToEmpty(kzbs);
        zscqsm = Strings.nullToEmpty(zscqsm);
        sqxw = Strings.nullToEmpty(sqxw);
        sqdx = Strings.nullToEmpty(sqdx);
    }
}