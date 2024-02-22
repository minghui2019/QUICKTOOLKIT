package archives.entity;

import com.google.common.base.Strings;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XStreamAliasType("业务实体")
public class YWST implements ITransferString {
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

    @Override
    public void emptyToNull() {
        ywxw = Strings.emptyToNull(ywxw);
        ywzt = Strings.emptyToNull(ywzt);
        ywstbsf = Strings.emptyToNull(ywstbsf);
        xwms = Strings.emptyToNull(xwms);
        xwsj = Strings.emptyToNull(xwsj);
        xwyj = Strings.emptyToNull(xwyj);
    }

    @Override
    public void nullToEmpty() {
        ywxw = Strings.nullToEmpty(ywxw);
        ywzt = Strings.nullToEmpty(ywzt);
        ywstbsf = Strings.nullToEmpty(ywstbsf);
        xwms = Strings.nullToEmpty(xwms);
        xwsj = Strings.nullToEmpty(xwsj);
        xwyj = Strings.nullToEmpty(xwyj);
    }
}