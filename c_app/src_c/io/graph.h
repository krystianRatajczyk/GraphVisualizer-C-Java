#ifndef GRAPH_H
#define GRAPH_H

#include <stdlib.h>
#define MAX_NAME_LEN 64
#define INIT_CAP 16

#define ERR_FILE 1
#define ERR_FORMAT 2
#define ERR_ALGO 3
#define ERR_ARGS 4

typedef struct {
  int id;
  double x, y;
} Node;

typedef struct {
  char name[MAX_NAME_LEN];
  int n_1, n_2; //<- indeksy do tablicy NODE
  double weight;
} Edge;

typedef struct {
  Node *nodes;
  int node_count;
  int node_cap;
  Edge *edges;
  int edge_count;
  int edge_cap;
} Graph;

Graph *graph_create(void);
int graph_load_text(Graph *g, const char *path, int verbose);
int graph_save_text(const Graph *g, const char *path);
int graph_save_binary(const Graph *g, const char *path);
void free_graph(Graph *g);

#endif
