package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.tuples.Triple;
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

    @Test
    public void canDeserializeReifiedTriple() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        Triple<Integer, Integer, Integer> got = mapper.readValue("[42, 42, 42]", ReifiedTriple.class);
        Assert.assertEquals(Triple.of(42, 42, 42), got);
    }

    public static class ReifiedTriple extends Triple<Integer, Integer, Integer> {

        public ReifiedTriple(Integer first, Integer second, Integer third) {
            super(first, second, third);
        }
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
