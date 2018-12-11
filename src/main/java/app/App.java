package app;

import resolver.Resolver;
import tools.Tools;

public class App {

    public static void main(String[] args) {
        Tools tools = new Tools();
        Resolver res = new Resolver(tools.stringToArray(tools.tempStringSudoku));
        res.run();
    }
}
