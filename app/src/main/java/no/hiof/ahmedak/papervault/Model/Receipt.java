package no.hiof.ahmedak.papervault.Model;

import java.util.Date;

public class Receipt {

    private String receipt_id;
    private double amount;
    private String receipt_title;
    private String receipt_date;
    private String image_path;
    private String user_id;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Receipt(String receipt_id, double amount, String receipt_title, String receipt_date, String image_path, String user_id) {

        this.receipt_id = receipt_id;
        this.amount = amount;
        this.receipt_title = receipt_title;
        this.receipt_date = receipt_date;
        this.image_path = image_path;
        this.user_id = user_id;
    }

    public Receipt() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
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

    @Override
    public String toString() {
        return "Receipt{" +
                "receipt_id='" + receipt_id + '\'' +
                ", amount=" + amount +
                ", receipt_title='" + receipt_title + '\'' +
                ", receipt_date='" + receipt_date + '\'' +
                ", image_path='" + image_path + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
