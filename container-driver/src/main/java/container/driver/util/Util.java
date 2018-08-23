package container.driver.util;

import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;

public class Util  {

    public int getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.valueOf(name.substring(0, name.indexOf("@")));
    }
    
    public void writePid(Writer w) throws IOException {
        w.write(String.valueOf(getPid()));
    }
}