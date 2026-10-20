package academy.cashbackmerge.output;

import academy.cashbackmerge.model.SimulationReport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ReportWriter {

    public void write(SimulationReport report, Path outputPath) {
        String content = createReport(report);

        try {
            Path parent = outputPath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(outputPath, content);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось записать отчёт: " + outputPath, e);
        }
    }

    private String createReport(SimulationReport report) {
        StringBuilder builder = new StringBuilder();

        builder.append("# Отчёт симуляции Cashback Merge\n\n");
        builder.append("## Параметры запуска\n\n");
        builder.append("- Количество партий: ").append(report.totalGames()).append('\n');
        builder.append("- Лимит ходов: ").append(report.movesLimit()).append('\n');
        builder.append("- Seed: ").append(report.seed()).append('\n');
        builder.append("- Стратегия: ").append(report.strategy()).append("\n\n");

        builder.append("## Распределение максимального кэшбэка\n\n");
        appendDistribution(builder, report.maxCashbackDistribution(), "%");

        builder.append("## Распределение наград\n\n");
        appendDistribution(builder, report.rewardDistribution(), "");

        builder.append("## Счёт\n\n");
        builder.append("- Средний счёт: ")
                .append(String.format(java.util.Locale.ROOT, "%.2f", report.averageScore()))
                .append('\n');
        builder.append("- Максимальный счёт: ").append(report.maxScore()).append("\n\n");

        builder.append("## Бизнес-ограничение\n\n");
        builder.append("- Игроков с максимальной наградой: ")
                .append(report.maxRewardGames())
                .append('\n');
        builder.append("- Доля игроков с максимальной наградой: ")
                .append(report.maxRewardRate().toPlainString())
                .append('\n');
        builder.append("- Статус: **")
                .append(report.businessConstraintSatisfied() ? "OK" : "FAILED")
                .append("**\n");

        return builder.toString();
    }

    private void appendDistribution(StringBuilder builder, Map<Integer, Integer> distribution, String suffix) {
        builder.append("| Значение | Количество партий |\n");
        builder.append("|---:|---:|\n");

        for (Map.Entry<Integer, Integer> entry : distribution.entrySet()) {
            builder.append("| ")
                    .append(entry.getKey())
                    .append(suffix)
                    .append(" | ")
                    .append(entry.getValue())
                    .append(" |\n");
        }

        builder.append('\n');
    }
}
