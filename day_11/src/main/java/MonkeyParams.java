import java.util.Arrays;
import java.util.function.Function;

public class MonkeyParams {

    private int id;
    private int[] items;

    private Function<Integer, Integer> worryFunction;

    private int divisor;

    private int trueTarget;
    private int falseTarget;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }

    public Function<Integer, Integer> getWorryFunction() {
        return worryFunction;
    }

    public void setWorryFunction(Function<Integer, Integer> worryFunction) {
        this.worryFunction = worryFunction;
    }

    public int getDivisor() {
        return divisor;
    }

    public void setDivisor(int divisor) {
        this.divisor = divisor;
    }

    public int getTrueTarget() {
        return trueTarget;
    }

    public void setTrueTarget(int trueTarget) {
        this.trueTarget = trueTarget;
    }

    public int getFalseTarget() {
        return falseTarget;
    }

    public void setFalseTarget(int falseTarget) {
        this.falseTarget = falseTarget;
    }

    @Override
    public String toString() {
        return "MonkeyParams{" +
                "id=" + id +
                ", items=" + Arrays.toString( items ) +
                ", worryFunction=" + worryFunction +
                ", divisor=" + divisor +
                ", trueTarget=" + trueTarget +
                ", falseTarget=" + falseTarget +
                '}';
    }
}
