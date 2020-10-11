package com.solomoon.mytriptracker.core;

import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.AppSettingsDao;
import com.solomoon.mytriptracker.data.UserDao;
import com.solomoon.mytriptracker.exception.IncorrectPasswordException;
import com.solomoon.mytriptracker.exception.UserNotFoundException;
import com.solomoon.mytriptracker.model.AppSettings;
import com.solomoon.mytriptracker.model.User;
import com.solomoon.mytriptracker.exception.UserAlreadyExistsException;
import com.solomoon.mytriptracker.utils.Encryptor;

public class DefaultUserManager {

    private final String USER_EXIST_MESSAGE = "User already exist";
    private final String USER_NOT_FOUND_MESSAGE = "User not found";
    private final String INCORRECT_PASSWORD_MESSAGE = "Incorrect password";

    private UserDao userDao;

    public DefaultUserManager(AppDatabase appDatabase) {
        userDao = appDatabase.userDao();
    }

    public void registerNewUser(User user) {
        if (existsUser(user.getLogin())) {
            throw new UserAlreadyExistsException(USER_EXIST_MESSAGE);
        } else {
            encryptPassword(user);
            userDao.insert(user);
        }
    }

    public User login(String login, String password) {
        User user = userDao.getUserByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
        } else {
            String encryptPassword = Encryptor.encryptSHA256(password);
            if (!user.getPassword().equals(encryptPassword)) {
                throw new IncorrectPasswordException(INCORRECT_PASSWORD_MESSAGE);
            } else {
                AppSettings appSettings = new AppSettings();
                return user;
            }
        }
    }

    private Boolean existsUser(String login) {
        User user = userDao.getUserByLogin(login);
        return user != null;
    }

    private void encryptPassword(final User user) {
        final String password = user.getPassword();
        user.setPassword(Encryptor.encryptSHA256(password));
    }

}
