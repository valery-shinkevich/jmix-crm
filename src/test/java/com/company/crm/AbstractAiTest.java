package com.company.crm;

import org.springframework.test.context.junit.jupiter.EnabledIf;

@EnabledIf(expression = """
        #{environment.getProperty('tests.ai.enabled') == 'true'
        && environment.getProperty('spring.ai.openai.api-key') != '<YOUR_API_KEY>'}""",
        loadContext = true)
public class AbstractAiTest extends AbstractTest {
}
