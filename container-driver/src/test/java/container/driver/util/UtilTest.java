package container.driver.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;
import container.driver.util.Util;

public class UtilTest {

    @Test
    public void testAcquirePID() {
        Util p = new Util();
        assertTrue(p.getPid() >= 0);
    }
    
    @Test
    public void testWritingPID() throws IOException {
        Util p = new Util();
        StringWriter w = new StringWriter();
        p.writePid(w);
        assertEquals(String.valueOf(p.getPid()), w.toString());
    }
}