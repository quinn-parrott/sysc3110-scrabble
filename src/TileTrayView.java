import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Component that implements the tile tray GUI element
 *
 * @author Quinn Parrott, 101169535
 */
public class TileTrayView extends JPanel {
    // TODO: Dedup this
    private static final Color colorUnselected = new Color(240, 240, 240);
    private static final Color colorSelected = new Color(200, 200, 200);

    public TileTrayModel model;
    public List<JButton> buttons;

    public TileTrayView(TileTrayModel model) {
        super(new GridLayout(1, Player.getTileHandSize()));
        this.model = model;
        this.setPreferredSize(new Dimension(500, 50));

        this.buttons = new ArrayList<>();

        for (int i = 0; i < Player.getTileHandSize(); i++) {
            JButton button = new JButton();
            if (i < model.getEntries().size()) {
                var entry = model.getEntries().get(i);
                button.setText(String.format("%c tile", entry.tile().chr()));
                button.setEnabled(true);
            } else {
                button.setText("-");
                button.setEnabled(false);
            }

            button.setBackground(colorUnselected);
            int finalI = i;
            button.addActionListener(event -> {
                var but = (JButton) event.getSource();

                var isSelected = but.getBackground().equals(colorSelected);

                // Reset all the buttons
                for (JButton b : buttons) {
                    b.setBackground(colorUnselected);
                }


                model.setSelected(isSelected ? Optional.empty() : Optional.of(finalI));

                but.setBackground(
                        isSelected ? colorUnselected : colorSelected
                );
            });
            buttons.add(button);
            this.add(button);
        }
    }

    public void update() {
        var entries = this.model.getEntries();
        for (int i = 0; i < buttons.size(); i++) {
            var button = this.buttons.get(i);
            if (i < entries.size()) {
                var entry = entries.get(i);
                button.setText(String.format("%c tile", entry.tile().chr()));
                button.setBackground(i == model.getSelected().orElse(-1) ? colorSelected : colorUnselected);
                button.setEnabled(entry.status().equals(TileTrayModel.TileStatus.Unplayed));
            } else {
                button.setText("-");
                button.setBackground(colorUnselected);
                button.setEnabled(false);
            }
        }
    }

}
