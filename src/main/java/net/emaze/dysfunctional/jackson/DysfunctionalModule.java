package net.emaze.dysfunctional.jackson;

import net.emaze.dysfunctional.options.Box;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;
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

public class DysfunctionalModule extends SimpleModule {

    public DysfunctionalModule() {
        super("dysfunctional-module", new Version(1, 0, 0, null));
        this.addSerializer(Maybe.class, new MaybeToArray());
        this.addSerializer(Either.class, new EitherToArray());
        this.addSerializer(Box.class, new BoxToArray());
        this.addSerializer(Pair.class, new PairToArray());
        this.addSerializer(Triple.class, new TripleToArray());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addDeserializers(new Deserializers.Base() {
            @Override
            public JsonDeserializer<?> findBeanDeserializer(final JavaType type, final DeserializationConfig config, final DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
                if (Maybe.class.isAssignableFrom(type.getRawClass())) {
                    return new MaybeFromArray(type.containedType(0));
                }
                if (Box.class.isAssignableFrom(type.getRawClass())) {
                    return new BoxFromArray(type.containedType(0));
                }
                if (Either.class.isAssignableFrom(type.getRawClass())) {
                    return new EitherFromArray(type.containedType(0), type.containedType(1));
                }
                if (Pair.class.isAssignableFrom(type.getRawClass())) {
                    return new PairFromArray(type.containedType(0), type.containedType(1));
                }
                if (Triple.class.isAssignableFrom(type.getRawClass())) {
                    return new TripleFromArray(type.containedType(0), type.containedType(1), type.containedType(2));
                }
                return null;
            }
        });
    }
}
