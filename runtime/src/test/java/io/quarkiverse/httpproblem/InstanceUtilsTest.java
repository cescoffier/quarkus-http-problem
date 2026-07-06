package io.quarkiverse.httpproblem;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;

class InstanceUtilsTest {

    @Test
    void plainPathPreservesSlashes() {
        URI uri = InstanceUtils.pathToInstance("/api/users/42");

        assertThat(uri.toString()).isEqualTo("/api/users/42");
        assertThat(uri.getPath()).isEqualTo("/api/users/42");
    }

    @Test
    void spacesArePercentEncoded() {
        URI uri = InstanceUtils.pathToInstance("/api/users/john doe");

        assertThat(uri.toASCIIString()).isEqualTo("/api/users/john%20doe");
        assertThat(uri.getPath()).isEqualTo("/api/users/john doe");
    }

    @Test
    void unwiseCharactersAreEncoded() {
        URI uri = InstanceUtils.pathToInstance("/path|with{unwise}chars");

        assertThat(uri.toASCIIString()).doesNotContain("|").doesNotContain("{").doesNotContain("}");
        assertThat(uri.getPath()).isEqualTo("/path|with{unwise}chars");
    }

    @Test
    void nullInputReturnsNull() {
        assertThat(InstanceUtils.pathToInstance(null)).isNull();
    }

    @Test
    void roundTripPlainPath() {
        String original = "/api/users/42";

        String result = InstanceUtils.instanceToPath(InstanceUtils.pathToInstance(original));

        assertThat(result).isEqualTo(original);
    }

    @Test
    void roundTripEncodesSpecialCharacters() {
        URI uri = InstanceUtils.pathToInstance("/non|existing{path /with{unwise\\characters>#");

        assertThat(InstanceUtils.instanceToPath(uri))
                .isEqualTo("/non%7Cexisting%7Bpath%20/with%7Bunwise%5Ccharacters%3E%23");
    }

    @Test
    void instanceToPathReturnsValidUriReference() {
        URI uri = InstanceUtils.pathToInstance("/api/users/john doe");

        assertThat(InstanceUtils.instanceToPath(uri)).isEqualTo("/api/users/john%20doe");
    }

}
