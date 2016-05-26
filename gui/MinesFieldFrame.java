/**
    Minesweeper game.
    
    @author Andrey Ivanov
    @version 1.0
*/

package minesweeper.gui;

import minesweeper.resources.*;
import minesweeper.model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.Container;
import java.awt.Font;


/**
    Class is intended to create a main windows and menu items of the game.
*/

public class MinesFieldFrame extends JFrame implements ActionListener {
    
    private int rowCount = 9;
    private int colCount = 9;
    private int minesCount = 4;
    private FieldPanel minesPanel;
    private final JButton centralButton;
    
    private final CustomLevel customDialog;
    private final Container contentPanel;
    
    private final int CBUTTONWIDTH = 30;
    private final int CBUTTONHEIGHT = 30;
    
    private final JCheckBoxMenuItem mOpenedMove;
    private final JCheckBoxMenuItem mOpenRemaining;

    
    public MinesFieldFrame() {
        //Toolkit t = Toolkit.getDefaultToolkit();
        //setSize(t.getScreenSize().height/2, t.getScreenSize().width/2);
        
        setTitle("Minesweeper");
        
        // ---------- Create menu ----------
        
        Font font = new Font("Comics", Font.BOLD, 14);
        
        JMenuBar menuBar = new JMenuBar();
        
        JMenu mGame = new JMenu("Game");
        mGame.setMnemonic(KeyEvent.VK_G);
        mGame.setFont(font);
        
        ButtonGroup bGroup = new ButtonGroup();
        
        JMenuItem mNewGame = newMenuItem("New Game");
        mNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.ALT_MASK));
        
        JMenuItem mExit = newMenuItem("Exit");
        mExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        
        mGame.add(mNewGame);
        mGame.addSeparator();
        mGame.add(newRadioButtonItem("Beginner", true, KeyEvent.VK_B, bGroup));
        mGame.add(newRadioButtonItem("Intermediate", false, KeyEvent.VK_I, bGroup));
        mGame.add(newRadioButtonItem("Expert", false, KeyEvent.VK_E, bGroup));
        mGame.add(newRadioButtonItem("Custom", false, KeyEvent.VK_C, bGroup));
        mGame.addSeparator();
        mGame.add(mExit);

        
        JMenu mOptions = new JMenu("Options");
        mOptions.setMnemonic(KeyEvent.VK_O);
        mOptions.setFont(font);
        
        mOpenedMove = new JCheckBoxMenuItem("Opening move");
        mOpenedMove.setMnemonic(KeyEvent.VK_M);
        mOpenedMove.setToolTipText("The first move always open a useful series of squares.");
        
        
        mOpenRemaining = new JCheckBoxMenuItem("Open remaining");
        mOpenRemaining.setMnemonic(KeyEvent.VK_R);
        mOpenRemaining.setToolTipText("When 0 bombs are left unmarked, click the bomb counter 000 to open all remaining");

        mOptions.add(mOpenedMove);
        mOptions.add(mOpenRemaining);
        
        
        JMenu mHelp = new JMenu("Help");
        mHelp.setMnemonic(KeyEvent.VK_H);
        mHelp.setFont(font);
        
        JMenuItem mAbout = new JMenuItem("About");
        mAbout.setMnemonic(KeyEvent.VK_A);
        mAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        mAbout.setActionCommand("About");
        mAbout.addActionListener(this);
        mHelp.add(mAbout);
   
        
        menuBar.add(mGame);
        menuBar.add(mOptions);
        menuBar.add(mHelp);
        
        setJMenuBar(menuBar);
        
        // --------------------------------------------------------------------------------
        
        // ---------- Create a central button ----------
        centralButton = new JButton();
        centralButton.setPreferredSize(new Dimension(CBUTTONWIDTH, CBUTTONHEIGHT));
        centralButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        centralButton.setActionCommand("New Game");
        centralButton.addActionListener(this);
        // --------------------------------------------------------------------------------
        
        setLocationByPlatform(true);
        setResizable(false);
       
        customDialog = new CustomLevel(this);
        
        contentPanel = getContentPane();
        createNewGame();
    }
    
    private JMenuItem newMenuItem(String title) {
        JMenuItem mItem = new JMenuItem(title);
        mItem.setActionCommand(title);
        mItem.addActionListener(this);
        return mItem;
    }
    
    private JRadioButtonMenuItem newRadioButtonItem(String title, Boolean selected, int key,
                                                    ButtonGroup group) {
        JRadioButtonMenuItem rItem = new JRadioButtonMenuItem(title, selected);
        rItem.setActionCommand(title);
        rItem.addActionListener(this);
        rItem.setMnemonic(key);
        group.add(rItem);
        return rItem;
    }
    
    private void createNewGame() {
        contentPanel.removeAll();
        minesPanel = new FieldPanel(rowCount, colCount, minesCount, centralButton, 
                                    mOpenedMove.isSelected(), mOpenRemaining.isSelected());
        contentPanel.add(minesPanel, BorderLayout.SOUTH);
        pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        switch (e.getActionCommand()) {
        
            case "New Game":     createNewGame();
                                 break;
            
            case "Beginner":     rowCount = 9;
                                 colCount = 9;
                                 minesCount = 4;
                                 createNewGame();
                                 break;
                             
            case "Intermediate": rowCount = 16;
                                 colCount = 16;
                                 minesCount = 40;
                                 createNewGame();
                                 break;

            case "Expert":       rowCount = 40;
                                 colCount = 60;
                                 minesCount = 256;
                                 createNewGame();
                                 break;
            
            case "Custom":       customDialog.showDialog();
                                 createNewGame();
                                 break;
                           
            case "About":        JOptionPane.showMessageDialog(this, "The purpose of this game is to open all\n" +
                                                                     "the cells which do not contain a bomb.\n" +
                                                                     "You lose if you open a bomb cell.\n\n\n" +
                                                                     "P.S. Nice to have the following:\n" +
                                                                     "1. Keep records\n" + 
                                                                     "2. Ability to save the game\n" +
                                                                     "3. Automatic solver\n" +
                                                                     "4. Checking for 9 cells\n",
                                                                     "Help", JOptionPane.PLAIN_MESSAGE);
                                 break;

            case "Exit":         System.exit(0);

        }
    }
    
    
    private class CustomLevel extends JDialog {
        
        private final int DEFAULT_WIDTH = 300;
        private final int DEFAULT_HEIGHT = 200;
        private final int MAX_ROWS = 100;
        private final int MAX_COLS = 100;
        private final int MAX_MINES = MAX_ROWS * MAX_COLS / 3;
        private final JFrame owner;
        
        private JSpinner rowSpinner;
        private JSpinner colSpinner;
        private JSpinner mineSpinner;
        
        public CustomLevel(JFrame owner) {
            super(owner, true);
            this.owner = owner;
            setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.PAGE_AXIS));
            
            rowSpinner = new JSpinner(new SpinnerNumberModel());
            colSpinner = new JSpinner(new SpinnerNumberModel());
            mineSpinner = new JSpinner(new SpinnerNumberModel());
            
            dialogPanel.add(rowSpinner);
            dialogPanel.add(colSpinner);
            dialogPanel.add(mineSpinner);
            
            JPanel dialogButtons = new JPanel();
            
            JButton ok = new JButton("OK");
            JButton cancel = new JButton("Cancel");
            
            dialogButtons.add(ok);
            dialogButtons.add(cancel);
            
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    int _rows = (Integer) rowSpinner.getValue();
                    int _cols = (Integer) colSpinner.getValue();
                    int _mines = (Integer) mineSpinner.getValue();
                    
                    if (_rows > MAX_ROWS || _cols > MAX_COLS || _mines > MAX_MINES) {
                        JOptionPane.showMessageDialog(owner, "Please select values from the following ranges:\nrows 1 - " + MAX_ROWS +
                                                      "\ncolumns 1 - " + MAX_COLS + "\nmines 1 - " + MAX_MINES);
                    } else if (_mines > (_rows * _cols/3)) {
                        JOptionPane.showMessageDialog(owner, "Max value of mines should be less then 1/3 of row*columns");
                    } else {
                        rowCount = _rows;
                        colCount = _cols;
                        minesCount = _mines;
                        setVisible(false); 
                    }
                }
            });
            
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    setVisible(false);
                }
            });
            
            add(dialogPanel, BorderLayout.NORTH);
            add(dialogButtons, BorderLayout.SOUTH);
            setTitle("Set custom values");
            pack();
            
        }
        
        public void showDialog() {
 
            rowSpinner.setValue(rowCount);
            colSpinner.setValue(colCount);
            mineSpinner.setValue(minesCount);
            setVisible(true);
        }
        
    }

}