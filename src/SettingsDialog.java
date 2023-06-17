import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class SettingsDialog extends JDialog {

    private JSpinner nCols;
    private JSpinner nRows;
    private JSpinner nGridSize;
    private JSpinner framesPerFall;
    private JSpinner framesGap;
    private JCheckBox drawGridNet;
    private JCheckBox canMoveUp;
    private GameWindow gameWindow;
    private boolean buttonPressed = false;

    public SettingsDialog(GameWindow frame, String title, boolean locked) {
        super(frame, title, locked);
        this.gameWindow = frame;

        JPanel holderPanel = new JPanel();
        holderPanel.setLayout(new BorderLayout());
        holderPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        holderPanel.add(loadSettingsPanels(), BorderLayout.CENTER);

        JButton submitButton = new JButton("Save");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            buttonPressed = true;
            dispose();
        });

        holderPanel.add(submitButton, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (buttonPressed) {
                    gameWindow.settings.put(GameSettings.NROWS, nRows.getValue());
                    gameWindow.settings.put(GameSettings.NCOLUMNS, nCols.getValue());
                    gameWindow.settings.put(GameSettings.GRIDSIZE, nGridSize.getValue());
                    gameWindow.settings.put(GameSettings.FRAMEGAP, framesGap.getValue());
                    gameWindow.settings.put(GameSettings.FRAMESFORFALL, framesPerFall.getValue());
                    gameWindow.settings.put(GameSettings.CANMOVEUP, canMoveUp.isSelected());
                    gameWindow.settings.put(GameSettings.DRAWGRIDNET, drawGridNet.isSelected());
                }
                gameWindow.sideMenu.scoreCounter.countMultiplier(gameWindow.settings);
            }
        });

        this.add(holderPanel);
        this.pack();
        this.setResizable(false);
    }

    private JPanel loadSettingsPanels() {
        JPanel holderPanel = new JPanel();
        holderPanel.setLayout(new GridLayout(1, 0));
        holderPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());
        settingsPanel.setBorder(new LineBorder(Color.BLACK, 2));

        JLabel settingsTitle = new JLabel("SETTINGS");
        settingsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        settingsTitle.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));

        settingsPanel.add(settingsTitle, BorderLayout.NORTH);

        JPanel settingsPanelCenter = new JPanel();
        settingsPanelCenter.setLayout(new GridLayout(0, 2));

        HashMap<GameSettings, Object> settings = gameWindow.settings;

        nCols = createSpinner((int) settings.get(GameSettings.NCOLUMNS), 3, 500);
        nRows = createSpinner((int) settings.get(GameSettings.NROWS), 3, 500);
        nGridSize = createSpinner((int) settings.get(GameSettings.GRIDSIZE), 1, 500);
        framesPerFall = createSpinner((int) settings.get(GameSettings.FRAMESFORFALL), 1, 999999);
        framesGap = createSpinner((int) settings.get(GameSettings.FRAMEGAP), 1, 999999);

        settingsPanelCenter.add(new JLabel("Number of columns:"), BorderLayout.CENTER);
        settingsPanelCenter.add(nCols, BorderLayout.CENTER);
        settingsPanelCenter.add(new JLabel("Number of rows:"), BorderLayout.CENTER);
        settingsPanelCenter.add(nRows, BorderLayout.CENTER);
        settingsPanelCenter.add(new JLabel("Grid size:"), BorderLayout.CENTER);
        settingsPanelCenter.add(nGridSize, BorderLayout.CENTER);
        settingsPanelCenter.add(new JLabel("Frames for fall:"), BorderLayout.CENTER);
        settingsPanelCenter.add(framesPerFall, BorderLayout.CENTER);
        settingsPanelCenter.add(new JLabel("Seconds between frames:"), BorderLayout.CENTER);
        settingsPanelCenter.add(framesGap, BorderLayout.CENTER);

        drawGridNet = new JCheckBox();
        drawGridNet.setSelected((boolean) settings.get(GameSettings.DRAWGRIDNET));

        settingsPanelCenter.add(new JLabel("Draw grid net: "));
        settingsPanelCenter.add(drawGridNet);

        settingsPanel.add(settingsPanelCenter, BorderLayout.CENTER);
        holderPanel.add(settingsPanel);

        JPanel modifiersPanel = new JPanel();
        modifiersPanel.setLayout(new BorderLayout());
        modifiersPanel.setBorder(new LineBorder(Color.BLACK, 2));

        JLabel modifiersTitle = new JLabel("MODIFIERS");
        modifiersTitle.setHorizontalAlignment(SwingConstants.CENTER);
        modifiersTitle.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));

        modifiersPanel.add(modifiersTitle, BorderLayout.NORTH);

        JPanel modifiersPanelCenter = new JPanel();
        modifiersPanelCenter.setLayout(new GridLayout(0, 2));

        canMoveUp = new JCheckBox();
        canMoveUp.setSelected((boolean) settings.get(GameSettings.CANMOVEUP));

        modifiersPanelCenter.add(new JLabel("Can move up: "));
        modifiersPanelCenter.add(canMoveUp);

        modifiersPanel.add(modifiersPanelCenter, BorderLayout.CENTER);
        holderPanel.add(modifiersPanel);

        return holderPanel;
    }

    private JSpinner createSpinner(int value, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, 1));
    }
}
