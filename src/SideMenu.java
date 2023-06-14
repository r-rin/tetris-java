/* File: SideMenu.java
 * Authors: Rafikov Rinat, ?
 * Клас, який описує бічне меню, у якому будуть міститися різні кнопки, налаштування, панель вибору ускладнень тощо.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideMenu extends JPanel {

    GameWindow gameWindow;
    JSpinner nCols;
    JSpinner nRows;
    JSpinner nGridSize;
    JSpinner framesPerFall;
    JSpinner framesGap;
    public SideMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.setLayout(new GridBagLayout());
        this.setBorder(new LineBorder(Color.black, 1));

        initHolderPanel();
    }

    private void initHolderPanel() {
        JPanel holderPanel = new JPanel();
        holderPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        holderPanel.setLayout(new GridLayout(0, 1));

        holderPanel.add(new JLabel("BlackTetris"));

        JButton startRound = new JButton("START");
        startRound.setFocusable(false);
        startRound.addActionListener(e -> {
            try {
                setGivenSettings();
                gameWindow.wrapper.removeAll();
                gameWindow.tetrisField = new TetrisField(gameWindow.settings);
                gameWindow.wrapper.add(gameWindow.tetrisField);
                gameWindow.wrapper.revalidate();
                gameWindow.wrapper.repaint();
                gameWindow.pack();
                gameWindow.getTetrisField().gameHandler.restart(startRound);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        nCols = new JSpinner(new SpinnerNumberModel(8, 3, 100, 1));
        nCols.setFocusable(false);
        nRows = new JSpinner(new SpinnerNumberModel(16, 3, 100, 1));
        nRows.setFocusable(false);
        nGridSize = new JSpinner(new SpinnerNumberModel(40, 1, 500, 1));
        nGridSize.setFocusable(false);
        framesPerFall = new JSpinner(new SpinnerNumberModel(25, 1, 99999, 1));
        framesPerFall.setFocusable(false);
        framesGap = new JSpinner(new SpinnerNumberModel(0.1, 0.00001,10000, 0.00001));
        framesGap.setFocusable(false);

        holderPanel.add(startRound);
        holderPanel.add(new JLabel("Number of columns:"));
        holderPanel.add(nCols);
        holderPanel.add(new JLabel("Number of rows:"));
        holderPanel.add(nRows);
        holderPanel.add(new JLabel("Grid size:"));
        holderPanel.add(nGridSize);
        holderPanel.add(new JLabel("Frames for fall:"));
        holderPanel.add(framesPerFall);
        holderPanel.add(new JLabel("Seconds between frames:"));
        holderPanel.add(framesGap);


        this.add(holderPanel, new GridBagConstraints());
    }

    private void setGivenSettings() {
        gameWindow.settings.put(GameSettings.NROWS, Integer.valueOf(nRows.getValue().toString()));
        gameWindow.settings.put(GameSettings.NCOLUMNS, Integer.valueOf(nCols.getValue().toString()));
        gameWindow.settings.put(GameSettings.GRIDSIZE, Integer.valueOf(nGridSize.getValue().toString()));
        gameWindow.settings.put(GameSettings.FRAMEGAP, Double.valueOf(framesGap.getValue().toString()));
        gameWindow.settings.put(GameSettings.FRAMESFORFALL, Integer.valueOf(framesPerFall.getValue().toString()));
        gameWindow.settings.put(GameSettings.CANMOVEUP, true);
    }
}
