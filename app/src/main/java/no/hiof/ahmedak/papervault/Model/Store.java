package no.hiof.ahmedak.papervault.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Store implements Parcelable {

    private String store_name;
    private Long store_lat;
    private Long store_long;
    private String store_domain;
    private String store_logo;
    private String store_id;
    private String user_id;



    public Store() {

    }

    public Store(String store_name, Long store_lat, Long store_long, String store_domain, String store_logo, String store_id, String user_id) {
        this.store_name = store_name;
        this.store_lat = store_lat;
        this.store_long = store_long;
        this.store_domain = store_domain;
        this.store_logo = store_logo;
        this.store_id = store_id;
        this.user_id = user_id;
    }

    protected Store(Parcel in) {
        store_name = in.readString();
        if (in.readByte() == 0) {
            store_lat = null;
        } else {
            store_lat = in.readLong();
        }
        if (in.readByte() == 0) {
            store_long = null;
        } else {
            store_long = in.readLong();
        }
        store_domain = in.readString();
        store_logo = in.readString();
        store_id = in.readString();
        user_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(store_name);
        if (store_lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(store_lat);
        }
        if (store_long == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(store_long);
        }
        dest.writeString(store_domain);
        dest.writeString(store_logo);
        dest.writeString(store_id);
        dest.writeString(user_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

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

    public String getStore_domain() {
        return store_domain;
    }

    public void setStore_domain(String store_domain) {
        this.store_domain = store_domain;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Store{" +
                "store_name='" + store_name + '\'' +
                ", store_lat=" + store_lat +
                ", store_long=" + store_long +
                ", store_domain='" + store_domain + '\'' +
                ", store_logo='" + store_logo + '\'' +
                ", store_id='" + store_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
