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

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Email Verification - HotOrNot</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; }" +
                        "    h2 { color: #333333; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .otp { display: inline-block; background-color: #e63946; color: #fff; font-size: 24px; font-weight: bold;" +
                        "           letter-spacing: 4px; padding: 10px 25px; border-radius: 8px; margin-top: 15px; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #e63946; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🔥 HotOrNot</div>" +
                        "    <h2>Email Verification</h2>" +
                        "    <p>Hi <strong>User!</strong></p>" +
                        "    <p>Thank you for joining <strong>HotOrNot</strong>. Please verify your email address to complete your registration.</p>" +
                        "    <p>Use the OTP below to verify your email:</p>" +
                        "    <div class='otp'>" + otp + "</div>" +
                        "    <p>This OTP will expire in <strong>10 minutes</strong>. Please do not share it with anyone.</p>" +
                        "    <p>If you didn’t request this verification, you can safely ignore this email.</p>" +
                        "    <p>Need help? Contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +
                        "    <div class='footer'>© 2025 HotOrNot / HOn. All rights reserved.</div>" +
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
      background-color: #f5f6fa;
      margin: 0;
      padding: 40px 0;
    }
    .container {
      max-width: 600px;
      margin: auto;
      background-color: #ffffff;
      border-radius: 12px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      padding: 40px 30px;
      text-align: center;
    }
    .logo {
      font-size: 34px;
      font-weight: bold;
      color: #e63946;
      margin-bottom: 20px;
    }
    h2 {
      color: #333333;
      font-size: 24px;
      margin-bottom: 10px;
    }
    p {
      color: #555555;
      font-size: 16px;
      line-height: 1.6;
      margin: 15px 0;
    }
    .highlight {
      color: #e63946;
      font-weight: 600;
    }
    .button {
      display: inline-block;
      margin-top: 25px;
      background-color: #e63946;
      color: #ffffff;
      padding: 12px 28px;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
      font-size: 16px;
      transition: background-color 0.3s ease;
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
      line-height: 1.4;
    }
    .emoji {
      font-size: 20px;
    }
  </style>
</head>
<body>
  <div class='container'>
    <div class='logo'>🔥 HotOrNot</div>
    <h2>Welcome to <span class='highlight'>HotOrNot</span>!</h2>
    <p>Hi <span class='highlight'>User</span> 👋,</p>
    <p>We’re absolutely thrilled to have you join the <span class='highlight'>HotOrNot</span> community! <span class='emoji'>🎉</span></p>
    <p>Your registration was successful, and your account is now active. Start exploring, connecting, and sharing your favorites — your journey starts now!</p>
    <p>If you ever need help, reach us anytime at <a href='mailto:contactjobsagar@gmail.com' class='highlight'>contactjobsagar@gmail.com</a>.</p>
    <a href='https://hotornot.com/login' class='button'>Login Now</a>
    <div class='footer'>
      © 2025 HotOrNot. All rights reserved.<br>
      You’re receiving this email because you registered on HotOrNot.<br>
      Stay real, stay <span class='highlight'>HotOrNot 🔥</span>
    </div>
  </div>
</body>
</html>
""";

        helper.setText(htmlBody , true);
        javaMailSender.send(message);

    }

    //reset one.

    public String sendResetPasswordOtp(String email) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Random random = new Random();
        String otp = random.nextInt(111111 , 999999 )+ "";
        System.out.println("Otp is : " + otp);


        helper.setFrom("shrotimuskan@gmail.com");
        helper.setTo(email);
        helper.setSubject("Reset Your Password - HotOrNot");

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Password Reset OTP</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; }" +
                        "    h2 { color: #333333; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .otp { display: inline-block; background-color: #e63946; color: #fff; font-size: 24px; font-weight: bold; letter-spacing: 4px; padding: 10px 25px; border-radius: 8px; margin-top: 15px; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🔥 HotOrNot</div>" +
                        "    <h2>Reset Your Password</h2>" +
                        "    <p>Hello,</p>" +
                        "    <p>We received a request to reset your password. Use the OTP below to complete your request:</p>" +
                        "    <div class='otp'>" + otp + "</div>" +
                        "    <p>This OTP will expire in <strong>5 minutes</strong>. Please do not share it with anyone.</p>" +
                        "    <p>If you didn’t request a password reset, you can safely ignore this email.</p>" +
                        "    <div class='footer'>© 2025 HotOrNot. All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlBody, true);
        javaMailSender.send(message);
        return otp;
    }


}
