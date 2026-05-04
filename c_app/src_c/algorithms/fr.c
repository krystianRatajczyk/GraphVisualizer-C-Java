#include "fr.h"
#include "../io/graph.h"
#include "../utils/math_utils.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

void fr(Graph *g, int iterations, double width, double height, int verbose) {
  srand(time(NULL));
  if (g->node_count == 0)
    return;
  double ideal_dist = sqrt(height * width / (double)g->node_count);

  Vector *offset = calloc(g->node_count, sizeof(Vector));
  if (!offset)
    return;

  double temperature = width / 10;

  for (int v = 0; v < g->node_count; v++) {
    g->nodes[v].x = (double)rand() / (double)RAND_MAX * width;
    g->nodes[v].y = (double)rand() / (double)RAND_MAX * height;
  }
  if (verbose)
    printf("Start algorytmu FR\n");
  for (int i = 0; i < iterations; i++) {
    if (verbose && (i % 50 == 0))
      printf("Algorytm FR: Iteracja %d/%d, temperatura: %lf\n", i, iterations,
             temperature);
    memset(offset, 0, g->node_count * sizeof(Vector));

    for (int v = 0; v < g->node_count; v++) {
      for (int u = 0; u < g->node_count; u++) {
        if (v == u)
          continue;
        Vector pos_v = {g->nodes[v].x, g->nodes[v].y};
        Vector pos_u = {g->nodes[u].x, g->nodes[u].y};
        Vector sub = vector_sub(pos_v, pos_u);
        Vector direction = vector_normalize(sub);
        double d = vector_mag(sub);
        double force_val;
        if (d > 0) {
          force_val = ideal_dist * ideal_dist / d;
        } else {
          force_val = ideal_dist * ideal_dist;
        }
        offset[v] = vector_add(offset[v], vector_mul(direction, force_val));
      }
    }
    for (int j = 0; j < g->edge_count; j++) {
      int v = g->edges[j].n_1;
      int u = g->edges[j].n_2;
      Vector pos_v = {g->nodes[v].x, g->nodes[v].y};
      Vector pos_u = {g->nodes[u].x, g->nodes[u].y};
      Vector sub = vector_sub(pos_v, pos_u);
      Vector direction = vector_normalize(sub);
      double d = vector_mag(sub);
      double force_val;
      if (ideal_dist > 0) {
        force_val = (d * d / ideal_dist) * g->edges[j].weight;
      } else {
        force_val = (d * d) * g->edges[j].weight;
      }
      offset[v] = vector_sub(offset[v], vector_mul(direction, force_val));
      offset[u] = vector_add(offset[u], vector_mul(direction, force_val));
    }
    for (int v = 0; v < g->node_count; v++) {
      Vector normalized = vector_normalize(offset[v]);
      Vector res =
          vector_mul(normalized, fmin(vector_mag(offset[v]), temperature));
      g->nodes[v].x += res.x;
      g->nodes[v].y += res.y;
      g->nodes[v].x = fmin(g->nodes[v].x, width);
      g->nodes[v].x = fmax(g->nodes[v].x, 0);
      g->nodes[v].y = fmin(g->nodes[v].y, height);
      g->nodes[v].y = fmax(g->nodes[v].y, 0);
    }
    temperature *= 0.95;
  }
  printf("Algorytm FR: Zakończono!\n");

  free(offset);
}
