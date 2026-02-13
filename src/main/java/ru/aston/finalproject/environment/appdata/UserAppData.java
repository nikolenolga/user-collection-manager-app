package ru.aston.finalproject.environment.appdata;

import ru.aston.finalproject.entity.user.BuildUser;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.entity.user.UserDataFaker;
import ru.aston.finalproject.entity.validator.UserBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.parser.UserParser;
import ru.aston.finalproject.service.loader.ConsoleDataLoader;
import ru.aston.finalproject.service.loader.FileDataLoader;
import ru.aston.finalproject.service.loader.LoaderService;
import ru.aston.finalproject.service.loader.RandomUserDataLoader;
import ru.aston.finalproject.service.loader.UserLoaderService;

public class UserAppData extends AppData<User> {

    private static final Validate<User.Builder> userValidator = new UserBuilderValidator();
    private static final BuildUser buildUser = new BuildUser(userValidator);
    private static final UserDataFaker userDataFaker = new UserDataFaker();
    private static final RandomUserDataLoader randomUserDataLoader = new RandomUserDataLoader(userDataFaker, buildUser);
    private static final UserParser userParser = new UserParser(userValidator);
    private static final FileDataLoader<User> fileDataLoader = new FileDataLoader<>(userParser);
    private static final ConsoleDataLoader<User> consoleDataLoader = new ConsoleDataLoader<>(userParser);
    private static final LoaderService<User> userLoaderService = new UserLoaderService(
            fileDataLoader, consoleDataLoader, randomUserDataLoader);

    public UserAppData() {
        super(userParser, userLoaderService, fileDataLoader);
    }
}
