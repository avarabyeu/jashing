package com.github.avarabyeu.jashing.integration.github.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Andrei Varabyeu
 */
public class Label {
    private Long id;
    private String url;

    private String name;
    private String color;
    @SerializedName("default")
    private Boolean isDefault;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
