import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


        addGuiComp();

    }


    private void addGuiComp(){

        // hangman image
        hangmanImage = CustomTools.loadImage(CommonConstants.IMAGE_PATH);
        hangmanImage.setBounds(0, 0, hangmanImage.getPreferredSize().width, hangmanImage.getPreferredSize().height);


        // category display
        categoryLabel = new JLabel(wordChallenge[0]);
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
            button.setForeground(Color.white);
            button.addActionListener(this);


            // using ASCII values to calcualte the current index
            int currentIndex = c - 'A';

            letterButtons[currentIndex] = button;
            buttonPanel.add(letterButtons[currentIndex]);
        }


        // reset Button
        JButton resetButton = new JButton("Reset");
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(CommonConstants.SECONDARY_COLOR);
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);




        // Quit button

        JButton quitButton = new JButton("Quit");
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
        if(command.equals("Reset")){
            resetGame();
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


            }else {
                button.setBackground(Color.RED);

                ++incorrectGuesses;


                CustomTools.updateImage(hangmanImage, incorrectGuesses + 1 + ".png");


            }
        }
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
