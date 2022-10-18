import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Colin Mandeville, 101140289
 */
public class TilePlacement {
    // This class represents a single player turn

    private final ArrayList<TilePositioned> tiles;
    private int score = 0;

    /**
     * Constructor for TilePlacement, Will handle placing word onto board in future
     *
     * @param tiles ArrayList of TilePositioned objects, which contain a Tile object, and a Position object
     * @author Colin Mandeville, 101140289
     */
    public TilePlacement(ArrayList<TilePositioned> tiles) {
        // TODO: Validate that all characters are in order
        this.tiles = tiles;
        for (TilePositioned tile : this.tiles) {

            // TODO Implement putting words onto board, need to wait on CLI and Game being implemented

            this.score += tile.tile().pointValue();
        }
    }

    /**
     * Construct a TilePlacement from a shorthand string format
     *
     * @param shorthand ex: "a1-a5;river", "a1:h;wh_ever"
     * @author Quinn Parrott, 101169535
     */
    public static Optional<TilePlacement> FromShorthand(String shorthand) {
        // TODO: Make a custom exception instead of using `Optional` that gives a breakdown of why parsing failed
        var split = shorthand.toUpperCase().split(";", 2);
        if (split.length != 2) {
            return Optional.empty();
        }

        var coordRaw = split[0];
        // TODO: Check that text is well-formed (no spaces and only valid letters)
        var word = split[1];

        var positions = new ArrayList<Position>(word.length());

        // Coordinates are in absolute position
        if (coordRaw.contains("-")) {
            var coordAbsolute = coordRaw.split("-", 2);

            var pos1 = Position.FromString(coordAbsolute[0]);
            var pos2 = Position.FromString(coordAbsolute[1]);
            if (pos1.isEmpty() || pos2.isEmpty()) {
                return Optional.empty();
            }

            var positionsOpt = Position.Interpolate(pos1.get(), pos2.get());
            if (positionsOpt.isEmpty()) {
                return Optional.empty();
            }

            positions.addAll(positionsOpt.get());
        }

        // Coordinates are in relative position
        if (coordRaw.contains(":")) {
            var coordRelative = coordRaw.split(":", 2);

            var pos1Opt = Position.FromString(coordRelative[0]);
            if (pos1Opt.isEmpty()) {
                return Optional.empty();
            }
            var pos1 = pos1Opt.get();

            var directionOpt = Direction.FromString(coordRelative[1]);
            if (directionOpt.isEmpty()) {
                return Optional.empty();
            }

            var pos2Opt = Position.FromInts(
                    directionOpt.get().equals(Direction.Horizontal) ? (pos1.getX() + word.length() - 1) : pos1.getX(),
                    directionOpt.get().equals(Direction.Vertical) ? (pos1.getY() + word.length() - 1) : pos1.getY()
            );
            if (pos2Opt.isEmpty()) {
                return Optional.empty();
            }

            var pos2 = pos2Opt.get();

            var positionsOpt = Position.Interpolate(pos1, pos2);
            if (positionsOpt.isEmpty()) {
                return Optional.empty();
            }


            positions.addAll(positionsOpt.get());
        }

        // Double check that length of the word the caller gave is right
        if (word.length() != positions.size()) {
            return Optional.empty();
        }

        // Create the tile placement by combining the letters with tile positions
        var tiles = new ArrayList<TilePositioned>(word.length());
        for (int i = 0; i < word.length(); i++) {
            char tileLetter = word.charAt(i);
            // TODO: Un-hardcode these letters
            if (tileLetter == '_' || tileLetter == '*' || tileLetter == ' ' || tileLetter == '-') {
                continue; // Skip empty letter slots
            }
            // TODO: Do something about the point value here so that it's not zero (maybe remove point from `Tile`?)
            tiles.add(new TilePositioned(new Tile(tileLetter, 0), positions.get(i)));
        }
        return Optional.of(new TilePlacement(tiles));
    }

    /**
     * @author Quinn Parrott, 101169535
     */
    @Override
    public String toString() {
        var builder = new StringBuilder();

        builder.append(this.tiles.get(0).pos().toString());
        builder.append('-');
        builder.append(this.tiles.get(this.tiles.size() - 1).pos().toString());
        builder.append(';');

        Position lastPosition = this.tiles.get(0).pos();
        for (var tile : this.tiles) {
            var pos = tile.pos();
            // TODO: Use `Position.distance` instead
            var delta = Math.abs(pos.getX() - lastPosition.getX()) + Math.abs(pos.getY() - lastPosition.getY());

            if (delta > 1) {
                // Fill in absent letters
                builder.append("_".repeat(delta - 1));
            }

            builder.append(tile.tile().chr());
            lastPosition = pos;
        }

        return builder.toString();
    }
}
