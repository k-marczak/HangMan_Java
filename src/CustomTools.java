import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class CustomTools {
    public static JLabel loadImage(String resourse) {
        BufferedImage image;
        try{
            InputStream inputStream = CustomTools.class.getResourceAsStream(resourse);
            image = ImageIO.read(inputStream);
            return new JLabel(new ImageIcon(image));
        }catch(Exception e) {
            System.out.println("Error: " + e);
        }
        return null;
    }


    public static String hideWords(String word) {
        String hiddenWord = "";
        for(int i = 0; i < word.length(); i++) {
            if(!(word.charAt(i) == ' ')) {
                hiddenWord += "*";
            }else {
                hiddenWord += " ";
            }
        }
        return hiddenWord;
    }



}
