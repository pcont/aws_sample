package org.fb.kdiff.api.manifest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManifestServiceTest {

    private ManifestService service = new ManifestService();

    @Test
    void manifest() {
        assertNotNull(service.manifest());
    }
}