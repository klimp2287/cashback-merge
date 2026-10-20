package academy.cashbackmerge;

import picocli.CommandLine;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CliCommand()).execute(args);

        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}
