package net.emaze.dysfunctional.jackson.exp;

import net.emaze.dysfunctional.ranges.Range;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.type.JavaType;

/**
 *
 * @author rferranti
 */
public class RangeModule extends SimpleModule {

    public RangeModule() {
        super("dysfunctiona-range-module", new Version(1, 0, 0, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addDeserializers(new Deserializers.Base() {
            @Override
            public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
                if (Range.class.isAssignableFrom(type.getRawClass())) {
                    return new RangeFromHash(type.containedType(0));
                }
                return null;
            }
        });

    }
}
