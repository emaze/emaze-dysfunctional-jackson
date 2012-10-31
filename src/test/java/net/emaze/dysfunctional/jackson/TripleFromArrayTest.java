package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.tuples.Triple;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class TripleFromArrayTest {

    @Test
    public void canDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        BeanWithTriple bean = mapper.readValue("{'inner':['42',42, true]}".replace('\'', '"'), BeanWithTriple.class);
        Assert.assertEquals(Triple.of("42", 42, true), bean.getInner());
    }

    public static class BeanWithTriple {

        public Triple<String, Integer, Boolean> getInner() {
            return inner;
        }

        public void setInner(Triple<String, Integer, Boolean> inner) {
            this.inner = inner;
        }
        private Triple<String, Integer, Boolean> inner;
    }

}
