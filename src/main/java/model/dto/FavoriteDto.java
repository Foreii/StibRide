package model.dto;

public class FavoriteDto extends Dto<String> {
    private int id_origin;
    private int id_destination;

    public FavoriteDto(String key, int id_origin, int id_destination) {
        super(key);
        this.id_origin = id_origin;
        this.id_destination = id_destination;
    }

    public int getId_origin() {
        return id_origin;
    }

    public void setId_origin(int id_origin) {
        this.id_origin = id_origin;
    }

    public int getId_destination() {
        return id_destination;
    }

    public void setId_destination(int id_destination) {
        this.id_destination = id_destination;
    }
}
