package no.hiof.ahmedak.papervault.Model;



public class Store {

    private String store_name;
    private Long store_lat;
    private Long store_long;
    private String store_retail;
    private String store_logo;
    private String store_id;


    public Store(String store_name, Long store_lat, Long store_long, String store_retail, String store_logo, String store_id) {
        this.store_name = store_name;
        this.store_lat = store_lat;
        this.store_long = store_long;
        this.store_retail = store_retail;
        this.store_logo = store_logo;
        this.store_id = store_id;
    }

    public Store() {

    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Long getStore_lat() {
        return store_lat;
    }

    public void setStore_lat(Long store_lat) {
        this.store_lat = store_lat;
    }

    public Long getStore_long() {
        return store_long;
    }

    public void setStore_long(Long store_long) {
        this.store_long = store_long;
    }

    public String getStore_retail() {
        return store_retail;
    }

    public void setStore_retail(String store_retail) {
        this.store_retail = store_retail;
    }

    public String getStore_logo() {
        return store_logo;
    }

    public void setStore_logo(String store_logo) {
        this.store_logo = store_logo;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    @Override
    public String toString() {
        return "Store{" +
                "store_name='" + store_name + '\'' +
                ", store_lat=" + store_lat +
                ", store_long=" + store_long +
                ", store_retail='" + store_retail + '\'' +
                ", store_logo='" + store_logo + '\'' +
                ", store_id='" + store_id + '\'' +
                '}';
    }
}
