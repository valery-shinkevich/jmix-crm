package com.company.crm.test.message;

import com.company.crm.CRMApplication;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessagePropertiesTest {

    private static final int EXPECTED_LOCALIZED_MESSAGE_FILES = 2;
    private static final Pattern LOCALIZED_MESSAGES_FILENAME = Pattern.compile("messages_(.+)\\.properties");

    @Test
    void localizedMessageBundlesContainSameKeys() throws IOException {
        Map<String, Properties> messagesByLocale = loadLocalizedMessages();

        Set<String> allKeys = new TreeSet<>();
        messagesByLocale.values().forEach(properties -> allKeys.addAll(properties.stringPropertyNames()));

        List<String> failures = new ArrayList<>();
        for (Map.Entry<String, Properties> entry : messagesByLocale.entrySet()) {
            Set<String> localeKeys = entry.getValue().stringPropertyNames();
            Set<String> missingKeys = new TreeSet<>(allKeys);
            missingKeys.removeAll(localeKeys);

            if (!missingKeys.isEmpty()) {
                failures.add(entry.getKey() + " is missing keys: " + String.join(", ", missingKeys));
            }
        }

        assertTrue(
                failures.isEmpty(),
                "Localized bundles do not contain the same keys:\n" + String.join("\n", failures)
        );
    }

    private static void ensureMessageFilesAmount(Map<String, Properties> messagesByLocale) {
        assertTrue(
                messagesByLocale.size() >= EXPECTED_LOCALIZED_MESSAGE_FILES,
                "Expected at least " + EXPECTED_LOCALIZED_MESSAGE_FILES
                        + " localized message bundles but found: " + messagesByLocale.keySet()
        );
    }

    private Map<String, Properties> loadLocalizedMessages() throws IOException {
        String packagePath = CRMApplication.class.getPackageName().replace(".", "/");
        String resourcePattern = "classpath*:" + packagePath + "/messages_*.properties";

        Resource[] resources = new PathMatchingResourcePatternResolver(getClass().getClassLoader())
                .getResources(resourcePattern);

        assertTrue(
                resources.length > 0,
                "No localized message bundles found for pattern '" + resourcePattern + "'"
        );

        Map<String, Properties> messagesByLocale = new TreeMap<>();
        for (Resource resource : resources) {
            String localeId = extractLocaleId(resource);
            Properties properties = messagesByLocale.computeIfAbsent(localeId, ignored -> new Properties());

            try (var stream = resource.getInputStream()) {
                properties.load(stream);
            }
        }

        ensureMessageFilesAmount(messagesByLocale);

        return messagesByLocale;
    }

    private String extractLocaleId(Resource resource) {
        String filename = resource.getFilename();
        assertNotNull(filename, "Localized message resource must have a filename");

        Matcher matcher = LOCALIZED_MESSAGES_FILENAME.matcher(filename);
        assertTrue(matcher.matches(), "Unexpected localized message filename: " + filename);
        return matcher.group(1);
    }
}
