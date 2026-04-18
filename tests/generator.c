#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>

typedef struct { double x, y; } Point;

typedef struct {
    int u, v;
    double dist;
    double weight;
} Edge;

int find(int i, int *parent) {
    if (parent[i] == i) return i;
    return parent[i] = find(parent[i], parent);
}

void union_set(int i, int j, int *parent) {
    int root_i = find(i, parent);
    int root_j = find(j, parent);
    if (root_i != root_j) parent[root_i] = root_j;
}

int orientation(Point p, Point q, Point r) {
    double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
    if (fabs(val) < 1e-6) return 0;
    return (val > 0) ? 1 : 2;
}

bool do_intersect(Point p1, Point q1, Point p2, Point q2) {
    if ((p1.x == p2.x && p1.y == p2.y) || (p1.x == q2.x && p1.y == q2.y) ||
        (q1.x == p2.x && q1.y == p2.y) || (q1.x == q2.x && q1.y == q2.y)) {
        return false;
    }

    int o1 = orientation(p1, q1, p2);
    int o2 = orientation(p1, q1, q2);
    int o3 = orientation(p2, q2, p1);
    int o4 = orientation(p2, q2, q1);

    if (o1 != o2 && o3 != o4) return true;
    return false;
}

double rand_double(double min, double max) {
    return min + ((double)rand() / RAND_MAX) * (max - min);
}

int compare_edges(const void *a, const void *b) {
    Edge *e1 = (Edge *)a;
    Edge *e2 = (Edge *)b;
    if (e1->dist < e2->dist) return -1;
    if (e1->dist > e2->dist) return 1;
    return 0;
}

int main(int argc, char *argv[]) {
    srand(time(NULL));

    if (argc != 6) {
        printf("Uzycie: %s <wierzcholki> <krawedzie> <minWaga> <maxWaga> <plik>\n", argv[0]);
        return 1;
    }

    int V = atoi(argv[1]);
    int E_target = atoi(argv[2]);
    double min_w = atof(argv[3]);
    double max_w = atof(argv[4]);
    char *filename = argv[5];

    int max_edges = (V >= 3) ? (3 * V - 6) : (V == 2 ? 1 : 0);
    if (E_target > max_edges || E_target < V - 1) {
        printf("Blad: Niemozliwe parametry dla spojnego grafu planarnego.\n");
        printf("Dla V=%d, krawedzie musza byc w przedziale [%d, %d]\n", V, V - 1, max_edges);
        return 1;
    }

    Point *pts = malloc(V * sizeof(Point));
    for (int i = 0; i < V; i++) {
        pts[i].x = rand_double(10.0, 990.0);
        pts[i].y = rand_double(10.0, 990.0);
    }

    int max_possible_edges = V * (V - 1) / 2;
    Edge *all_edges = malloc(max_possible_edges * sizeof(Edge));
    int edge_idx = 0;
    
    for (int i = 0; i < V; i++) {
        for (int j = i + 1; j < V; j++) {
            all_edges[edge_idx].u = i;
            all_edges[edge_idx].v = j;
            double dx = pts[i].x - pts[j].x;
            double dy = pts[i].y - pts[j].y;
            all_edges[edge_idx].dist = sqrt(dx * dx + dy * dy);
            edge_idx++;
        }
    }

    qsort(all_edges, max_possible_edges, sizeof(Edge), compare_edges);

    Edge *final_edges = malloc(E_target * sizeof(Edge));
    int final_count = 0;
    int *parent = malloc(V * sizeof(int));
    for (int i = 0; i < V; i++) parent[i] = i;

    for (int i = 0; i < max_possible_edges && final_count < V - 1; i++) {
        int u = all_edges[i].u;
        int v = all_edges[i].v;
        if (find(u, parent) != find(v, parent)) {
            union_set(u, v, parent);
            all_edges[i].weight = rand_double(min_w, max_w);
            final_edges[final_count++] = all_edges[i];
            all_edges[i].dist = -1;
        }
    }

    for (int i = 0; i < max_possible_edges && final_count < E_target; i++) {
        if (all_edges[i].dist == -1) continue;

        int u = all_edges[i].u;
        int v = all_edges[i].v;
        bool intersects = false;

        for (int j = 0; j < final_count; j++) {
            int fu = final_edges[j].u;
            int fv = final_edges[j].v;
            if (do_intersect(pts[u], pts[v], pts[fu], pts[fv])) {
                intersects = true;
                break;
            }
        }

        if (!intersects) {
            all_edges[i].weight = rand_double(min_w, max_w);
            final_edges[final_count++] = all_edges[i];
        }
    }

    if (final_count < E_target) {
        printf("Uwaga: Wygenerowano tylko %d z %d krawedzi (gesty graf, zla losowosc).\n", final_count, E_target);
    }

    FILE *f = fopen(filename, "w");
    if (!f) {
        printf("Blad zapisu do pliku.\n");
        return 1;
    }

    for (int i = 0; i < final_count; i++) {
        int id1 = final_edges[i].u + 1;
        int id2 = final_edges[i].v + 1;
        int min_id = (id1 < id2) ? id1 : id2;
        int max_id = (id1 > id2) ? id1 : id2;
        
        fprintf(f, "E%d_%d %d %d %.3f\n", min_id, max_id, min_id, max_id, final_edges[i].weight);
    }

    fclose(f);
    printf("Sukces! Wygenerowano graf planarny i zapisano do %s\n", filename);

    free(pts);
    free(all_edges);
    free(final_edges);
    free(parent);

    return 0;
}