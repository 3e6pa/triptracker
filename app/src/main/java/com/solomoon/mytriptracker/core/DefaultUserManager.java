package com.solomoon.mytriptracker.core;

import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.UserDao;
import com.solomoon.mytriptracker.model.User;
import com.solomoon.mytriptracker.exception.UserAlreadyExistsException;
import com.solomoon.mytriptracker.utils.Encryptor;

public class DefaultUserManager {

    private final String USER_EXIST_MESSAGE = "User already exist";

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

    private Boolean existsUser(String login) {
        User user = userDao.getUserByLogin(login);
        return user != null;
    }

    private void encryptPassword(final User user) {
        final String password = user.getPassword();
        user.setPassword(Encryptor.encryptSHA2(password));
    }

}
