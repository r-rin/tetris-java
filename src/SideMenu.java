/* File: SideMenu.java
 * Authors: Rafikov Rinat, ?
 * Клас, який описує бічне меню, у якому будуть міститися різні кнопки, налаштування, панель вибору ускладнень тощо.
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideMenu extends JPanel {

    GameWindow gameWindow;
    public SideMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.setLayout(new GridBagLayout());
        this.setBorder(new LineBorder(Color.black, 1));

        initHolderPanel();
    }

    private void initHolderPanel() {
        JPanel holderPanel = new JPanel();

        holderPanel.add(new JLabel("BlackTetris"));

        JButton startRound = new JButton("START");
        startRound.setFocusable(false);
        startRound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gameWindow.getTetrisField().gameHandler.run();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        holderPanel.add(startRound);

        this.add(holderPanel, new GridBagConstraints());
    }
}
