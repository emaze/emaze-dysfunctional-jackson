package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module.SetupContext;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.emaze.dysfunctional.options.Box;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;

public class DysfunctionalModule extends SimpleModule {

    public DysfunctionalModule() {
        super("dysfunctional-module", new Version(2, 0, 0, null, "net.emaze", "emaze-dysfunctional-jackson"));
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
            public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
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
