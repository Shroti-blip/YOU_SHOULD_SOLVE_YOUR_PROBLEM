package com.example.ProjectHON.User_masterpackage;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    //for sending email verification otp.
    public String sendOtp(String email)throws Exception{
        MimeMessage message =javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        //OTP generate with random package.

        Random random = new Random();
        String otp = random.nextInt(111111 , 999999 )+ "";
        System.out.println("Otp is : " + otp);

        helper.setFrom("shrotimuskan@gmail.com");
        helper.setTo(email);
        helper.setSubject("Welcome User");

        String htmlBody = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <title>Email Verification - HotOrNot</title>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f5f7fa; margin: 0; padding: 40px 0; }" +
                "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px;" +
                "                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); padding: 40px; text-align: center; }" +
                "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; letter-spacing: 1px; }" +
                "    h1 { font-size: 24px; color: #222; margin-bottom: 16px; }" +
                "    p { font-size: 16px; color: #555; margin: 14px 0; line-height: 1.6; }" +
                "    .otp-box { font-size: 28px; font-weight: bold; letter-spacing: 6px; background-color: #f0f0f0;" +
                "               padding: 15px 25px; border-radius: 8px; display: inline-block; margin: 20px 0; color: #222; }" +
                "    .footer { margin-top: 40px; font-size: 12px; color: #999; line-height: 1.4; }" +
                "    .footer a { color: #e63946; text-decoration: none; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='logo'>HotOrNot <span style='color:#555;'>/ HOn</span></div>" +
                "    <h1>Email Verification</h1>" +
                "    <p>Hi <strong>User!</strong></p>" +
                "    <p>Our admin has sent this verification code to confirm your email address for <strong>HotOrNot</strong>.</p>" +
                "    <p>Please use the OTP below to verify your email:</p>" +
                "    <div class='otp-box'>" + otp + "</div>" +
                "    <p>This OTP will expire in <strong>10 minutes</strong>.</p>" +
                "    <p>If you did not request this verification, please ignore this email or contact our support team immediately.</p>" +
                "    <p>Need help? Reach out at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +
                "    <p><strong>Stay real, stay HotOrNot!</strong></p>" +
                "    <div class='footer'>" +
                "      © 2025 HotOrNot / HOn, All rights reserved.<br>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";


        helper.setText(htmlBody ,true);
        javaMailSender.send(message);
        return otp;
    }

    //after registration

    public void sendAfterRegistration(String email )throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("shrotimuskan@gmail.com");
        helper.setTo(email);
        helper.setSubject("SuccessFull Registration");

        String htmlBody = """
<!DOCTYPE html>
<html lang='en'>
<head>
  <meta charset='UTF-8'>
  <title>Welcome to HotOrNot</title>
  <style>
    body {
      font-family: 'Segoe UI', Arial, sans-serif;
      background-color: #f6f8fb;
      margin: 0;
      padding: 40px 0;
    }
    .container {
      max-width: 600px;
      margin: auto;
      background-color: #ffffff;
      border-radius: 10px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.08);
      padding: 40px 30px;
      text-align: center;
    }
    .logo {
      font-size: 32px;
      font-weight: bold;
      color: #e63946;
      letter-spacing: 1px;
      margin-bottom: 25px;
    }
    h2 {
      color: #333333;
      margin-bottom: 10px;
      font-size: 22px;
    }
    p {
      color: #555555;
      font-size: 16px;
      line-height: 1.6;
      margin: 15px 0;
    }
    .highlight {
      color: #e63946;
      font-weight: bold;
    }
    .button {
      display: inline-block;
      margin-top: 25px;
      background-color: #e63946;
      color: white;
      padding: 12px 25px;
      border-radius: 6px;
      text-decoration: none;
      font-weight: 600;
      transition: 0.3s ease;
    }
    .button:hover {
      background-color: #c71f2d;
    }
    .footer {
      margin-top: 35px;
      font-size: 12px;
      color: #999999;
      border-top: 1px solid #eeeeee;
      padding-top: 20px;
    }
  </style>
</head>
<body>
  <div class='container'>
    <div class='logo'>🔥 HotOrNot</div>
    <h2>Welcome to <span class='highlight'>HotOrNot</span>!</h2>
    <p>Hi <span class='highlight'>User</span>,</p>
    <p>We’re thrilled to have you join the <span class='highlight'>HotOrNot</span> community! 🎉</p>
    <p>Your registration was successful, and your account is now active. Start exploring, connecting, and sharing your favorites — it’s your time to shine!</p>
    <p>If you ever need help, feel free to reach us at <span class='highlight'>contactjobsagar@gmail.com</span>.</p>
    <a href='https://hotornot.com/login' class='button'>Login Now</a>
    <div class='footer'>
      © 2025 HotOrNot. All rights reserved.<br>
      You’re receiving this email because you registered at HotOrNot.
    </div>
  </div>
</body>
</html>
""";

        helper.setText(htmlBody , true);
        javaMailSender.send(message);

    }

}
