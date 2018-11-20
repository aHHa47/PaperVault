package no.hiof.ahmedak.papervault.Model;

public class Company {

    private String Logo_name;
    private String logo_domain;
    private String logo_img_path;

    public Company(String logo_name, String logo_domain, String logo_img_path) {
        Logo_name = logo_name;
        this.logo_domain = logo_domain;
        this.logo_img_path = logo_img_path;
    }

    public Company() {

    }

    public String getLogo_name() {
        return Logo_name;
    }

    public void setLogo_name(String logo_name) {
        Logo_name = logo_name;
    }

    public String getLogo_domain() {
        return logo_domain;
    }

    public void setLogo_domain(String logo_domain) {
        this.logo_domain = logo_domain;
    }

    public String getLogo_img_path() {
        return logo_img_path;
    }

    public void setLogo_img_path(String logo_img_path) {
        this.logo_img_path = logo_img_path;
    }


    @Override
    public String toString() {
        return "Company{" +
                "Logo_name='" + Logo_name + '\'' +
                ", logo_domain='" + logo_domain + '\'' +
                ", logo_img_path='" + logo_img_path + '\'' +
                '}';
    }
}
