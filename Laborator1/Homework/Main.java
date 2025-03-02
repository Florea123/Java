public class Main {
    public static void main(String[] args) {
        int number_of_edges = 0;
        long t1 = System.currentTimeMillis();
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        int[][] m = new int[n][n];

        for(int i = 0; i < n; ++i) {
            for(int j = i + 1; j < n; ++j) {
                int r = (int)(Math.random() * (double)2.0F);
                m[i][j] = r;
                m[j][i] = r;
            }
        }

        for(int i = 0; i < k; ++i) {
            for(int j = i + 1; j < k; ++j) {
                m[i][j] = 1;
                m[j][i] = 1;
            }
        }

        for(int i = n - 1; i >= n - k; --i) {
            for(int j = i - 1; j >= n - k; --j) {
                m[i][j] = 0;
                m[j][i] = 0;
            }
        }

        for(int i = 0; i < n; ++i) {
            for(int j = i + 1; j < n; ++j) {
                if (m[i][j] == 1) {
                    ++number_of_edges;
                }
            }
        }

        int minimum = n;
        int maximum = 0;
        int sum_of_degrees = 0;

        for(int i = 0; i < n; ++i) {
            int number = 0;

            for(int j = 0; j < n; ++j) {
                if (m[i][j] == 1) {
                    ++number;
                    ++sum_of_degrees;
                }
            }

            if (minimum > number) {
                minimum = number;
            }

            if (maximum < number) {
                maximum = number;
            }
        }

        if (n < 30000) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < n; ++i) {
                for(int j = 0; j < n; ++j) {
                    if (m[i][j] == 1) {
                        sb.append("⬛");
                    } else {
                        sb.append("⬜");
                    }
                }

                sb.append("\n");
            }

            System.out.println(sb);
        }

        System.out.println("Numarul total de muchii este: " + number_of_edges);
        System.out.println("Δ(G)=: " + maximum);
        System.out.println("δ(G)=: " + minimum);
        System.out.println("Suma gradelor este: " + sum_of_degrees + " (trebuie sa fie 2*m = " + 2 * number_of_edges + ")");
        long t2 = System.currentTimeMillis();
        if (n >= 30000) {
            System.out.println(t2 - t1);
        }

    }
}
