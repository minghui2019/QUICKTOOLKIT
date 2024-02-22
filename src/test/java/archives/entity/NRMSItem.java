package archives.entity;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NRMSItem implements ITransferString {
    private String name;
    private String value;

    @Override
    public void emptyToNull() {
        value = Strings.emptyToNull(value);
    }

    @Override
    public void nullToEmpty() {
        value = Strings.nullToEmpty(value);
    }
}
