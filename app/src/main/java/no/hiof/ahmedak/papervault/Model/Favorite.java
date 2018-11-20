package no.hiof.ahmedak.papervault.Model;

public class Favorite {
    private Boolean liked;

    public Favorite(Boolean liked) {
        this.liked = liked;
    }
    public Favorite() {
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "liked=" + liked +
                '}';
    }
}
