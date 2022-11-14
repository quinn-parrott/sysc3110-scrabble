import java.util.List;
import java.util.Optional;

/**
 * The model representing the current internal state of the TileTrayView
 *
 * @author Quinn Parrott, 101169535
 */
public class TileTrayModel {
    private List<TileTrayEntry> entries;
    private Optional<Integer> selected;

    public TileTrayModel(List<TileTrayEntry> entries, Optional<Integer> selected) {
        this.entries = entries;
        this.selected = selected;
    }

    public List<TileTrayEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TileTrayEntry> entries) {
        this.entries = entries;
    }

    public Optional<Integer> getSelected() {
        return selected;
    }

    public void setSelected(Optional<Integer> selected) {
        this.selected = selected;
    }

    public record TileTrayEntry(TileStatus status, Tile tile) {}
    public enum TileStatus {
        Unplayed,
        Played
    }
}
