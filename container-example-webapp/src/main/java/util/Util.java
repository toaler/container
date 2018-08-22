package util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.UUID;

public class Util {

    public UUID getFid(Exception e) {
        try {
            return UUID.nameUUIDFromBytes((e.getClass().getCanonicalName() + Arrays.toString(e
                    .getStackTrace())).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            return UUID.fromString("");
        }
    }
}
