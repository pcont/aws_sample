package org.fb.manifest.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ManifestServiceIT {

    @Autowired
    private ManifestService service;

    @Test
    public void integrationTestShouldRun() {
        assertNotNull(service.manifest());
    }
}