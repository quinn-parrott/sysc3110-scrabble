import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TileTrayView extends JPanel {
    // TODO: Dedup this
    private static final Color colorUnselected = new Color(240, 240, 240);
    private static final Color colorSelected = new Color(200, 200, 200);

    public TileTrayModel model;
    public List<JButton> buttons;

    public TileTrayView(TileTrayModel model) {
        super(new GridLayout(1, model.getEntries().size()));
        this.model = model;
        this.setPreferredSize(new Dimension(500, 50));

        this.buttons = new ArrayList<>();

        int i = 0;
        for (TileTrayModel.TileTrayEntry entry : model.getEntries()) {
            JButton button = new JButton(String.format("%c tile", entry.tile().chr()));
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
            i++;
        }
    }

    private void update() {
        int i = 0;
        for (TileTrayModel.TileTrayEntry entry : model.getEntries()) {
            var button = this.buttons.get(i);
            button.setText(String.format("%c tile", entry.tile().chr()));
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
            i++;
        }
    }

}
