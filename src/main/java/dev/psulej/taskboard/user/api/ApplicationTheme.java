package dev.psulej.taskboard.user.api;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.Assert;

import java.beans.ConstructorProperties;
import java.util.List;

public record ApplicationTheme(@JsonValue String value) {

    public static ApplicationTheme DARK = new ApplicationTheme("dark");

    @ConstructorProperties("value")
    public ApplicationTheme(String value) {
        Assert.hasText(value, "Application theme cannot be empty");
        Assert.isTrue(isThemeAllowed(value), "Application theme was not recognized");
        this.value = value;
    }

    private static boolean isThemeAllowed(String value) {
        return List.of("dark", "light", "candy","blue","green","purple").contains(value);
    }
}