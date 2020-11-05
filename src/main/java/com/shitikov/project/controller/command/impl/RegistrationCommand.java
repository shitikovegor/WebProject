package com.shitikov.project.controller.command.impl;

import com.shitikov.project.controller.Router;
import com.shitikov.project.controller.command.AttributeName;
import com.shitikov.project.controller.command.Command;
import com.shitikov.project.model.exception.ServiceException;
import com.shitikov.project.model.service.impl.UserServiceImpl;
import com.shitikov.project.util.ParameterName;
import com.shitikov.project.util.mail.MailSender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import static com.shitikov.project.controller.command.AttributeName.EMAIL_EXISTS;
import static com.shitikov.project.controller.command.AttributeName.LOGIN_EXISTS;
import static com.shitikov.project.util.ParameterName.*;


public class RegistrationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String EXISTS = "exists";
    private static final String EMAIL_SUBJECT = "HelpByCar. Activate your account"; // TODO: 09.10.2020 from properties
    private static final String EMAIL_PROPERTIES = "config/mail.properties";
    private ResourceBundle resourceBundle = ResourceBundle.getBundle(ParameterName.PAGES_PATH);

    @Override
    public Router execute(HttpServletRequest request) {
        Router router;

        Map<String, String> parameters = new HashMap<>();
        parameters.put(LOGIN, request.getParameter(LOGIN));
        parameters.put(PASSWORD, request.getParameter(PASSWORD));
        parameters.put(NAME, request.getParameter(NAME));
        parameters.put(SURNAME, request.getParameter(SURNAME));
        parameters.put(EMAIL, request.getParameter(EMAIL));
        parameters.put(PHONE, request.getParameter(PHONE));
        parameters.put(SUBJECT_TYPE, request.getParameter(SUBJECT_TYPE));
        parameters.put(ROLE_TYPE, request.getParameter(ROLE_TYPE));

        try {
            if (UserServiceImpl.getInstance().add(parameters)) {
                Properties properties = new Properties();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(EMAIL_PROPERTIES);
                properties.load(inputStream);
                String emailBody = String.format(EMAIL_BODY,
                        request.getRequestURL(), request.getParameter(LOGIN));
                MailSender sender = new MailSender(request.getParameter(EMAIL)
                        , EMAIL_SUBJECT, emailBody, properties);
                sender.send();
                router = new Router(resourceBundle.getString("path.page.activation"));
                logger.log(Level.INFO, "User added");
            } else {
                if (parameters.get(LOGIN).equals(EXISTS)) {
                    request.setAttribute(LOGIN_EXISTS, true);
                    parameters.remove(LOGIN);
                }
                if (parameters.get(EMAIL).equals(EXISTS)) {
                    request.setAttribute(EMAIL_EXISTS, true);
                    parameters.remove(EMAIL);
                }
                parameters.remove(PASSWORD);
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    if (!entry.getValue().isEmpty()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    } else {
                        request.setAttribute(entry.getKey().concat(AttributeName.ATTRIBUTE_SUBSTRING_INVALID), true);
                    }
                }
                router = new Router(resourceBundle.getString("path.page.registration"));
                logger.log(Level.INFO, "User didn't add");
            }
        } catch (ServiceException | IOException e) {

            router = new Router(resourceBundle.getString("path.page.error500"));
        }
        return router;
    }
}


