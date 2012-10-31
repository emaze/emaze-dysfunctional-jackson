package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.jackson.MaybeFromArrayTest.BeanWithMaybe;
import net.emaze.dysfunctional.options.Maybe;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class MaybeToArrayTest {

    @Test
    public void canSerialize() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithMaybe bwm = new BeanWithMaybe();
        bwm.setInner(Maybe.just(42));
        final String got = mapper.writeValueAsString(bwm);
        Assert.assertTrue(got.contains("42"));
    }
}
