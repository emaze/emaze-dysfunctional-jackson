package net.emaze.dysfunctional.jackson.ranges;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.emaze.dysfunctional.Ranges;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.ranges.Range;

public class RangeFromHash<T> extends JsonDeserializer<Range<T>> {

    private final JavaType nestedType;
    private final Ranges<T> ranges;

    public RangeFromHash(JavaType nestedType, Ranges<T> ranges) {
        this.nestedType = nestedType;
        this.ranges = ranges;
    }

    @Override
    public Range<T> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JavaType containedType = ctxt.getTypeFactory().constructParametricType(DenseRangeSerializedForm.class, nestedType);
        final JavaType serializedType = ctxt.getTypeFactory().constructCollectionType(ArrayList.class, containedType);
        final List<DenseRangeSerializedForm<T>> serializedValue = (List<DenseRangeSerializedForm<T>>) ctxt.findContextualValueDeserializer(serializedType, null).deserialize(jp, ctxt);
        final Iterator<DenseRangeSerializedForm<T>> iterator = serializedValue.iterator();
        final DenseRangeSerializedForm<T> first = iterator.next();
        Range<T> range = ranges.rightHalfOpen(first.b, first.e);
        while (iterator.hasNext()) {
            final DenseRangeSerializedForm<T> current = iterator.next();
            range = ranges.union(range, ranges.rightHalfOpen(current.b, current.e));
        }
        return range;
    }
    public static class DenseRangeSerializedForm<T> {

        private T b;
        private Maybe<T> e;

        public T getB() {
            return b;
        }

        public void setB(T b) {
            this.b = b;
        }

        public Maybe<T> getE() {
            return e;
        }

        public void setE(Maybe<T> e) {
            this.e = e;
        }

    }
}
