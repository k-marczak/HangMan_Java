import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Hangman extends JFrame implements ActionListener     {
    // counts the number of incorrect guesses player has made.
    private int incorrectGuesses;

    // store the challenge from the wordDB
    private String[] wordChallenge;

    private final WordDB wordDB;
    private JLabel hangmanImage;
    private JLabel categoryLabel;
    private JLabel hiddenWordLabel;
    private JButton[] letterButtons;



    private JDialog resultDialog;
    private JLabel resultLabel, wordLabel;

    private Font customFont;






    public Hangman() {
        super("Hangman Game");
        setSize(CommonConstants.FRAME_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);

        // init vars
        wordDB = new WordDB();
        letterButtons = new JButton[26];
        wordChallenge = wordDB.loadChallenge();
        customFont = CustomTools.createFont(CommonConstants.FONT_PATH);
        createResultDialog();


        addGuiComp();

    }


    private void addGuiComp(){

        // hangman image
        hangmanImage = CustomTools.loadImage(CommonConstants.IMAGE_PATH);
        hangmanImage.setBounds(0, 0, hangmanImage.getPreferredSize().width, hangmanImage.getPreferredSize().height);


        // category display
        categoryLabel = new JLabel(wordChallenge[0]);
        categoryLabel.setFont(customFont.deriveFont(30f));
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        categoryLabel.setOpaque(true);
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setBackground(CommonConstants.SECONDARY_COLOR);
        categoryLabel.setBorder(BorderFactory.createLineBorder(CommonConstants.SECONDARY_COLOR));
        categoryLabel.setBounds(
                0,
                hangmanImage.getPreferredSize().height - 28,
                CommonConstants.FRAME_SIZE.width,
                categoryLabel.getPreferredSize().height);





        //hidden word
        hiddenWordLabel = new JLabel(CustomTools.hideWords(wordChallenge[1]));
        hiddenWordLabel.setFont(customFont.deriveFont(64f));
        hiddenWordLabel.setForeground(Color.white);
        hiddenWordLabel.setHorizontalAlignment(SwingConstants.CENTER);

        hiddenWordLabel.setBounds(
                0,
                categoryLabel.getY() + categoryLabel.getPreferredSize().height + 50,
                CommonConstants.FRAME_SIZE.width,
                hiddenWordLabel.getPreferredSize().height
        );


        // letter buttons
        GridLayout gridLayout = new GridLayout(4, 7);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(
                -5,
                hiddenWordLabel.getY() + hiddenWordLabel.getPreferredSize().height,
                CommonConstants.BUTTON_PANEL_SIZE.width,
                CommonConstants.BUTTON_PANEL_SIZE.height
        );
        buttonPanel.setLayout(gridLayout);

        // create the letter buttons
        for(char c = 'A'; c <= 'Z'; c++) {
            JButton button = new JButton(Character.toString(c));
            button.setBackground(CommonConstants.PRIMARY_COLOR);
            button.setFont(customFont.deriveFont(22f));
            button.setForeground(Color.white);
            button.addActionListener(this);


            // using ASCII values to calcualte the current index
            int currentIndex = c - 'A';

            letterButtons[currentIndex] = button;
            buttonPanel.add(letterButtons[currentIndex]);
        }


        // reset Button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(customFont.deriveFont(22f));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(CommonConstants.SECONDARY_COLOR);
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);




        // Quit button

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(customFont.deriveFont(22f));
        quitButton.setForeground(Color.WHITE);
        quitButton.setBackground(CommonConstants.SECONDARY_COLOR);
        quitButton.addActionListener(this);
        buttonPanel.add(quitButton);



        getContentPane().add(categoryLabel);
        getContentPane().add(hangmanImage);
        getContentPane().add(hiddenWordLabel);
        getContentPane().add(buttonPanel);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        if(command.equals("Reset") || command.equals("Restart")){
            resetGame();

            if(command.equals("Restart")) {
                resultDialog.setVisible(false);
            }

        }else if(command.equals("Quit")) {
            dispose();
            return;
        }else {
            // letter buttons



            // disable button
            JButton button = (JButton) e.getSource();
            button.setEnabled(false);


            // check if the word contains the users guess.
            if(wordChallenge[1].contains(command)) {
                button.setBackground(Color.green);



                char[] hiddenWord = hiddenWordLabel.getText().toCharArray();

                for(int i = 0; i < wordChallenge[1].length(); i ++) {
                    if(wordChallenge[1].charAt(i) == command.charAt(0)) {
                        hiddenWord[i] = command.charAt(0);
                    }
                }


                //update hiddenWordlabel
                hiddenWordLabel.setText(String.valueOf(hiddenWord));

                if(!hiddenWordLabel.getText().contains("*")) {
                    // display success result\

                    resultLabel.setText("you git it right!");
                    resultDialog.setVisible(true);

                }


            }else {
                button.setBackground(Color.RED);

                ++incorrectGuesses;


                CustomTools.updateImage(hangmanImage, incorrectGuesses + 1 + ".png");



                if(incorrectGuesses >= 6){

                    // diplsay reuslt dialog wiht game over
                    resultLabel.setText("Too Bad, try again? ");
                    resultDialog.setVisible(true);

                }
            }
            wordLabel.setText("Word: " + wordChallenge[1]);
        }
    }


    private void createResultDialog(){

        resultDialog = new JDialog();
        resultDialog.setSize(CommonConstants.RESULT_DIALOG_SIZE);
        resultDialog.getContentPane().setBackground(CommonConstants.BACKGROUND_COLOR);
        resultDialog.setResizable(false);
        resultDialog.setLocationRelativeTo(null);
        resultDialog.setModal(true);
        resultDialog.setLayout(new GridLayout(3, 1));
        resultDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetGame();
            }
        });


        resultLabel = new JLabel();
        resultLabel.setForeground(Color.white);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        wordLabel = new JLabel();
        wordLabel.setForeground(Color.white);
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JButton restartButton = new JButton("Restart");
        restartButton.setForeground(Color.WHITE);
        restartButton.setBackground(CommonConstants.SECONDARY_COLOR);
        restartButton.addActionListener(this);


        resultDialog.add(resultLabel);
        resultDialog.add(wordLabel);
        resultDialog.add(restartButton);


    }


    public void resetGame(){
        // load new challenge
        wordChallenge = wordDB.loadChallenge();
        incorrectGuesses = 0;

        //load starting image
        CustomTools.updateImage(hangmanImage, CommonConstants.IMAGE_PATH);


        // update category
        categoryLabel.setText(wordChallenge[0]);


        // update hiddenWord
        String hiddenWOrd = CustomTools.hideWords(wordChallenge[1]);
        hiddenWordLabel.setText(hiddenWOrd);


        // enable all buttons again

        for(int i  = 0; i < letterButtons.length; i++) {
            letterButtons[i].setEnabled(true);
            letterButtons[i].setBackground(CommonConstants.PRIMARY_COLOR);
        }
    }


}
