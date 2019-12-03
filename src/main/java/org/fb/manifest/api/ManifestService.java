package org.fb.manifest.api;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

@Service
public class ManifestService {
    public String manifest() {
        try {
            String jarName = currentJar();
            String other = jarName + "!/META-INF/MANIFEST.MF";

            Set<URL> candidates = candidates();
            URL filter = candidates.stream()
                    .filter(it -> it.getPath().contains(other))
                    .findFirst()
                    .orElse(null);


            if (filter != null) {
                URLConnection urlConnection = filter.openConnection();
                InputStream iss = urlConnection.getInputStream();
                Attributes mainAttributes = new Manifest(iss).getMainAttributes();
                return mainAttributes.entrySet().stream()
                        .map(e -> e.getKey() + ":\t" + e.getValue())
                        .collect(Collectors.joining("\n", "Manifest\n", ""));
            } else {
                return "No Manifest found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "An exception occured";
        }
    }

    private Set<URL> candidates() throws IOException {
        List<URL> urls1 = Collections.list(Thread.currentThread().getContextClassLoader().getResources("/META-INF/MANIFEST.MF"));
        List<URL> urls2 = Collections.list(Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF"));
        Set<URL> union = new HashSet<>();
        union.addAll(urls1);
        union.addAll(urls2);
        return union;
    }

    String currentJar() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String sub = path.substring(1, path.indexOf(".jar") + ".jar".length());
        return sub.substring(sub.lastIndexOf("/") + 1);
    }
}
