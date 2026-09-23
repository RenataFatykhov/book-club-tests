package tests;

import models.clubs.ClubRequestPaginationModel;
import models.clubs.ClubResponseModel;
import models.clubs.ResultsClubModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class GetClubsTests extends TestBase {

    @Test
    @DisplayName("Получение списка клубов")
    public void getClubsWithoutParametersTest() {
        clubsClient.noParametersGetClubs();
    }

    @Test
    @DisplayName("Переход по next возвращает следующую страницу клубов без повторений")
    public void getClubsWithPaginationTest() {
        ClubResponseModel firstResponse = clubsClient.noParametersGetClubs();

        step("Проверить наличие первой страницы и ссылки на следующую", () -> {
            assertThat(firstResponse.results()).isNotEmpty();
            assertThat(firstResponse.previous()).isNull();
            assertThat(firstResponse.next())
                    .as("Для теста пагинации нужны клубы как минимум на двух страницах")
                    .isNotBlank();
        });

        int nextPage = step("Извлечь номер следующей страницы из ссылки next", () -> {
            String query = URI.create(firstResponse.next()).getQuery();
            assertThat(query).as("Параметры ссылки next").isNotBlank();

            int page = Arrays.stream(query.split("&"))
                    .map(parameter -> parameter.split("=", 2))
                    .filter(parts -> parts.length == 2 && parts[0].equals("page"))
                    .map(parts -> Integer.parseInt(parts[1]))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("В ссылке next отсутствует параметр page"));

            assertThat(page).as("После первой страницы должна идти вторая").isEqualTo(2);
            return page;
        });

        int pageSize = firstResponse.results().size();

        ClubRequestPaginationModel parameters = new ClubRequestPaginationModel(nextPage, pageSize);

        ClubResponseModel secondResponse = clubsClient.paginationGetClubs(parameters);

        step("Проверить размер второй страницы и наличие ссылки назад", () -> {
            assertThat(secondResponse.results()).isNotEmpty();
            assertThat(secondResponse.results().size()).isLessThanOrEqualTo(pageSize);
            assertThat(secondResponse.previous()).isNotBlank();
        });

        step("Проверить уникальность ID клубов и отсутствие повторений между страницами", () -> {
            List<Integer> firstPageIds = firstResponse.results().stream()
                    .map(ResultsClubModel::id)
                    .toList();
            List<Integer> secondPageIds = secondResponse.results().stream()
                    .map(ResultsClubModel::id)
                    .toList();

            assertThat(firstPageIds).doesNotContainNull().doesNotHaveDuplicates();
            assertThat(secondPageIds).doesNotContainNull().doesNotHaveDuplicates();
            assertThat(secondPageIds).doesNotContainAnyElementsOf(firstPageIds);
        });

    }
}
