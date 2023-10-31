package com.example.studentrecorder.config.properties;

import com.example.studentrecorder.model.Student;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

@ConfigurationProperties(prefix = "sample-data")
@Getter
@Setter
public class InitDataProperties {
    private Boolean enabled;
    @NestedConfigurationProperty
    private List<Student> students;
}
