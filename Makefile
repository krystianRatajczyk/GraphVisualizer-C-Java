CC = gcc
CFLAGS = -Wall -Wextra -pedantic -std=gnu99 -O2
LDFLAGS = -lm

SRC_DIR = src_c
OBJ_DIR = obj
TEST_DIR = tests

SRCS = $(SRC_DIR)/core/main.c \
       $(SRC_DIR)/io/graph.c \
       $(SRC_DIR)/utils/math_utils.c \
       $(SRC_DIR)/algorithms/fr.c \
       $(SRC_DIR)/algorithms/tutte.c

OBJS = $(SRCS:$(SRC_DIR)/%.c=$(OBJ_DIR)/%.o)

TARGET = graphviz
GEN_BIN = $(TEST_DIR)/generator
TEST_BIN = $(TEST_DIR)/run_tests

.PHONY: all clean test

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) $(OBJS) -o $(TARGET) $(LDFLAGS)

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c
	@mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -c $< -o $@

test: all $(GEN_BIN) $(TEST_BIN)
	@cd $(TEST_DIR) && ./run_tests

$(GEN_BIN): $(TEST_DIR)/generator.c
	$(CC) $(CFLAGS) $< -o $@ $(LDFLAGS)

$(TEST_BIN): $(TEST_DIR)/tests.c
	$(CC) $(CFLAGS) $< -o $@ $(LDFLAGS)

clean:
	rm -rf $(OBJ_DIR) $(TARGET) $(GEN_BIN) $(TEST_BIN)
	rm -f $(TEST_DIR)/test_*.txt $(TEST_DIR)/out_*.txt
