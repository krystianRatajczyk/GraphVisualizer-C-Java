#include <stdio.h>
#include <stdlib.h>

#define APP_PATH "../graph_layout"
#define GEN_PATH "./generator"

int run_test(const char *test_name, const char *cmd, int expected_exit_code) {
    printf("--- TEST: %s ---\n", test_name);
    int status = system(cmd);
    int exit_code = status / 256; 

    if (exit_code == expected_exit_code) {
        printf("\033[0;32m[ZALICZONY]\033[0m Oczekiwano: %d, Otrzymano: %d\n\n", expected_exit_code, exit_code);
        return 1;
    } else {
        printf("\033[0;31m[OBLANY]\033[0m Oczekiwano: %d, Otrzymano: %d (Komenda: %s)\n\n", expected_exit_code, exit_code, cmd);
        return 0;
    }
}

int main() {
    int passed = 0, total = 0;
    char cmd[512];

    printf("========================================\n");
    printf(" ROZPOCZYNAM AUTOMATYCZNE TESTY SYSTEMU\n");
    printf("========================================\n\n");

    system(GEN_PATH " 100 200 1.0 5.0 test_100.txt > /dev/null");
    snprintf(cmd, sizeof(cmd), "%s -i test_100.txt -o out_100.txt -a tutte > /dev/null", APP_PATH);
    passed += run_test("Obciazeniowy - 100 wezlow (Tutte)", cmd, 0);
    total++;

    system(GEN_PATH " 500 1000 1.0 5.0 test_500.txt > /dev/null");
    snprintf(cmd, sizeof(cmd), "%s -i test_500.txt -o out_500.txt -a fr > /dev/null", APP_PATH);
    passed += run_test("Obciazeniowy - 500 wezlow (FR)", cmd, 0);
    total++;

    system(GEN_PATH " 2 1 1.0 5.0 test_2_nodes.txt > /dev/null");
    snprintf(cmd, sizeof(cmd), "%s -i test_2_nodes.txt -a tutte > /dev/null 2>&1", APP_PATH);
    passed += run_test("Krawedziowy - Zbyt maly graf (Tutte musi przerwac - Kod 3)", cmd, 3);
    total++;

    system(GEN_PATH " 10 15 0.000001 9999999.0 test_weights.txt > /dev/null");
    snprintf(cmd, sizeof(cmd), "%s -i test_weights.txt -o out_weights.txt -a tutte > /dev/null", APP_PATH);
    passed += run_test("Krawedziowy - Ekstremalne wagi (Precyzja double)", cmd, 0);
    total++;

    snprintf(cmd, sizeof(cmd), "valgrind --leak-check=full --error-exitcode=99 %s -i test_100.txt -a tutte > /dev/null 2>&1", APP_PATH);
    passed += run_test("Weryfikacja Valgrind (Brak wyciekow pamieci)", cmd, 0);
    total++;

    system("rm -f test_*.txt out_*.txt");

    printf("========================================\n");
    printf("WYNIK TESTOW: %d / %d ZALICZONYCH\n", passed, total);
    if (passed == total) {
        printf("\033[0;32mWSZYSTKIE TESTY ZAKONCZONE SUKCESEM!\033[0m\n");
    } else {
        printf("\033[0;31mWYKRYTO BLEDY! Popraw kod i uruchom testy ponownie.\033[0m\n");
    }
    printf("========================================\n");

    return (passed == total) ? 0 : 1;
}