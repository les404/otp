package com.example.otp;


import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
//
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.EventObject;

public class MainController {

    @FXML private Label welcomeText;
    @FXML private Pane panee;
    @FXML private TextField txtusername;
    @FXML private PasswordField txtpasswordl;
    @FXML private Text identifier;
    @FXML private TextField txtemail;
    @FXML private TextField txtotp;
    static String storedOtp;


    private Session newSession;
    private MimeMessage mimeMessage;


    Parent root;
    Scene scene;
    Stage stage;

    private void sendEmail() throws MessagingException {
        String fromUser = "lestervalenciano05@gmail.com";
        String fromUserPassword = "taza vbux afra sxqb";
        String emailHost = "smtp.gmail.com";
        Transport transport = newSession.getTransport("smtp");
        transport.connect(emailHost, fromUser, fromUserPassword);
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
        System.out.println("Email successfully sent!!!");
    }

    static char[] otpgen(int otplen){
        String number = "0123456789";

        Random random = new Random();
        char[] otp = new char[otplen];

        for(int i = 0; i < otplen; i++){
            otp[i] = number.charAt(random.nextInt(number.length()));
        }
        return otp;
    }


    private MimeMessage draftEmail() throws AddressException, MessagingException, IOException {
        int otpLength = 4;
        char[] otpArray = otpgen(otpLength);
        String otp = new String(otpArray);
        storeotp(otp);
        String email = txtemail.getText();

        String[] emailReceipients = {(email)};
        String emailSubject = "Login OTP";
        String emailBody = "Your otp is: " + otp ;
        mimeMessage = new MimeMessage(newSession);

        for (int i =0 ;i<emailReceipients.length;i++)
        {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceipients[i]));
        }
        mimeMessage.setSubject(emailSubject);


        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailBody,"text/html");
        MimeMultipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart);
        mimeMessage.setContent(multiPart);
        return mimeMessage;
    }


    private void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        newSession = Session.getDefaultInstance(properties,null);

        newSession = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-email@gmail.com", "your-app-password");
            }
        });
    }

        public void btnlogin(ActionEvent event) throws IOException, MessagingException {
        String username = txtusername.getText();
        String password = txtpasswordl.getText();
        String email = txtemail.getText();
        String identi = identifier.getText();

        if (username.equals("lester") && password.equals("lester")){
            setupServerProperties();
            draftEmail();
            sendEmail();
            Parent root = FXMLLoader.load(getClass().getResource("otpconfirmation.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            identifier.setText("username or password or email is empty");

        } else{
            identifier.setText("Wrong Credential");
        }

    }
    private void storeotp(String otp) {
        storedOtp = otp ;
    }


    public void btnconfirm (ActionEvent event) throws IOException{
        String cotp = txtotp.getText();

        if(cotp.equals(storedOtp)){

            Parent root = FXMLLoader.load(getClass().getResource("landing.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }else{
            Parent root = FXMLLoader.load(getClass().getResource("otpconfirmation.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void btngoback (ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


}