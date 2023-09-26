package model.dto;

public class StopDto extends Dto<Composed> {
    private int id_order;

    public StopDto(Composed key, int id_order) {
        super(key);
        this.id_order = id_order;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    @Override
    public String toString() {
        return "{" + key.getFirst() + ", " + key.getSecond() + ", " + id_order + '}';
    }
}
