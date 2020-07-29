package com.example.sodoku;

class Solver4x4{

    int[][] result;
    int status;

    public Solver4x4() {
        status = 2; // Initialized
        result = new int[4][4];
    }

    int isSafe(int[][] a, int x, int y, int k) {
        // Row check:
        for(int i=0; i<4; i++)
            if(a[x][i]==k && i!=y)
                return 0;
        // Column check:
        for(int i=0; i<4; i++)
            if(a[i][y]==k && i!=x)
                return 0;
        // Box check:
        if(x%2==0 && y%2==0)
            if(a[x+1][y]==k || a[x][y+1]==k || a[x+1][y+1]==k)
                return 0;
        if(x%2==0 && y%2!=0)
            if(a[x+1][y]==k || a[x][y-1]==k || a[x+1][y-1]==k)
                return 0;
        if(x%2!=0 && y%2==0)
            if(a[x-1][y]==k || a[x][y+1]==k || a[x-1][y+1]==k)
                return 0;
        if(x%2!=0 && y%2!=0)
            if(a[x-1][y]==k || a[x][y-1]==k || a[x-1][y-1]==k)
                return 0;
        // All fine (safe)
        return 1;
    }

    void setResult(int[][] a) {
        for (int i = 0; i < 4; i++)
            System.arraycopy(a[i], 0, result[i], 0, 4);
    }

    int solve(int[][] a, int x, int y) {
        int flag=0, k=1;
        // Skip the non empty cell
        if(a[x][y]!=0) {
            if(y<3)
                flag = solve(a, x, y+1);
            else if(x<3 && y==3)
                flag = solve(a, x+1, 0);
            else if(x==3 && y==3) {
                flag = 1;
                setResult(a);
            }
        }
        // Check for possible values in empty cells
        else {
            while(k<=4) {
                flag = 0;
                if(isSafe(a,x,y,k) == 1) {
                    a[x][y] = k;
                    if(y<3)
                        flag = solve(a, x, y+1);
                    else if(x<3 && y==3)
                        flag = solve(a, x+1, 0);
                    else if(x==3 && y==3) {
                        flag = 1;
                        setResult(a);
                    }
                }
                if(flag==1) break;
                a[x][y] = 0;
                k++;
            }
        }
        return flag;
    }
}
