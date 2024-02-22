package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XStreamAliasType("实体关系")
public class STGX implements ITransferString {
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

    @Override
    public void emptyToNull() {
        gxms = Strings.emptyToNull(gxms);
        gxlx = Strings.emptyToNull(gxlx);
        gx = Strings.emptyToNull(gx);
        mbbsf = Strings.emptyToNull(mbbsf);
        ybsf = Strings.emptyToNull(ybsf);
    }

    @Override
    public void nullToEmpty() {
        gxms = Strings.nullToEmpty(gxms);
        gxlx = Strings.nullToEmpty(gxlx);
        gx = Strings.nullToEmpty(gx);
        mbbsf = Strings.nullToEmpty(mbbsf);
        ybsf = Strings.nullToEmpty(ybsf);
    }
}