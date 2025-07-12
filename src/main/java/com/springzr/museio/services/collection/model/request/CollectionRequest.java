package com.springzr.museio.services.collection.model.request;

import jakarta.validation.constraints.NotBlank;

public class CollectionRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Portfolio is required")
    private String portfolio;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }
}
