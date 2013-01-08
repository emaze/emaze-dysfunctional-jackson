package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.jackson.PairFromArrayTest.BeanWithPair;
import net.emaze.dysfunctional.tuples.Pair;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class PairToArrayTest {

    @Test
    public void canSerialize() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithPair bwp = new BeanWithPair();
        bwp.setInner(Pair.of("42", 43));
        final String got = mapper.writeValueAsString(bwp);
        Assert.assertTrue(got.contains("\"42\"") && got.contains("43"));
    }
    
}
