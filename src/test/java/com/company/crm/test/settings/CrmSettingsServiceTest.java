package com.company.crm.test.settings;

import com.company.crm.AbstractServiceTest;
import com.company.crm.app.service.settings.CrmSettingsService;
import com.company.crm.model.settings.CrmSettings;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CrmSettingsServiceTest extends AbstractServiceTest<CrmSettingsService> {

    @Test
    void testVatPercentSetting() {
        assertThat(service.getDefaultVatPercent()).isEqualByComparingTo("20");

        CrmSettings settings = service.loadSettings();
        settings.setDefaultVatPercent(new BigDecimal("15"));
        service.saveSettings(settings);

        assertThat(service.getDefaultVatPercent()).isEqualByComparingTo("15");
    }
}
