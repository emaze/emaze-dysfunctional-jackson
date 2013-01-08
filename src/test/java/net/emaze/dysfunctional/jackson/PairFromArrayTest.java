package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.tuples.Pair;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class PairFromArrayTest {

    @Test
    public void canDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        BeanWithPair bean = mapper.readValue("{'inner':['42',42]}".replace('\'', '"'), BeanWithPair.class);
        Assert.assertEquals(Pair.of("42", 42), bean.getInner());
    }

    public static class BeanWithPair {

        private Pair<String, Integer> inner;

        public Pair<String, Integer> getInner() {
            return inner;
        }

        public void setInner(Pair<String, Integer> inner) {
            this.inner = inner;
        }
    }

}
