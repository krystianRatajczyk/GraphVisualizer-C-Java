#include "graph.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

Graph *graph_create() {
  Graph *g = malloc(sizeof(Graph));

  if (g == NULL)
    return NULL;

  g->node_count = 0;
  g->node_cap = INIT_CAP;
  g->nodes = malloc(sizeof(Node) * INIT_CAP);

  g->edge_count = 0;
  g->edge_cap = INIT_CAP;
  g->edges = malloc(sizeof(Edge) * INIT_CAP);

  if (g->edges == NULL || g->nodes == NULL) {
    free(g->edges);
    free(g->nodes);
    free(g);
    return NULL;
  }

  return g;
}

static int find_or_add_node(Graph *g, int id) {
  for (int i = 0; i < g->node_count; i++) {
    if (g->nodes[i].id == id)
      return i;
  }

  if (g->node_cap == g->node_count) {
    g->node_cap *= 2;
    Node *tmp = realloc(g->nodes, g->node_cap * sizeof(Node));
    if (!tmp)
      return -1;
    g->nodes = tmp;
  }

  int idx = g->node_count++;
  g->nodes[idx].id = id;
  g->nodes[idx].x = 0.0;
  g->nodes[idx].y = 0.0;
  return idx;
}

int graph_load_text(Graph *g, const char *path, int verbose) {
  FILE *f = fopen(path, "r");
  if (!f) {
    perror(path);
    return ERR_FILE;
  }

  char edge_name[MAX_NAME_LEN];
  int u, v;
  double weight;

  while (fscanf(f, "%63s %d %d %lf", edge_name, &u, &v, &weight) == 4) {
    int idx_u = find_or_add_node(g, u);
    int idx_v = find_or_add_node(g, v);
    if (idx_u < 0 || idx_v < 0) {
      fclose(f);
      return ERR_FILE;
    }

    if (g->edge_cap == g->edge_count) {
      g->edge_cap *= 2;
      Edge *tmp = realloc(g->edges, g->edge_cap * sizeof(Edge));
      if (!tmp) {
        fclose(f);
        return ERR_FILE;
      }
      g->edges = tmp;
    }

    Edge *e = &g->edges[g->edge_count++];
    strncpy(e->name, edge_name, MAX_NAME_LEN - 1);
    e->name[MAX_NAME_LEN - 1] = '\0';
    e->n_1 = idx_u;
    e->n_2 = idx_v;
    e->weight = weight;

    if (verbose)
      fprintf(stderr, "[load] %s: %d -- %d (w=%.3f)\n", e->name, u, v, weight);
  }
  fclose(f);
  return 0;
}

int graph_save_text(const Graph *g, const char *path) {
  FILE *f = path ? fopen(path, "w") : stdout;
  if (!f) {
    perror(path);
    return ERR_FILE;
  }

  for (int i = 0; i < g->node_count; i++) {
    fprintf(f, "%d %.6f %.6f\n", g->nodes[i].id, g->nodes[i].x, g->nodes[i].y);
  }

  if (path)
    fclose(f);
  return 0;
}

int graph_save_binary(const Graph *g, const char *path) {
  FILE *f = fopen(path, "wb");
  if (!f) {
    perror(path);
    return ERR_FILE;
  }
  for (int i = 0; i < g->node_count; i++) {
    fwrite(&g->nodes[i].id, sizeof(int), 1, f);
    fwrite(&g->nodes[i].x, sizeof(double), 1, f);
    fwrite(&g->nodes[i].y, sizeof(double), 1, f);
  }

  fclose(f);
  return 0;
}

void free_graph(Graph *g) {
  if (g == NULL)
    return;
  free(g->edges);
  free(g->nodes);
  free(g);
  return;
}
