package academy.cashbackmerge;

import academy.cashbackmerge.config.GameConfig;
import academy.cashbackmerge.config.GameConfigReader;
import academy.cashbackmerge.model.SimulationReport;
import academy.cashbackmerge.output.ReportWriter;
import academy.cashbackmerge.service.SimulationService;
import java.nio.file.Path;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "cashback-merge")
public final class CliCommand implements Runnable {

    @Option(names = "--config", description = "Путь к файлу конфигурации")
    private String configPath = "";

    @Option(names = "--players", description = "Количество симулируемых игроков")
    private int players;

    @Option(names = "--strategy", description = "Стратегия игрока", defaultValue = "random")
    private String strategy = "random";

    @Option(names = "--seed", description = "Seed для воспроизводимой симуляции", defaultValue = "42")
    private long seed;

    @Option(names = "--output", description = "Путь к файлу отчёта", defaultValue = "report.md")
    private String outputPath = "report.md";

    @Override
    public void run() {
        if (configPath.isBlank() || players <= 0) {
            System.out.println("Cashback Merge — симулятор кампании кэшбэка.");
            System.out.println("Для запуска укажите --config <путь> --players <число>.");
            return;
        }

        if ("random".equals(strategy)) {
            throw new IllegalArgumentException("Пока поддерживается только стратегия random");
        }

        GameConfigReader configReader = new GameConfigReader();
        GameConfig config = configReader.read(Path.of(configPath));

        SimulationService simulationService = new SimulationService(config);
        SimulationReport report = simulationService.simulate(players, seed);

        System.out.println("Симуляция завершена.");
        System.out.println("Количество партий: " + report.totalGames());
        System.out.println("Распределение максимального кэшбэка: " + report.maxCashbackDistribution());
        System.out.println("Распределение наград: " + report.rewardDistribution());
        System.out.println("Игроков с максимальной наградой: " + report.maxRewardGames());
        System.out.println("Доля максимальной награды: " + report.maxRewardRate());
        System.out.println("Бизнес-ограничение выполнено: " + report.businessConstraintSatisfied());
        new ReportWriter().write(report, Path.of(outputPath));

        System.out.println("Отчёт сохранён: " + Path.of(outputPath).toAbsolutePath());
    }
}
