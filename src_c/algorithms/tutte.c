#include "tutte.h"
#include "../io/graph.h"
#include "../utils/math_utils.h"
#include <math.h>
#include <stdlib.h>

static int is_connected(Graph *G) {
    if (G->node_count <= 1) return 1;
    int *visited = calloc(G->node_count, sizeof(int));
    int *queue = malloc(G->node_count * sizeof(int));
    int head = 0, tail = 0, count = 1;

    queue[tail++] = 0;
    visited[0] = 1;

    while (head < tail) {
        int u = queue[head++];
        for (int j = 0; j < G->edge_count; j++) {
            int v = -1;
            if (G->edges[j].n_1 == u) v = G->edges[j].n_2;
            else if (G->edges[j].n_2 == u) v = G->edges[j].n_1;

            if (v != -1 && !visited[v]) {
                visited[v] = 1;
                queue[tail++] = v;
                count++;
            }
        }
    }
    free(queue);
    free(visited);
    return count == G->node_count;
}

static int dfs_cycle(Graph *G, int u, int p, int *visited, int *parent, int *cycle_nodes, int *cycle_len) {
    visited[u] = 1;
    for (int j = 0; j < G->edge_count; j++) {
        int v = -1;
        if (G->edges[j].n_1 == u) v = G->edges[j].n_2;
        else if (G->edges[j].n_2 == u) v = G->edges[j].n_1;

        if (v != -1 && v != p) {
            if (visited[v] == 1) {
                int curr = u;
                int len = 0;
                cycle_nodes[len++] = v;
                while (curr != v && curr != -1) {
                    cycle_nodes[len++] = curr;
                    curr = parent[curr];
                }
                *cycle_len = len;
                return 1;
            } else if (visited[v] == 0) {
                parent[v] = u;
                if (dfs_cycle(G, v, u, visited, parent, cycle_nodes, cycle_len)) {
                    return 1;
                }
            }
        }
    }
    visited[u] = 2;
    return 0;
}

int run_tutte(Graph *G) {
    if (G->node_count < 3) {
        return 3;
    }

    if (!is_connected(G)) {
        return 3;
    }

    int *visited = calloc(G->node_count, sizeof(int));
    int *parent = malloc(G->node_count * sizeof(int));
    for (int i = 0; i < G->node_count; i++) parent[i] = -1;

    int *cycle_nodes = malloc(G->node_count * sizeof(int));
    int cycle_len = 0;

    int cycle_found = dfs_cycle(G, 0, -1, visited, parent, cycle_nodes, &cycle_len);
    free(visited);
    free(parent);

    if (!cycle_found || cycle_len < 3) {
        free(cycle_nodes);
        return 3;
    }

    int *is_nail = calloc(G->node_count, sizeof(int));
    for (int i = 0; i < cycle_len; i++) {
        is_nail[cycle_nodes[i]] = 1;
    }

    int num_nails = cycle_len;
    int num_balloons = G->node_count - num_nails;

    int radius = 10;
    for (int i = 0; i < cycle_len; i++) {
        G->nodes[cycle_nodes[i]].x = radius * cos(i * ((2 * M_PI) / cycle_len));
        G->nodes[cycle_nodes[i]].y = radius * sin(i * ((2 * M_PI) / cycle_len));
    }

    if (num_balloons == 0) {
        free(cycle_nodes);
        free(is_nail);
        return 0;
    }

    int *balloon_matrix_idx = malloc(G->node_count * sizeof(int));
    int b_idx = 0;
    for (int i = 0; i < G->node_count; i++) {
        if (!is_nail[i]) {
            balloon_matrix_idx[i] = b_idx++;
        }
    }

    Matrix m = create_matrix(num_balloons);
    double *B_X = calloc(num_balloons, sizeof(double));
    double *B_Y = calloc(num_balloons, sizeof(double));
    double *w_x = calloc(num_balloons, sizeof(double));
    double *w_y = calloc(num_balloons, sizeof(double));

    for (int j = 0; j < G->edge_count; j++) {
        int n1 = G->edges[j].n_1;
        int n2 = G->edges[j].n_2;
        double w = G->edges[j].weight;

        if (!is_nail[n1]) {
            int idx1 = balloon_matrix_idx[n1];
            m.values[idx1][idx1] += w;
            if (!is_nail[n2]) {
                int idx2 = balloon_matrix_idx[n2];
                m.values[idx1][idx2] -= w;
            } else {
                B_X[idx1] += G->nodes[n2].x * w;
                B_Y[idx1] += G->nodes[n2].y * w;
            }
        }

        if (!is_nail[n2]) {
            int idx2 = balloon_matrix_idx[n2];
            m.values[idx2][idx2] += w;
            if (!is_nail[n1]) {
                int idx1 = balloon_matrix_idx[n1];
                m.values[idx2][idx1] -= w;
            } else {
                B_X[idx2] += G->nodes[n1].x * w;
                B_Y[idx2] += G->nodes[n1].y * w;
            }
        }
    }

    gauss_elimination(m, B_X, w_x);
    gauss_elimination(m, B_Y, w_y);

    for (int i = 0; i < G->node_count; i++) {
        if (!is_nail[i]) {
            G->nodes[i].x = w_x[balloon_matrix_idx[i]];
            G->nodes[i].y = w_y[balloon_matrix_idx[i]];
        }
    }

    free_matrix(m);
    free(B_X);
    free(B_Y);
    free(w_x);
    free(w_y);
    free(cycle_nodes);
    free(is_nail);
    free(balloon_matrix_idx);

    return 0;
}