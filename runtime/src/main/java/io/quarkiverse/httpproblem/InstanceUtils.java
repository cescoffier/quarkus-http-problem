package io.quarkiverse.httpproblem;

import java.net.URI;
import java.net.URISyntaxException;

public final class InstanceUtils {

    public static URI pathToInstance(String path) {
        if (path == null) {
            return null;
        }
        try {
            return new URI(null, null, path, null);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static String instanceToPath(URI instance) {
        return instance.toASCIIString();
    }

}
