package com.company.crm;

import org.springframework.test.context.junit.jupiter.EnabledIf;

@EnabledIf("#{environment['test.ai.enabled'] == 'true' && environment['spring.ai.openai.api-key'] != null && environment['spring.ai.openai.api-key'] != '<<YOUR_API_KEY>>'}")
public class AbstractAiTest extends AbstractTest {
}
