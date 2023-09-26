package model.dto;

public class Composed {
    private Integer first;
    private Integer second;

    public Composed(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public Integer getFirst() {
        return first;
    }

    public Integer getSecond() {
        return second;
    }
}
