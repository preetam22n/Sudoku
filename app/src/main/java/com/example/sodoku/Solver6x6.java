package com.example.sodoku;

class Solver6x6 {

    public int[][] result;

    public Solver6x6() {
        result = new int[6][6];
    }

    int isSafe(int[][] a, int x, int y, int k) {
        // Row check:
        for(int i=0; i<6; i++)
            if(a[x][i]==k && i!=y)
                return 0;
        // Column check:
        for(int i=0; i<6; i++)
            if(a[i][y]==k && i!=x)
                return 0;
        // Box check:
        if(x%2==0 && y%3==0)
            if(a[x+1][y]==k || a[x][y+1]==k || a[x+1][y+1]==k || a[x][y+2]==k || a[x+1][y+2]==k)
                return 0;
        if(x%2==0 && y%3==1)
            if(a[x+1][y]==k || a[x][y-1]==k || a[x+1][y-1]==k || a[x][y+1]==k || a[x+1][y+1]==k)
                return 0;
        if(x%2==0 && y%3==2)
            if(a[x][y-1]==k || a[x][y-2]==k || a[x+1][y]==k || a[x+1][y-1]==k || a[x+1][y-2]==k)
                return 0;
        if(x%2==1 && y%3==0)
            if(a[x-1][y]==k || a[x-1][y+1]==k || a[x-1][y+2]==k || a[x][y+1]==k || a[x][y+2]==k)
                return 0;
        if(x%2==1 && y%3==1)
            if(a[x-1][y-1]==k || a[x-1][y]==k || a[x-1][y+1]==k || a[x][y-1]==k || a[x][y+1]==k)
                return 0;
        if(x%2==1 && y%3==2)
            if(a[x-1][y-2]==k || a[x-1][y-1]==k || a[x-1][y]==k || a[x][y-1]==k || a[x][y-2]==k)
                return 0;
        // All fine (safe)
        return 1;
    }

    // Put the solved sodoku in the result array
    void setResult(int[][] a) {
        for (int i = 0; i < 6; i++)
            System.arraycopy(a[i], 0, result[i], 0, 6);
    }

    // SODOKU solver function
    int solve(int[][] a, int x, int y) {
        int flag=0, k=1;
        // Skip the non empty cell
        if(a[x][y]!=0) {
            if(y<5)
                flag = solve(a, x, y+1);
            else if(x<5 && y==5)
                flag = solve(a, x+1, 0);
            else if(x==5 && y==5) {
                flag = 1;
                setResult(a);
            }
        }
        // Check for possible values in empty cells
        else {
            while(k<=6) {
                flag = 0;
                if(isSafe(a,x,y,k) == 1) {
                    a[x][y] = k;
                    if(y<5)
                        flag = solve(a, x, y+1);
                    else if(x<5 && y==5)
                        flag = solve(a, x+1, 0);
                    else if(x==5 && y==5) {
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
