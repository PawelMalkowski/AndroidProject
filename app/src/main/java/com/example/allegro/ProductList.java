package com.example.allegro;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "items",
        "searchMeta",
        "categories",
        "filters",
        "sort"
})



public class ProductList {
    @JsonProperty("items")
    public Items items ;
    @JsonProperty("searchMeta")
    public SearchMeta searchMeta ;
    @JsonProperty("categories")
    public CategoriesProduct categories ;
    @JsonProperty("filters")
    public List<Filter> filters;
    @JsonProperty("sort")
    public List<Sort> sort;
}
