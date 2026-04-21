package com.AppVersionManagementSystem.model;

import java.util.Objects;

public final class App {
    private final String name;

    public App(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("App name cannot be null or blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return Objects.equals(name, app.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "App{name='" + name + "'}";
    }
}
