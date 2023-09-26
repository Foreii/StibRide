package model.dto;

public class LineDto extends Dto<Integer> {
    public LineDto(Integer key) {
        super(key);
    }

    @Override
    public String toString() {
        return "{" + key + '}';
    }
}
