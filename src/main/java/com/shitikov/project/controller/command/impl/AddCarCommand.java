package com.shitikov.project.controller.command.impl;

import com.shitikov.project.controller.Router;
import com.shitikov.project.controller.command.AttributeName;
import com.shitikov.project.controller.command.Command;
import com.shitikov.project.controller.command.CommandType;
import com.shitikov.project.model.entity.User;
import com.shitikov.project.model.exception.ServiceException;
import com.shitikov.project.model.service.CarService;
import com.shitikov.project.model.service.impl.CarServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.shitikov.project.util.ParameterName.*;


public class AddCarCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private ResourceBundle resourceBundle = ResourceBundle.getBundle(PAGES_PATH);

    @Override
    public Router execute(HttpServletRequest request) {
        CarService carService = CarServiceImpl.getInstance();
        Router router;

        String weight = request.getParameter(CARRYING_WEIGHT).isEmpty() ? "0.0" : request.getParameter(CARRYING_WEIGHT);
        String volume = request.getParameter(CARRYING_VOLUME).isEmpty() ? "0.0" : request.getParameter(CARRYING_VOLUME);
        String passengers = request.getParameter(PASSENGERS_NUMBER).isEmpty() ? "0"
                : request.getParameter(PASSENGERS_NUMBER);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(CAR_NUMBER, request.getParameter(CAR_NUMBER));
        parameters.put(CARRYING_WEIGHT, weight);
        parameters.put(CARRYING_VOLUME, volume);
        parameters.put(PASSENGERS_NUMBER, passengers);

        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(USER);

            if (carService.add(parameters, user.getLogin())) {
                logger.log(Level.INFO, "Car added successfully.");

                String page = getRedirectPage(request, CommandType.ACCOUNT_PAGE);
                router = new Router(Router.Type.REDIRECT, page);
            } else {
                request.setAttribute(AttributeName.ADD_ERROR, true);
                logger.log(Level.INFO, "Car didn't add.");

                String page = resourceBundle.getString("path.page.add_edit_car");
                router = new Router(page);
            }
        } catch (ServiceException e) {
            logger.log(Level.WARN, e);
            router = new Router(resourceBundle.getString("path.page.error"));
        }
        return router;
    }
}

