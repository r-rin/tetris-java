import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class SettingsDialog extends JDialog {

    HashMap<GameSettings, Object> roundSettings;

    private JSpinner nCols;
    private JSpinner nRows;
    private JSpinner nGridSize;
    private JSpinner framesPerFall;
    private JSpinner framesGap;
    private JCheckBox drawGridNet;
    private JCheckBox canMoveUp;
    private JCheckBox limitedVision;
    private JSpinner visionDistance;
    private JCheckBox shuffledControls;
    private JCheckBox doRandomMoves;
    private JSpinner eachNFrameRandMove;
    private JCheckBox disableUpMoves;
    private GameWindow gameWindow;
    private boolean buttonPressed = false;

    public SettingsDialog(GameWindow frame, String title, boolean locked) {
        super(frame, title, locked);
        this.gameWindow = frame;

        JPanel holderPanel = new JPanel();
        holderPanel.setBackground(Color.BLACK);
        holderPanel.setLayout(new BorderLayout());
        holderPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        holderPanel.add(loadSettingsPanels(), BorderLayout.CENTER);

        JButton submitButton = new JButton("Save");
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(new LineBorder(Color.WHITE, 2));
        submitButton.setPreferredSize(new Dimension(100, 40));
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
                    gameWindow.settings.put(GameSettings.LIMITEDVISION, limitedVision.isSelected());
                    gameWindow.settings.put(GameSettings.VISIONDISTANCE, visionDistance.getValue());
                    gameWindow.settings.put(GameSettings.CONTROLSSHUFFLE, shuffledControls.isSelected());
                    gameWindow.settings.put(GameSettings.RANDOMMOVES, doRandomMoves.isSelected());
                    gameWindow.settings.put(GameSettings.RANDOMMOVEEACHNFRAME, eachNFrameRandMove.getValue());
                    gameWindow.settings.put(GameSettings.DISABLEANYUPMOVES, disableUpMoves.isSelected());
                }
                gameWindow.sideMenu.scoreCounter.countMultiplier(gameWindow.settings);
            }
        });

        this.add(holderPanel);
        this.pack();
        this.setResizable(false);
    }

    public SettingsDialog(GameWindow frame, String title, boolean locked, HashMap<GameSettings, Object> roundSettings) {
        super(frame, title, locked);
        this.gameWindow = frame;
        this.roundSettings = roundSettings;

        JPanel holderPanel = new JPanel();
        holderPanel.setBackground(Color.BLACK);
        holderPanel.setLayout(new BorderLayout());
        holderPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        holderPanel.add(loadSettingsPanelsCopy(), BorderLayout.CENTER);

        JButton submitButton = new JButton("Copy");
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(new LineBorder(Color.WHITE, 2));
        submitButton.setPreferredSize(new Dimension(100, 40));
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
                    gameWindow.settings = new HashMap<>(roundSettings);
                }
                gameWindow.sideMenu.scoreCounter.countMultiplier(gameWindow.settings);
            }
        });

        this.add(holderPanel);
        this.pack();
        this.setResizable(false);
    }

    private JPanel loadSettingsPanelsCopy() {
        JPanel holderPanel = new JPanel();
        holderPanel.setBackground(Color.BLACK);
        holderPanel.setLayout(new GridLayout(1, 0));
        holderPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.BLACK);
        settingsPanel.setLayout(new BorderLayout());
        settingsPanel.setBorder(new LineBorder(Color.WHITE, 2));

        JLabel settingsTitle = new JLabel("SETTINGS");
        settingsTitle.setBackground(Color.BLACK);
        settingsTitle.setForeground(Color.WHITE);
        settingsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        settingsTitle.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));

        settingsPanel.add(settingsTitle, BorderLayout.NORTH);

        JPanel settingsPanelCenter = new JPanel();
        settingsPanelCenter.setBackground(Color.BLACK);
        settingsPanelCenter.setLayout(new GridLayout(0, 2));

        HashMap<GameSettings, Object> settings = roundSettings;

        nCols = createSpinner((int) settings.get(GameSettings.NCOLUMNS), 3, 500);
        nCols.setBackground(Color.BLACK);
        nCols.setForeground(Color.WHITE);
        nRows = createSpinner((int) settings.get(GameSettings.NROWS), 3, 500);
        nRows.setBackground(Color.BLACK);
        nRows.setForeground(Color.WHITE);
        nGridSize = createSpinner((int) settings.get(GameSettings.GRIDSIZE), 1, 500);
        nGridSize.setBackground(Color.BLACK);
        nGridSize.setForeground(Color.WHITE);
        framesPerFall = createSpinner((int) settings.get(GameSettings.FRAMESFORFALL), 1, 999999);
        framesPerFall.setBackground(Color.BLACK);
        framesPerFall.setForeground(Color.WHITE);
        framesGap = createSpinner((int) settings.get(GameSettings.FRAMEGAP), 1, 999999);
        framesGap.setBackground(Color.BLACK);
        framesGap.setForeground(Color.WHITE);

        settingsPanelCenter.add(generateLabel("Number of columns: "));
        settingsPanelCenter.add(nCols);

        settingsPanelCenter.add(generateLabel("Number of rows: "));
        settingsPanelCenter.add(nRows);

        settingsPanelCenter.add(generateLabel("Grid size: "));
        settingsPanelCenter.add(nGridSize);

        settingsPanelCenter.add(generateLabel("Frames for fall: "));
        settingsPanelCenter.add(framesPerFall);

        settingsPanelCenter.add(generateLabel("Seconds between frames: "));
        settingsPanelCenter.add(framesGap);

        drawGridNet = new JCheckBox();
        drawGridNet.setSelected((boolean) settings.get(GameSettings.DRAWGRIDNET));

        settingsPanelCenter.add(generateLabel("Draw grid net: "));
        settingsPanelCenter.add(drawGridNet);

        settingsPanel.add(settingsPanelCenter, BorderLayout.CENTER);
        holderPanel.add(settingsPanel);

        JPanel modifiersPanel = new JPanel();
        modifiersPanel.setBackground(Color.BLACK);
        modifiersPanel.setForeground(Color.WHITE);
        modifiersPanel.setLayout(new BorderLayout());
        modifiersPanel.setBorder(new LineBorder(Color.WHITE, 2));

        JLabel modifiersTitle = new JLabel("MODIFIERS");
        modifiersTitle.setBackground(Color.BLACK);
        modifiersTitle.setForeground(Color.WHITE);
        modifiersTitle.setHorizontalAlignment(SwingConstants.CENTER);
        modifiersTitle.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));

        modifiersPanel.add(modifiersTitle, BorderLayout.NORTH);

        JPanel modifiersPanelCenter = new JPanel();
        modifiersPanelCenter.setBackground(Color.BLACK);
        modifiersPanelCenter.setForeground(Color.WHITE);
        modifiersPanelCenter.setLayout(new GridLayout(0, 2));

        canMoveUp = new JCheckBox();
        canMoveUp.setAlignmentX(Component.CENTER_ALIGNMENT);
        canMoveUp.setSelected((boolean) settings.get(GameSettings.CANMOVEUP));

        limitedVision = new JCheckBox();
        limitedVision.setAlignmentX(Component.CENTER_ALIGNMENT);
        limitedVision.setSelected((boolean) settings.get(GameSettings.LIMITEDVISION));

        visionDistance = new JSpinner(new SpinnerNumberModel((double) settings.get(GameSettings.VISIONDISTANCE), 0.25, 500, 0.25));
        visionDistance.setBackground(Color.BLACK);
        visionDistance.setForeground(Color.WHITE);

        shuffledControls = new JCheckBox();
        shuffledControls.setAlignmentX(Component.CENTER_ALIGNMENT);
        shuffledControls.setSelected((boolean) settings.get(GameSettings.CONTROLSSHUFFLE));

        doRandomMoves = new JCheckBox();
        doRandomMoves.setAlignmentX(Component.CENTER_ALIGNMENT);
        doRandomMoves.setSelected((boolean) settings.get(GameSettings.RANDOMMOVES));

        eachNFrameRandMove = new JSpinner(new SpinnerNumberModel((int) settings.get(GameSettings.RANDOMMOVEEACHNFRAME), 1, 999999, 1));
        eachNFrameRandMove.setBackground(Color.BLACK);
        eachNFrameRandMove.setForeground(Color.WHITE);

        disableUpMoves = new JCheckBox();
        disableUpMoves.setAlignmentX(Component.CENTER_ALIGNMENT);
        disableUpMoves.setSelected((boolean) settings.get(GameSettings.DISABLEANYUPMOVES));

        modifiersPanelCenter.add(generateLabel("Can move up: "));
        modifiersPanelCenter.add(canMoveUp);

        modifiersPanelCenter.add(generateLabel("Limited vision: "));
        modifiersPanelCenter.add(limitedVision);

        modifiersPanelCenter.add(generateLabel("Vision distance (in grids): "));
        modifiersPanelCenter.add(visionDistance);

        modifiersPanelCenter.add(generateLabel("Shuffled controls:"));
        modifiersPanelCenter.add(shuffledControls);

        modifiersPanelCenter.add(generateLabel("Random figure moves: "));
        modifiersPanelCenter.add(doRandomMoves);

        modifiersPanelCenter.add(generateLabel("Amount of frames for rand. move: "));
        modifiersPanelCenter.add(eachNFrameRandMove);

        modifiersPanelCenter.add(generateLabel("Disable any up moves: "));
        modifiersPanelCenter.add(disableUpMoves);

        modifiersPanel.add(modifiersPanelCenter, BorderLayout.CENTER);
        holderPanel.add(modifiersPanel);

        nCols.setEnabled(false);
        nRows.setEnabled(false);
        nGridSize.setEnabled(false);
        framesPerFall.setEnabled(false);
        framesGap.setEnabled(false);
        drawGridNet.setEnabled(false);
        canMoveUp.setEnabled(false);
        limitedVision.setEnabled(false);
        visionDistance.setEnabled(false);
        shuffledControls.setEnabled(false);
        doRandomMoves.setEnabled(false);
        eachNFrameRandMove.setEnabled(false);
        disableUpMoves.setEnabled(false);

        return holderPanel;
    }

    private JPanel loadSettingsPanels() {
        JPanel holderPanel = new JPanel();
        holderPanel.setBackground(Color.BLACK);
        holderPanel.setLayout(new GridLayout(1, 0));
        holderPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.BLACK);
        settingsPanel.setLayout(new BorderLayout());
        settingsPanel.setBorder(new LineBorder(Color.WHITE, 2));

        JLabel settingsTitle = new JLabel("SETTINGS");
        settingsTitle.setBackground(Color.BLACK);
        settingsTitle.setForeground(Color.WHITE);
        settingsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        settingsTitle.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));

        settingsPanel.add(settingsTitle, BorderLayout.NORTH);

        JPanel settingsPanelCenter = new JPanel();
        settingsPanelCenter.setBackground(Color.BLACK);
        settingsPanelCenter.setLayout(new GridLayout(0, 2));

        HashMap<GameSettings, Object> settings = gameWindow.settings;

        nCols = createSpinner((int) settings.get(GameSettings.NCOLUMNS), 3, 500);
        nCols.setBackground(Color.BLACK);
        nCols.setForeground(Color.WHITE);
        nRows = createSpinner((int) settings.get(GameSettings.NROWS), 3, 500);
        nRows.setBackground(Color.BLACK);
        nRows.setForeground(Color.WHITE);
        nGridSize = createSpinner((int) settings.get(GameSettings.GRIDSIZE), 1, 500);
        nGridSize.setBackground(Color.BLACK);
        nGridSize.setForeground(Color.WHITE);
        framesPerFall = createSpinner((int) settings.get(GameSettings.FRAMESFORFALL), 1, 999999);
        framesPerFall.setBackground(Color.BLACK);
        framesPerFall.setForeground(Color.WHITE);
        framesGap = createSpinner((int) settings.get(GameSettings.FRAMEGAP), 1, 999999);
        framesGap.setBackground(Color.BLACK);
        framesGap.setForeground(Color.WHITE);

        settingsPanelCenter.add(generateLabel("Number of columns: "));
        settingsPanelCenter.add(nCols);

        settingsPanelCenter.add(generateLabel("Number of rows: "));
        settingsPanelCenter.add(nRows);

        settingsPanelCenter.add(generateLabel("Grid size: "));
        settingsPanelCenter.add(nGridSize);

        settingsPanelCenter.add(generateLabel("Frames for fall: "));
        settingsPanelCenter.add(framesPerFall);

        settingsPanelCenter.add(generateLabel("Seconds between frames: "));
        settingsPanelCenter.add(framesGap);

        drawGridNet = new JCheckBox();
        drawGridNet.setSelected((boolean) settings.get(GameSettings.DRAWGRIDNET));

        settingsPanelCenter.add(generateLabel("Draw grid net: "));
        settingsPanelCenter.add(drawGridNet);

        settingsPanel.add(settingsPanelCenter, BorderLayout.CENTER);
        holderPanel.add(settingsPanel);

        JPanel modifiersPanel = new JPanel();
        modifiersPanel.setBackground(Color.BLACK);
        modifiersPanel.setForeground(Color.WHITE);
        modifiersPanel.setLayout(new BorderLayout());
        modifiersPanel.setBorder(new LineBorder(Color.WHITE, 2));

        JLabel modifiersTitle = new JLabel("MODIFIERS");
        modifiersTitle.setBackground(Color.BLACK);
        modifiersTitle.setForeground(Color.WHITE);
        modifiersTitle.setHorizontalAlignment(SwingConstants.CENTER);
        modifiersTitle.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));

        modifiersPanel.add(modifiersTitle, BorderLayout.NORTH);

        JPanel modifiersPanelCenter = new JPanel();
        modifiersPanelCenter.setBackground(Color.BLACK);
        modifiersPanelCenter.setForeground(Color.WHITE);
        modifiersPanelCenter.setLayout(new GridLayout(0, 2));

        canMoveUp = new JCheckBox();
        canMoveUp.setAlignmentX(Component.CENTER_ALIGNMENT);
        canMoveUp.setSelected((boolean) settings.get(GameSettings.CANMOVEUP));

        limitedVision = new JCheckBox();
        limitedVision.setAlignmentX(Component.CENTER_ALIGNMENT);
        limitedVision.setSelected((boolean) settings.get(GameSettings.LIMITEDVISION));

        visionDistance = new JSpinner(new SpinnerNumberModel((double) settings.get(GameSettings.VISIONDISTANCE), 0.25, 500, 0.25));
        visionDistance.setBackground(Color.BLACK);
        visionDistance.setForeground(Color.WHITE);

        shuffledControls = new JCheckBox();
        shuffledControls.setAlignmentX(Component.CENTER_ALIGNMENT);
        shuffledControls.setSelected((boolean) settings.get(GameSettings.CONTROLSSHUFFLE));

        doRandomMoves = new JCheckBox();
        doRandomMoves.setAlignmentX(Component.CENTER_ALIGNMENT);
        doRandomMoves.setSelected((boolean) settings.get(GameSettings.RANDOMMOVES));

        eachNFrameRandMove = new JSpinner(new SpinnerNumberModel((int) settings.get(GameSettings.RANDOMMOVEEACHNFRAME), 1, 999999, 1));
        eachNFrameRandMove.setBackground(Color.BLACK);
        eachNFrameRandMove.setForeground(Color.WHITE);

        disableUpMoves = new JCheckBox();
        disableUpMoves.setAlignmentX(Component.CENTER_ALIGNMENT);
        disableUpMoves.setSelected((boolean) settings.get(GameSettings.DISABLEANYUPMOVES));

        modifiersPanelCenter.add(generateLabel("Can move up: "));
        modifiersPanelCenter.add(canMoveUp);

        modifiersPanelCenter.add(generateLabel("Limited vision: "));
        modifiersPanelCenter.add(limitedVision);

        modifiersPanelCenter.add(generateLabel("Vision distance (in grids): "));
        modifiersPanelCenter.add(visionDistance);

        modifiersPanelCenter.add(generateLabel("Shuffled controls:"));
        modifiersPanelCenter.add(shuffledControls);

        modifiersPanelCenter.add(generateLabel("Random figure moves: "));
        modifiersPanelCenter.add(doRandomMoves);

        modifiersPanelCenter.add(generateLabel("Amount of frames for rand. move: "));
        modifiersPanelCenter.add(eachNFrameRandMove);

        modifiersPanelCenter.add(generateLabel("Disable any up moves: "));
        modifiersPanelCenter.add(disableUpMoves);

        modifiersPanel.add(modifiersPanelCenter, BorderLayout.CENTER);
        holderPanel.add(modifiersPanel);

        return holderPanel;
    }

    private JLabel generateLabel(String title) {
        JLabel label = new JLabel(title);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JSpinner createSpinner(int value, int min, int max) {
        return new JSpinner(new SpinnerNumberModel(value, min, max, 1));
    }
}
