import java.util.Optional;
import java.util.Stack;

public class Transactionable<T> {

    public interface IClone<T> {
        T cloneObject(T obj);
    }

    protected int i = 0;
    protected final Stack<T> internalState;
    protected Optional<T> head;

    public Transactionable(T state) {
        this.internalState = new Stack<>() {{
            add(state);
        }};
        this.head = Optional.empty();
    }

    public boolean canRedo() {
        return (i + 1) < this.internalState.size();
    }

    public boolean redo() {
        var isRedo = canRedo();
        if (isRedo) {
            i++;
            this.head = Optional.empty();
        }
        return isRedo;
    }

    public boolean canUndo() {
        return (i - 1) >= 0;
    }

    public boolean undo() {
        var isUndo = canUndo();
        if (isUndo) {
            i--;
            this.head = Optional.empty();
        }
        return isUndo;
    }

    public T state(IClone<T> clone) {
        if (this.head.isEmpty()) {
            this.head = Optional.of(clone.cloneObject(this.internalState.get(i)));
        }
        return this.head.get();
    }

    public void commitWorking(IClone<T> clone) {
        var statesToDiscard = this.internalState.size() - (i + 1);
        for (int i = 0; i < statesToDiscard; i++) {
            this.internalState.pop();
        }

        this.internalState.push(clone.cloneObject(this.state(clone)));
        i = this.internalState.size() - 1;
        this.head = Optional.empty();
    }

    public void popCommitToWorking() {
        var head = this.internalState.pop();
        this.i--;
        this.head = Optional.of(head);
    }
}
