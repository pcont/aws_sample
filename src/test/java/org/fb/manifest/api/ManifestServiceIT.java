package org.fb.manifest.api;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;


@SpringBootTest
class ManifestServiceIT {

    @Autowired
    private ManifestService service;

    @Test
    void integrationTestShouldRun() {
        assertNotNull(service.manifest());
    }
}