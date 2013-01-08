package net.emaze.dysfunctional.jackson.ranges;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module.SetupContext;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.Serializers;
import net.emaze.dysfunctional.Ranges;
import net.emaze.dysfunctional.ranges.Range;

/**
 *
 * @author rferranti
 */
public class RangeModule<T> extends SimpleModule {

    private final Ranges<T> ranges;
    private final Class<T> typeParameter;

    public RangeModule(Ranges<T> ranges, Class<T> typeParameter) {
        super("dysfunctional-range-module", new Version(2, 0, 0, null, "net.emaze", "emaze-dysfunctional-jackson"));
        this.ranges = ranges;
        this.typeParameter = typeParameter;
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addSerializers(new Serializers.Base() {
            @Override
            public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
                if (Range.class.isAssignableFrom(type.getRawClass()) && type.containedType(0).hasRawClass(typeParameter)) {
                    return new RangeToHash();
                }
                return null;
            }
        });
        context.addDeserializers(new Deserializers.Base() {
            @Override
            public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
                if (Range.class.isAssignableFrom(type.getRawClass()) && type.containedType(0).hasRawClass(typeParameter)) {
                    return new RangeFromHash<T>(type.containedType(0), ranges);
                }
                return null;
            }
        });

    }
}
