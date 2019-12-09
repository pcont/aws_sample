package org.fb.manifest.api;


import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class ManifestServiceTest {

    private ManifestService service = new ManifestService();

    @Test
    public void unitTestShouldRun() {
        assertNotNull(service.manifest());
    }
}