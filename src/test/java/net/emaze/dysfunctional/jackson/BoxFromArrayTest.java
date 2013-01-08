package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.options.Box;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class BoxFromArrayTest {

    @Test
    public void emptyBox() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithBox bean = mapper.readValue("{'inner': []}".replace('\'', '"'), BeanWithBox.class);
        Assert.assertEquals(Box.empty(), bean.getInner());
    }

    @Test
    public void filledBox() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithBox bean = mapper.readValue("{'inner': [42]}".replace('\'', '"'), BeanWithBox.class);
        Assert.assertEquals(Box.of(42), bean.getInner());
    }

    public static class BeanWithBox {

        public Box<Integer> getInner() {
            return inner;
        }

        public void setInner(Box<Integer> inner) {
            this.inner = inner;
        }
        private Box<Integer> inner;
    }
}
