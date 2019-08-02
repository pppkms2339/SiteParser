package ru.loshmanov;

/**
 * The class represents product from site AliExpress.
 *
 */
public class Product {
    private String productId = "";
    private String sellerId = "";
    private String productImage = "";
    private String productDetailUrl = "";
    private String productTitle = "";
    private String minPrice = "";
    private String maxPrice = "";

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setProductDetailUrl(String productDetailUrl) {
        this.productDetailUrl = productDetailUrl;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String[] toArray() {
        return new String[]{productId, sellerId, productImage, productDetailUrl, productTitle, minPrice, maxPrice};
    }
}
