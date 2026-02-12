package ru.aston.finalproject.actions;

import lombok.Getter;

public enum Action {

    help(new HelpAction<>(),
            " \t list available commands and required arguments"),
    load(new LoadAction<>(),
            """
                    \t load stock ore user list
                    \t\t -size=<size> -type=file \t -path=<path-to-file> \t load from file path
                    \t\t -size=<size> -type=console \t\t\t\t\t\t load from console input
                    \t\t -size=<size> -type=excel \t -xlsx=<path-to-file> \t load from excel file
                    \t\t -size=<size> -type=random \t\t\t\t\t\t\t load random user list"""),
    write(new WriteAction<>(),
            """
                    \t write current list to file
                    \t\t -file=<file-path> \t\t\t\t\t\t\t\t\t write to file (uses current parser format)"""),
    count(new UsersCountAction(),
            """
                    \t count entries of a given user
                    \t\t -threads=<tread count> \t\t\t\t\t\t\t thread count for multithread search
                    \t\t (set at 1 for sequential search)"""),
    sort(new SortingAction<>(),
            """
                    \t sorting current list to file
                    \t\t -basic \t\t\t\t\t\t\t\t\t\t\t natural order sorting for user and stock
                    \t\t -user \t\t\t\t\t\t\t\t\t\t\t\t sorting only by even age user"""),
    filter(new RemovePartsOfStockAction(),
            """
                     \t remove stocks from list after filters
                     \t\t -floor=<value> \t\t\t\t\t\t\t\t\t with floor parameter
                    \t\t -ceiling=<value> \t\t\t\t\t\t\t\t\t with ceiling parameter
                    \t\t -value=<value> \t\t\t\t\t\t\t\t\t just value parameter
                    \t\t -floor=<value> -ceiling=<value> \t\t\t\t\t with both borders
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t (in value need write PE stock)"""),
    print(new PrintAction<>(),
            " \t print current saved list as string"),
    show(new ShowAction<>(),
            " \t print current saved list with toString"),
    clear(new ClearAction<>(),
            " \t clear current saved list"),
    change(null, " \t to change entity"),
    exit(null, " \t to close the program");

    @Getter
    private final String info;

    @Getter
    private final AppAction<?> appAction;

    Action(AppAction<?> appAction, String info) {
        this.appAction = appAction;
        this.info = info;
    }

}
