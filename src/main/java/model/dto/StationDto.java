package model.dto;

public class StationDto extends Dto<Integer> {
    private String name;

    public StationDto(Integer key, String name) {
        super(key);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" + name + ", " + key + '}';
    }
}
