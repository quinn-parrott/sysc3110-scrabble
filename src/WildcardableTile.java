import java.io.Serializable;

/**
 * The representation of a single tile but can also be a wildcard
 *
 * @author Quinn Parrott, 101169535
 */
public record WildcardableTile(char chr, int pointValue) implements Serializable {
    public static final char WILDCARD_CHAR = '&';

    public boolean isWildcard() {
        return WILDCARD_CHAR == chr();
    }

    public String toXML(int numTabs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numTabs; i++) {
            sb.append("    ");
        }
        sb.append("<WildcardableTile chr=\"").append(chr).append("\" pointValue=\"").append(pointValue).append("\"></WildcardableTile>\n");
        return sb.toString();
    }
}
