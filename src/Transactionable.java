import java.util.EmptyStackException;
import java.util.Stack;

public class Transactionable<T> {

    protected int i = 0;
    protected final Stack<T> internalState;

    public Transactionable(T state) {
        this.internalState = new Stack<>() {{
            add(state);
        }};
    }

    public boolean canRedo() {
        return (i + 1) < this.internalState.size();
    }

    public boolean redo() {
        var isRedo = canRedo();
        if (isRedo) {
            i++;
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
        }
        return isUndo;
    }

    public T state() {
        return this.internalState.get(i);
    }

    public void pushCopy(T copy) {
        var statesToDiscard = this.internalState.size() - (i + 1);
        for (int i = 0; i < statesToDiscard; i++) {
            this.internalState.pop();
        }

        var head = this.internalState.pop();
        this.internalState.push(copy);
        this.internalState.push(head);
        i = this.internalState.size() - 1;
    }

    public void discardLastCommit() {
        var head = this.internalState.pop();

        var removed = false;
        try {
            this.internalState.pop();
            removed = true;
        } catch (EmptyStackException ignored) {}

        this.internalState.push(head);
        if (removed) {
            i--;
        }
    }
}
