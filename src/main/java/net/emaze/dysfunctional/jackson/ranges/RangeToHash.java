package net.emaze.dysfunctional.jackson.ranges;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import net.emaze.dysfunctional.ranges.Range;

public class RangeToHash extends JsonSerializer<Range> {

    @Override
    public void serialize(Range value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (Range<?> range : (List<Range>) value.densified()) {
            jgen.writeStartObject();
            jgen.writeFieldName("b");
            jgen.writeObject(range.begin());
            jgen.writeFieldName("e");
            jgen.writeObject(range.end());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
}
