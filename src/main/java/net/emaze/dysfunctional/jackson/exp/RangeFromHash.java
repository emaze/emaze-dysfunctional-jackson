package net.emaze.dysfunctional.jackson.exp;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.emaze.dysfunctional.order.SequencingPolicy;
import net.emaze.dysfunctional.ranges.DenseRange;
import net.emaze.dysfunctional.ranges.Endpoints;
import net.emaze.dysfunctional.ranges.Range;
import net.emaze.dysfunctional.ranges.RangeOps;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.JavaType;

public class RangeFromHash extends JsonDeserializer<Range<?>> {

    private final JavaType nestedType;

    public RangeFromHash(JavaType nestedType) {
        this.nestedType = nestedType;
    }

    @Override
    public Range<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // {s:'Sequencer' ,c:'Comparator', d:[{l:1,u:2,e:'IncludeBoth'},..]}
        final JavaType serializedType = ctxt.getTypeFactory().constructParametricType(RangeSerializedForm.class, nestedType);
        final RangeSerializedForm<Object> serializedValue = (RangeSerializedForm<Object>) ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), serializedType, null).deserialize(jp, ctxt);
        final Comparator<Object> comparator = forName(serializedValue.c);
        final SequencingPolicy<Object> sequencer = forName(serializedValue.s);
        final Iterator<DenseRangeSerializedForm<Object>> iterator = serializedValue.d.iterator();
        final DenseRangeSerializedForm<Object> first = iterator.next();
        Range<Object> range = new DenseRange<Object>(sequencer, comparator, first.e, first.l, first.u);
        while (iterator.hasNext()) {
            final DenseRangeSerializedForm<Object> current = iterator.next();
            range = RangeOps.union(sequencer, comparator, range, new DenseRange<Object>(sequencer, comparator, current.e, current.l, current.u));
        }
        return range;
    }

    private <T> T forName(String type) {
        try {
            return (T) Class.forName(type).newInstance();
        } catch (InstantiationException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static class RangeSerializedForm<T> {

        private String s;
        private String c;
        private List<DenseRangeSerializedForm<T>> d;

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public List<DenseRangeSerializedForm<T>> getD() {
            return d;
        }

        public void setD(List<DenseRangeSerializedForm<T>> d) {
            this.d = d;
        }
    }

    public static class DenseRangeSerializedForm<T> {

        private T l;
        private T u;
        private Endpoints e;

        public T getL() {
            return l;
        }

        public void setL(T l) {
            this.l = l;
        }

        public T getU() {
            return u;
        }

        public void setU(T u) {
            this.u = u;
        }

        public Endpoints getE() {
            return e;
        }

        public void setE(Endpoints e) {
            this.e = e;
        }
    }
}
