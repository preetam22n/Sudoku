package com.example.sodoku;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SolverActivity4x4 extends AppCompatActivity {

    private int posX, posY;        // Position of selected cell
    private int[][] grid;          // The sodoku in grid representation
    private int[][] ivStat;        // Status for each cell and its contents
    private ImageView[][] imgView; // The image views for all cells
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver_4x4);

        // Initialize
        posX = posY = -1;
        grid = new int[4][4];
        ivStat = new int[4][4];
        imgView = new ImageView[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                imgView[i][j] = findViewById(this.getResources().getIdentifier("iv" + i + j, "id", this.getPackageName()));
        setIvStat(-1, -1, 0, 0);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    for (int i = 0; i < 4; i++)
                        for (int j = 0; j < 4; j++) {
                            setIvStat(i, j, 0, grid[i][j]);
                            draw(i, j);
                        }
                } else if (msg.what == 0)
                    Toast.makeText(SolverActivity4x4.this, "Not Solvable", Toast.LENGTH_LONG).show();
                else if (msg.what == 2)
                    Toast.makeText(SolverActivity4x4.this, "Check input", Toast.LENGTH_LONG).show();
            }
        };
    }

    /*
     * Sets the ivStat code as per the given parameters
     * if x and y are both -1 then set default value for all cells (blank bg)
     * if col val is 0 set default color value for that cell
     * num value is used for setting the required number in that cell
     * Color codes:
     * 0 for default (white 1 or grey 2)
     * Note: Do not use 1 or 2 directly for col value
     * 3 for selected (blue)
     * 4 for error (red)
     */
    private void setIvStat(int x, int y, int col, int num) {
        int tx, ty;
        if (x == -1 && y == -1) {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++) {
                    tx = i % 4;
                    ty = j % 4;
                    if ((ty == 0 || ty == 1) && (tx == 0 || tx == 1))
                        ivStat[i][j] = 10;
                    else if ((ty == 2 || ty == 3) && (tx == 2 || tx == 3))
                        ivStat[i][j] = 10;
                    else
                        ivStat[i][j] = 20;
                }
            return;
        }
        if (col == 0) {
            tx = x % 4;
            ty = y % 4;
            if ((ty == 0 || ty == 1) && (tx == 0 || tx == 1))
                ivStat[x][y] = 10 + num;
            else if ((ty == 2 || ty == 3) && (tx == 2 || tx == 3))
                ivStat[x][y] = 10 + num;
            else
                ivStat[x][y] = 20 + num;
        } else
            ivStat[x][y] = col * 10 + num;
    }

    /*
     * Executed on tapping on a cell in the sodoku grid
     * When a cell is tapped it updates the location of
     * currently selected cell and also calls the setIvStat
     * to update the background colors of the current as well
     * as the previously selected cells
     */
    public void getSelected(View view) {
        int pre, col;
        if (posX != -1 && posY != -1) {
            col = (ivStat[posX][posY] / 10 == 4) ? 4 : 0;
            setIvStat(posX, posY, col, grid[posX][posY]);
            draw(posX, posY);
        }
        boolean flag = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                if (view.getId() == imgView[i][j].getId()) {
                    posX = i;
                    posY = j;
                    flag = true;
                    break;
                }
            if (flag) break;
        }
        pre = ivStat[posX][posY] / 10;
        setIvStat(posX, posY, 3, grid[posX][posY]);
        draw(posX, posY);
        setIvStat(posX, posY, pre, grid[posX][posY]);
    }

    // Function to be executed on pressing any numeric buttons
    public void setNumber(View view) {
        int pre;
        int id = Integer.parseInt(view.getTag().toString());
        if (posX != -1 && posY != -1 && (ivStat[posX][posY] / 10) != 4) {
            grid[posX][posY] = id;
            setIvStat(posX, posY, 3, id);
            draw(posX, posY);
        } else if (posX != -1 && posY != -1 && (ivStat[posX][posY] / 10) == 4) {
            if ((ivStat[posX][posY] % 10) != id) {
                grid[posX][posY] = id;
                safeCheck(posX, posY);
                pre = ivStat[posX][posY] / 10;
                setIvStat(posX, posY, 3, grid[posX][posY]);
                draw(posX, posY);
                if (pre == 4)
                    setIvStat(posX, posY, 4, grid[posX][posY]);
            }
        }
    }

    // Function to be executed on pressing the erase button
    public void clearCell(View view) {
        if (posX != -1 && posY != -1) {
            grid[posX][posY] = 0;
            setIvStat(posX, posY, 3, 0);
            draw(posX, posY);
            safeCheck(posX, posY);
        }
    }

    // Function to be executed on pressing the reset button
    public void resetGrid(View view) {
        posX = posY = -1;
        setIvStat(-1, -1, 0, 0);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                grid[i][j] = 0;
                draw(i, j);
            }
    }

    // Function to be executed on pressing the solve button
    public void calculate(View view) {
        posX = posY = -1;
        safeCheck(-1, -1);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if ((ivStat[i][j] / 10) == 4) {
                    handler.sendEmptyMessage(2);
                    return;
                }
        Thread thread = new Thread() {
            Solver4x4 obj = new Solver4x4();

            @Override
            public void run() {
                int status;
                status = obj.solve(grid, 0, 0);
                if (status == 1) {
                    for (int i = 0; i < 4; i++)
                        System.arraycopy(obj.result, 0, grid, 0, 4);
                    handler.sendEmptyMessage(1);
                } else if (status == 0)
                    handler.sendEmptyMessage(0);
            }
        };
        thread.start();
    }

    /*
     * Marks the cells with obvious mistakes by
     * a red background color. Takes the cell
     * index as input and marks all cells affected
     * by this color. Checks all cells for possible
     * errors when -1 is passed for both cell index
     */
    private void safeCheck(int x, int y) {
        int k, pre, post;
        Solver4x4 obj = new Solver4x4();
        if (x == -1 && y == -1) {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    if (grid[i][j] != 0) {
                        pre = ivStat[i][j];
                        if (grid[i][j] != 0) {
                            k = obj.isSafe(grid, i, j, grid[i][j]);
                            if (k == 0) setIvStat(i, j, 4, grid[i][j]);
                            else setIvStat(i, j, 0, grid[i][j]);
                            post = ivStat[i][j];
                            if (pre != post) draw(i, j);
                        }
                    }
            return;
        }
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if (x + i >= 0 && x + i <= 3 && y + j >= 0 && y + j <= 3) {
                    if (i == 0 || j == 0) continue;
                    if (grid[x + i][y + j] != 0) {
                        pre = ivStat[x + i][y + j];
                        k = obj.isSafe(grid, x + i, y + j, grid[x + i][y + j]);
                        if (k == 0) setIvStat(x + i, y + j, 4, grid[x + i][y + j]);
                        else setIvStat(x + i, y + j, 0, grid[x + i][y + j]);
                        post = ivStat[x + i][y + j];
                        if (pre != post) draw(x + i, y + j);
                    }
                }
        for (int i = 0; i < 4; i++) {
            if (i == y) continue;
            if (grid[x][i] != 0) {
                pre = ivStat[x][i];
                k = obj.isSafe(grid, x, i, grid[x][i]);
                if (k == 0) setIvStat(x, i, 4, grid[x][i]);
                else setIvStat(x, i, 0, grid[x][i]);
                post = ivStat[x][i];
                if (pre != post) draw(x, i);
            }
        }
        for (int j = 0; j < 4; j++) {
            if (j == x) continue;
            if (grid[j][y] != 0) {
                pre = ivStat[j][y];
                k = obj.isSafe(grid, j, y, grid[j][y]);
                if (k == 0) setIvStat(j, y, 4, grid[j][y]);
                else setIvStat(j, y, 0, grid[j][y]);
                post = ivStat[j][y];
                if (pre != post) draw(j, y);
            }
        }
        if (grid[x][y] != 0) {
            k = obj.isSafe(grid, x, y, grid[x][y]);
            if (k == 0) setIvStat(x, y, 4, grid[x][y]);
            else setIvStat(x, y, 0, grid[x][y]);
        }
    }

    /*
     * Draw the required image in the given cell
     * Draw properties are taken from ivStat
     */
    private void draw(int x, int y) {
        int col = ivStat[x][y] / 10;
        int num = ivStat[x][y] % 10;
        switch (col) {
            case 1:
                switch (num) {
                    case 0:
                        imgView[x][y].setImageResource(R.drawable.background_white_big);
                        break;
                    case 1:
                        imgView[x][y].setImageResource(R.drawable.one_big_white);
                        break;
                    case 2:
                        imgView[x][y].setImageResource(R.drawable.two_big_white);
                        break;
                    case 3:
                        imgView[x][y].setImageResource(R.drawable.three_big_white);
                        break;
                    case 4:
                        imgView[x][y].setImageResource(R.drawable.four_big_white);
                        break;
                }
                break;
            case 2:
                switch (num) {
                    case 0:
                        imgView[x][y].setImageResource(R.drawable.background_grey_big);
                        break;
                    case 1:
                        imgView[x][y].setImageResource(R.drawable.one_big_grey);
                        break;
                    case 2:
                        imgView[x][y].setImageResource(R.drawable.two_big_grey);
                        break;
                    case 3:
                        imgView[x][y].setImageResource(R.drawable.three_big_grey);
                        break;
                    case 4:
                        imgView[x][y].setImageResource(R.drawable.four_big_grey);
                        break;
                }
                break;
            case 3:
                switch (num) {
                    case 0:
                        imgView[x][y].setImageResource(R.drawable.background_selected_big);
                        break;
                    case 1:
                        imgView[x][y].setImageResource(R.drawable.one_big_blue);
                        break;
                    case 2:
                        imgView[x][y].setImageResource(R.drawable.two_big_blue);
                        break;
                    case 3:
                        imgView[x][y].setImageResource(R.drawable.three_big_blue);
                        break;
                    case 4:
                        imgView[x][y].setImageResource(R.drawable.four_big_blue);
                        break;
                }
                break;
            case 4:
                switch (num) {
                    case 1:
                        imgView[x][y].setImageResource(R.drawable.one_big_red);
                        break;
                    case 2:
                        imgView[x][y].setImageResource(R.drawable.two_big_red);
                        break;
                    case 3:
                        imgView[x][y].setImageResource(R.drawable.three_big_red);
                        break;
                    case 4:
                        imgView[x][y].setImageResource(R.drawable.four_big_red);
                        break;
                }
                break;
        }
    }
}