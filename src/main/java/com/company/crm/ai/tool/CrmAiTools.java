package com.company.crm.ai.tool;

import com.company.crm.model.base.UuidEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.HashSet;

public record CrmAiTools(Collection<CrmAiTool> tools) {

    public static Builder builder(ApplicationContext applicationContext) {
        return new Builder(applicationContext);
    }

    public static class Builder {

        private final ApplicationContext applicationContext;

        public Builder(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        private final Collection<CrmAiTool> tools = new HashSet<>();

        public Builder viewsDiscoveryTool() {
            return tool(ViewsDiscoveryTool.create(applicationContext));
        }

        public Builder jpqlQueryExecutorTool() {
            return tool(JpqlExecutorTool.create(applicationContext));
        }

        public Builder entitiesDiscoveryTool(@Nullable Collection<Class<? extends UuidEntity>> whitelist) {
            return tool(EntitiesDiscoveryTool.create(applicationContext, whitelist));
        }

        public Builder reportsDiscoveryTool(Collection<String> whitelist) {
            return tool(ReportsDiscoveryTool.create(applicationContext, whitelist));
        }

        public Builder runReportTool(Collection<String> allowedReportCodes) {
            return tool(RunReportTool.create(applicationContext, allowedReportCodes));
        }

        public Builder tool(CrmAiTool tool) {
            tools.add(tool);
            return this;
        }

        public Builder tools(Collection<CrmAiTool> tools) {
            this.tools.addAll(tools);
            return this;
        }

        public CrmAiTools build() {
            return new CrmAiTools(tools);
        }
    }
}
