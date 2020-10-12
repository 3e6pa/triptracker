package com.solomoon.mytriptracker.core;

import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.UserDao;
import com.solomoon.mytriptracker.exceptions.IncorrectPasswordException;
import com.solomoon.mytriptracker.exceptions.NotFoundException;
import com.solomoon.mytriptracker.models.User;
import com.solomoon.mytriptracker.exceptions.UserAlreadyExistsException;
import com.solomoon.mytriptracker.utils.Encryptor;

import java.security.NoSuchAlgorithmException;

public class DefaultUserManager {

    private static final String USER_EXIST_MESSAGE = "User already exist";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String INCORRECT_PASSWORD_MESSAGE = "Incorrect password";

    private UserDao userDao;

    public DefaultUserManager(AppDatabase appDatabase) {
        userDao = appDatabase.userDao();
    }

    public void registerNewUser(User user) throws NoSuchAlgorithmException {
        if (existsUser(user.getLogin())) {
            throw new UserAlreadyExistsException(USER_EXIST_MESSAGE);
        } else {
            encryptPassword(user);
            userDao.insert(user);
        }
    }

    public User login(String login, String password) throws NoSuchAlgorithmException {
        User user = userDao.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
        } else {
            StringBuilder encryptPassword =  new StringBuilder();
            encryptPassword.append(Encryptor.encryptSHA256(password));
            if (!user.getPassword().equals(encryptPassword.toString())) {
                throw new IncorrectPasswordException(INCORRECT_PASSWORD_MESSAGE);
            } else {
                return user;
            }
        }
    }

    private Boolean existsUser(String login) {
        User user = userDao.getUserByLogin(login);
        return user != null;
    }

    private void encryptPassword(final User user) throws NoSuchAlgorithmException {
        final StringBuilder password = new StringBuilder();
        password.append(user.getPassword());
        user.setPassword(Encryptor.encryptSHA256(password.toString()));
    }

}
