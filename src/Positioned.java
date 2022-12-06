import java.io.Serializable;

/**
 * A tile that also has a position (used for storing on board)
 *
 * @author Colin Mandeville, 101140289
 */
public record Positioned<T>(T value, Position pos) implements Serializable {
    public String toXML(int numTabs) {
        StringBuilder sb = new StringBuilder();
        StringBuilder tabs = new StringBuilder();
        tabs.append("    ".repeat(numTabs));
        sb.append(tabs).append("<Positioned>\n");
        sb.append(tabs).append("    ").append("<value value=\"").append(value).append("</value>\n");
        sb.append(pos.toXML(numTabs + 1));
        sb.append(tabs).append("</Positioned>\n");

        return sb.toString();
    }
}
