package no.hiof.ahmedak.papervault.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class Receipt implements Parcelable {

    private String receipt_id;
    private double amount;
    private String receipt_title;
    private String receipt_date;
    private String image_path;
    private String user_id;
    private String store_id;
    private Boolean favorite;
    private String receipt_location;

    public Receipt(String receipt_id, double amount, String receipt_title, String receipt_date, String image_path, String user_id, String store_id, Boolean favorite, String receipt_location) {
        this.receipt_id = receipt_id;
        this.amount = amount;
        this.receipt_title = receipt_title;
        this.receipt_date = receipt_date;
        this.image_path = image_path;
        this.user_id = user_id;
        this.store_id = store_id;
        this.favorite = favorite;
        this.receipt_location = receipt_location;
    }

    public Receipt(){

    }

    protected Receipt(Parcel in) {
        receipt_id = in.readString();
        amount = in.readDouble();
        receipt_title = in.readString();
        receipt_date = in.readString();
        image_path = in.readString();
        user_id = in.readString();
        store_id = in.readString();
        byte tmpFavorite = in.readByte();
        favorite = tmpFavorite == 0 ? null : tmpFavorite == 1;
        receipt_location = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceipt_title() {
        return receipt_title;
    }

    public void setReceipt_title(String receipt_title) {
        this.receipt_title = receipt_title;
    }

    public String getReceipt_date() {
        return receipt_date;
    }

    public void setReceipt_date(String receipt_date) {
        this.receipt_date = receipt_date;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getReceipt_location() {
        return receipt_location;
    }

    public void setReceipt_location(String receipt_location) {
        this.receipt_location = receipt_location;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receipt_id='" + receipt_id + '\'' +
                ", amount=" + amount +
                ", receipt_title='" + receipt_title + '\'' +
                ", receipt_date='" + receipt_date + '\'' +
                ", image_path='" + image_path + '\'' +
                ", user_id='" + user_id + '\'' +
                ", store_id='" + store_id + '\'' +
                ", favorite=" + favorite +
                ", receipt_location='" + receipt_location + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(receipt_id);
        dest.writeDouble(amount);
        dest.writeString(receipt_title);
        dest.writeString(receipt_date);
        dest.writeString(image_path);
        dest.writeString(user_id);
        dest.writeString(store_id);
        dest.writeByte((byte) (favorite == null ? 0 : favorite ? 1 : 2));
        dest.writeString(receipt_location);
    }
}
