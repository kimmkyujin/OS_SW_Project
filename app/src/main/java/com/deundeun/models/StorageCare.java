package com.deundeun.models;

import com.google.gson.annotations.SerializedName;

public class StorageCare {
    private Long id;
    
    @SerializedName("item_name")
    private String itemName;
    
    @SerializedName("image_url")
    private String imageUrl;
    
    @SerializedName("description")
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
