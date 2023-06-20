/* File: LeaderboardDialog.java
 * Authors: Rafikov Rinat
 * Class, that creates dialog frame.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EventObject;

public class LeaderboardDialog extends JDialog {
    private JButton loadLeaderboardButton;
    private JButton exportLeaderboardButton;
    private JButton editUsernameButton;
    private JTable table = new JTable();
    private GameWindow gameWindow;
    private DefaultTableModel tableModel;

    public LeaderboardDialog(GameWindow frame, String title, boolean modal) {
        super(frame, title, modal);
        this.gameWindow = frame;
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        initializeComponents();
        createLayout();
    }

    private void initializeComponents() {
        loadLeaderboardButton = new JButton("Load Leaderboard");
        loadLeaderboardButton.setBackground(Color.BLACK);
        loadLeaderboardButton.setForeground(Color.WHITE);
        loadLeaderboardButton.setBorder(new LineBorder(Color.WHITE, 2, true));
        loadLeaderboardButton.setPreferredSize(new Dimension(100, 30));

        loadLeaderboardButton.addActionListener(e -> {
            loadLeaderboardArray();
        });

        exportLeaderboardButton = new JButton("Export Leaderboard");
        exportLeaderboardButton.setBackground(Color.BLACK);
        exportLeaderboardButton.setForeground(Color.WHITE);
        exportLeaderboardButton.setBorder(new LineBorder(Color.WHITE, 2, true));
        exportLeaderboardButton.setPreferredSize(new Dimension(100, 30));

        exportLeaderboardButton.addActionListener(e -> {
            exportLeaderboardArray();
        });

        editUsernameButton = new JButton("Edit Username");
        editUsernameButton.setBackground(Color.BLACK);
        editUsernameButton.setForeground(Color.WHITE);
        editUsernameButton.setBorder(new LineBorder(Color.WHITE, 2, true));
        editUsernameButton.setPreferredSize(new Dimension(100, 30));

        editUsernameButton.addActionListener(e -> {
            String optionPane;
            do {
                optionPane = JOptionPane.showInputDialog("Enter new username: ");
                optionPane = optionPane.trim();
                if(optionPane.length() < 5) {
                    JOptionPane.showMessageDialog(null, "Minimum length for a username is 5", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } while(optionPane.length() < 5);
            gameWindow.leaderboard.setCurrentName(optionPane);
        });
    }

    private void loadLeaderboardArray() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Object File");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("DarkStack Leaderboard", "dslb");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();

            try {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                ArrayList<RoundStats> object = new ArrayList<>((ArrayList<RoundStats>) objectInputStream.readObject());

                objectInputStream.close();
                fileInputStream.close();

                gameWindow.leaderboard.setLeaderboard(object);
                loadTableData(gameWindow.leaderboard.getTableData());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to read object.");
            }
        }
    }

    private void exportLeaderboardArray() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Object");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("DarkStack Leaderboard", "dslb");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();

            if (!filePath.toLowerCase().endsWith(".dslb")) {
                filePath += ".dslb";
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                ArrayList<RoundStats> object = new ArrayList<>(gameWindow.leaderboard.getLeaderboard());
                objectOutputStream.writeObject(object);

                objectOutputStream.close();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to save object.");
            }
        }
    }

    private void createLayout() {
        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setForeground(Color.WHITE);
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);
        topPanel.setForeground(Color.WHITE);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(editUsernameButton);

        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setForeground(Color.WHITE);
        centerPanel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Player", "Points", "Multiplier", "Time", "Round Settings"}, 0);
        table.setModel(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setForeground(Color.WHITE);
        bottomPanel.setLayout(new GridLayout(1, 2, 10, 0)); // 1 row, 2 columns, spacing between buttons

        bottomPanel.add(loadLeaderboardButton);
        bottomPanel.add(exportLeaderboardButton);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        this.add(contentPane);
        pack();
        setLocationRelativeTo(gameWindow);

        TableColumn column = table.getColumnModel().getColumn(4);
        column.setCellRenderer(new ButtonCellRenderer());
        column.setCellEditor(new ButtonCellEditor());

        int minimumWidth = 600;
        int minimumHeight = 450;
        Dimension minimumSize = new Dimension(minimumWidth, minimumHeight);
        this.setMinimumSize(minimumSize);
        this.setPreferredSize(minimumSize);
    }

    public void loadTableData(Object[][] data){
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private class ButtonCellRenderer extends JButton implements TableCellRenderer {
        public ButtonCellRenderer() {
            setOpaque(true);
            setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Settings");
            return this;
        }
    }

    private class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;

        public ButtonCellEditor() {
            button = new JButton("Settings");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Object value = table.getValueAt(row, 0); // Get the value from the first column
                        if (value instanceof RoundStats currentStat) {
                            SettingsDialog settingsDialog = new SettingsDialog(gameWindow, "Round Settings of " + currentStat.playerName, true, currentStat.roundSettings);
                            settingsDialog.setVisible(true);
                        }
                    }
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
}

