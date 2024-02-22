package archives.entity;

import java.util.List;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NRMS implements ITransferString {
    List<NRMSItem> items = Lists.newArrayList();

    public NRMS addItem(NRMSItem item) {
        items.add(item);
        return this;
    }

    public NRMS addItem(String name, String value) {
        return addItem(new NRMSItem(name, value));
    }

    @Override
    public void emptyToNull() {
        for (NRMSItem item : items) {
            item.emptyToNull();
        }
    }

    @Override
    public void nullToEmpty() {
        for (NRMSItem item : items) {
            item.nullToEmpty();
        }
    }
}
