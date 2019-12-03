package org.fb.manifest.api.manifest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ManifestServiceIT {

    @Autowired
    private ManifestService service;

    @Test
    void integrationTestShouldRun() {
        assertNotNull(service.manifest());
    }
}