package com.example.hrmsbackend.dtos.request;

import com.example.hrmsbackend.dtos.Create;
import com.example.hrmsbackend.dtos.Update;
import jakarta.validation.constraints.NotBlank;

public class ConfigurationRequestDTO {
    
    @NotBlank(groups = {Create.class}, message = "Config key cannot be blank")
    private String configKey;

    @NotBlank(groups = {Create.class, Update.class}, message = "Config value cannot be blank")
    private String configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}
