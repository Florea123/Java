public class Main {
    private static int[][] m;
    private static int n;

    public static int bkt_stabil(int vector[],int k,int index,int start)
    {
        if(index==k)
        {
            for(int i=0;i<k;i++)
                for(int j=i+1;j<k;j++)
                    if(m[vector[i]][vector[j]]==1)
                        return 0;
            return 1;
        }
        if(index>=k)
            return 0;
        {
            for(int i=start;i<n;i++)
            {
                vector[index]=i;
                if(bkt_stabil(vector,k,index+1,i+1)==1)
                    return 1;
            }
            return 0;
        }
    }

    public static int bkt_clica(int vector[],int k,int index,int start)
    {
        if(index==k)
        {
            for(int i=0;i<k;i++)
                for(int j=i+1;j<k;j++)
                    if(m[vector[i]][vector[j]]==1)
                        return 0;
            return 1;
        }
        if(index>=k)
            return 0;
        {
            for(int i=start;i<n;i++)
            {
                vector[index]=i;
                if(bkt_clica(vector,k,index+1,i+1)==1)
                    return 1;
            }
            return 0;
        }
    }
    public static void main(String[] args) {
        int k;
        n=Integer.parseInt(args[0]);
        k=Integer.parseInt(args[1]);
        m=new int[n][n];
        for(int i = 0; i < n; ++i) {
            for(int j = i + 1; j < n; ++j) {
                int r = (Math.random() < 0.5) ? 0 : 1;
                m[i][j] = r;
                m[j][i] = r;
            }
        }
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

        for(int i=k;i<=n;i++) {
            int[] vector=new int[i];
            if (bkt_clica(vector, i,0,0) == 1) {
                System.out.println("Am gasit clica de:" + i);
                break;
            }
        }

        for(int i=k;i<=n;i++) {
            int[] vector=new int[i];
            if (bkt_stabil(vector, i,0,0) == 1) {
                System.out.println("Am gasit un set stabil de:" + i);
                break;
            }
        }
    }
}