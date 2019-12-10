package com.ex.cy.demo4.alg.algthink.traceback;

public class _8Queen {
    //回溯法，暴力解8皇后
    private static int ways = 0;
    //返回解法个数
    public static int f8queen() {
        int[][] board = new int[8][8];
        ways = 0;
        p_f8queen(board, 0);
        return ways;
    }

    private static void p_f8queen(int[][] board, int row) {
        if (row > 7) {
            ways++;
            printBoard(board);              //最后一行穷举完，输出棋盘
            return;
        }
        for (int col = 0; col < 8; col++) {
            if (check(board, row, col)) {    //检测是否符合规则
                board[row][col] = 1;         //放皇后棋子
                p_f8queen(board, row + 1);//进入下一行
                board[row][col] = 0;         //清除
            }
        }
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != 1)
                    System.out.print('.');  //.为棋盘空白格
                else
                    System.out.print('Q');  //Q为皇后棋子
            }
            System.out.println();
        }
        System.out.println("==============");
    }

    private static boolean check(int[][] board, int row, int col) {
        for (int tmprow = row - 1; tmprow >= 0; tmprow--) {
            int l = col - (row - tmprow);
            int r = col + (row - tmprow);
            if (l > -1 && board[tmprow][l] != 0)    //左上角线
                return false;
            if (r < 8 && board[tmprow][r] != 0)     //右上角线
                return false;
            if (board[tmprow][col] != 0)            //顶部线
                return false;
        }
        return true;
    }

    public static void main(String[] ar) {
        System.out.println(f8queen());
    }
}
