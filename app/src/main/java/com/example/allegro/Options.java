package com.example.allegro;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "variantsByColorPatternAllowed",
        "advertisement",
        "advertisementPriceOptional",
        "offersWithProductPublicationEnabled",
        "productCreationEnabled",
        "productEANRequired"
})
public class Options {

    @JsonProperty("variantsByColorPatternAllowed")
    private Boolean variantsByColorPatternAllowed;
    @JsonProperty("advertisement")
    private Boolean advertisement;
    @JsonProperty("advertisementPriceOptional")
    private Boolean advertisementPriceOptional;
    @JsonProperty("offersWithProductPublicationEnabled")
    private Boolean offersWithProductPublicationEnabled;
    @JsonProperty("productCreationEnabled")
    private Boolean productCreationEnabled;
    @JsonProperty("productEANRequired")
    private Boolean productEANRequired;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("variantsByColorPatternAllowed")
    public Boolean getVariantsByColorPatternAllowed() {
        return variantsByColorPatternAllowed;
    }

    @JsonProperty("variantsByColorPatternAllowed")
    public void setVariantsByColorPatternAllowed(Boolean variantsByColorPatternAllowed) {
        this.variantsByColorPatternAllowed = variantsByColorPatternAllowed;
    }

    public Options withVariantsByColorPatternAllowed(Boolean variantsByColorPatternAllowed) {
        this.variantsByColorPatternAllowed = variantsByColorPatternAllowed;
        return this;
    }

    @JsonProperty("advertisement")
    public Boolean getAdvertisement() {
        return advertisement;
    }

    @JsonProperty("advertisement")
    public void setAdvertisement(Boolean advertisement) {
        this.advertisement = advertisement;
    }

    public Options withAdvertisement(Boolean advertisement) {
        this.advertisement = advertisement;
        return this;
    }

    @JsonProperty("advertisementPriceOptional")
    public Boolean getAdvertisementPriceOptional() {
        return advertisementPriceOptional;
    }

    @JsonProperty("advertisementPriceOptional")
    public void setAdvertisementPriceOptional(Boolean advertisementPriceOptional) {
        this.advertisementPriceOptional = advertisementPriceOptional;
    }

    public Options withAdvertisementPriceOptional(Boolean advertisementPriceOptional) {
        this.advertisementPriceOptional = advertisementPriceOptional;
        return this;
    }

    @JsonProperty("offersWithProductPublicationEnabled")
    public Boolean getOffersWithProductPublicationEnabled() {
        return offersWithProductPublicationEnabled;
    }

    @JsonProperty("offersWithProductPublicationEnabled")
    public void setOffersWithProductPublicationEnabled(Boolean offersWithProductPublicationEnabled) {
        this.offersWithProductPublicationEnabled = offersWithProductPublicationEnabled;
    }

    public Options withOffersWithProductPublicationEnabled(Boolean offersWithProductPublicationEnabled) {
        this.offersWithProductPublicationEnabled = offersWithProductPublicationEnabled;
        return this;
    }

    @JsonProperty("productCreationEnabled")
    public Boolean getProductCreationEnabled() {
        return productCreationEnabled;
    }

    @JsonProperty("productCreationEnabled")
    public void setProductCreationEnabled(Boolean productCreationEnabled) {
        this.productCreationEnabled = productCreationEnabled;
    }

    public Options withProductCreationEnabled(Boolean productCreationEnabled) {
        this.productCreationEnabled = productCreationEnabled;
        return this;
    }

    @JsonProperty("productEANRequired")
    public Boolean getProductEANRequired() {
        return productEANRequired;
    }

    @JsonProperty("productEANRequired")
    public void setProductEANRequired(Boolean productEANRequired) {
        this.productEANRequired = productEANRequired;
    }

    public Options withProductEANRequired(Boolean productEANRequired) {
        this.productEANRequired = productEANRequired;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Options withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }



}