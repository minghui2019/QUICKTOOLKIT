package archives.main;

import archives.entity.FZNR;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class FZNRConverter implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (source instanceof FZNR) {
            if ("案卷".equals(((FZNR) source).getJhcc())) {
                ((FZNR) source).setJgrysts(null);
                ((FZNR) source).setYwsts(null);
                ((FZNR) source).setStgxs(null);
                ((FZNR) source).setSplcs(null);
                context.convertAnother(source);
            }
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return null;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(FZNR.class);
    }
}
