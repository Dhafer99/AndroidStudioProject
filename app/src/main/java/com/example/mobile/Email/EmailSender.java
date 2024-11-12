package com.example.mobile.Email;

import android.os.AsyncTask;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;


public class EmailSender extends AsyncTask<Void, Void, Boolean> {

    private final String email;
    private final String subject;
    private final UserRepository userRepo;

    public EmailSender(String email, String subject, UserRepository userRepo) {
        this.email = email;
        this.subject = subject;
        this.userRepo = userRepo;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        // Executor to handle asynchronous fetching of user
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<UserEntity> fetchUserTask = () -> userRepo.getUserByEmail(email);
        Future<UserEntity> futureUser = executor.submit(fetchUserTask);

        try {
            // Retrieve the user entity
            UserEntity user = futureUser.get();
            String userPassword = user.getPassword(); // Assuming getPassword() gives unhashed password

            // Gmail SMTP server configuration
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Email credentials
            final String username = "genereux.scaredtocompile@gmail.com";
            final String password = "qxrosldtlgtwnpky";

            // Create a session with authentication
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Format the email body with HTML and CSS for better appearance
            String formattedMessageBody = "<html><body style='font-family: Arial, sans-serif; color: #333; background-color: #f4f4f4; padding: 20px;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);'>"
                    + "<div style='text-align: center; margin-bottom: 20px;'>"
                    + "<img src='https://i.ibb.co/Th00z3r/paw.png' alt='Company Logo' style='max-width: 150px; border-radius: 5px;'>"
                    + "</div>"
                    + "<h2 style='color: #555; text-align: center;'>Welcome to Our Service</h2>"
                    + "<p style='font-size: 16px;'>Hello,</p>"
                    + "<p style='font-size: 16px;'>Thank you for registering with us! Here are your login details:</p>"
                    + "<p style='font-size: 16px;'><strong>Email:</strong> " + email + "</p>"
                    + "<p style='font-size: 16px;'><strong>Password:</strong> " + userPassword + "</p>"
                    + "<p style='font-size: 16px;'>Please keep this information secure.</p>"
                    + "<p style='text-align: center; margin-top: 30px;'>"
                    + "<a href='http://192.168.1.184:3000/reset-password?email="+ email +"' style='background-color: #555; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Reset Password</a>"
                    + "</p>"
                    + "<br><p style='font-size: 14px; color: #777; text-align: center;'>Best regards,<br>PetCare Team</p>"
                    + "</div></body></html>";



            // Prepare the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(formattedMessageBody, "text/html");

            // Send the email
            Transport.send(message);

            return true;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown(); // Shut down the executor
        }
    }


}
