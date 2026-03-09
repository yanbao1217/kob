package com.example.backend.consumer.utils;


import com.alibaba.fastjson2.JSONObject;
import com.example.backend.consumer.WebSocketServer;
import com.example.backend.pojo.Record;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int[][] g;
    final private int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();

    private String status = "playing";
    private String loser = ""; // all：平局，A：A输，B：B输

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return this.playerA;
    }
    public Player getPlayerB() {
        return this.playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }

    }

    public int[][] getG() {
        return g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; ++ i) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean draw() { // 画地图
        for (int i = 0; i < this.rows; ++ i) {
            for (int j = 0; j < this.cols; ++ j) {
                g[i][j] = 0;
            }
        }

        // 给四周加上障碍物
        for (int r = 0; r < this.rows; ++ r) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; ++ c) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        // 创建随机障碍物
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; ++ i) {
            for (int j = 0; j < 1000; ++j) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if (r == this.rows - 2 && c == 1) continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1 , 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; ++ i) {
            if (draw()) {
                break;
            }
        }
    }

    private boolean nextStep() { // 等待两名玩家的下一步操作
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 50; ++ i) {
            try {
              Thread.sleep(200);
              lock.lock();
              try {
                  if (nextStepA != null && nextStepB != null) {
                      playerA.getSteps().add(nextStepA);
                      playerB.getSteps().add(nextStepB);
                      return true;
                  }
              } finally {
                  lock.unlock();
              }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell head = cellsA.get(n - 1);
        if (g[head.x][head.y] == 1) return false;

        for (int i = 0; i < n - 1; ++ i) {
            if (cellsA.get(i).x == head.x && cellsA.get(i).y == head.y) {
                return false;
            }
        }

        for (int i = 0; i < n - 1; ++ i) {
            if (cellsB.get(i).x == head.x && cellsB.get(i).y == head.y) {
                return false;
            }
        }

        return true;
    }

    private void judge() { // 判断两名玩家下一步操作是否合法
        List<Cell> cellsA = getPlayerA().getCells();
        List<Cell> cellsB = getPlayerB().getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);

        if (!validA || !validB) {
            status = "finished";

            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else if (!validB) {
                loser = "B";
            }
        }
    }

    private void sendAllMessage(String message) {
        if (playerA.getId() != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (playerB.getId() != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void sendMove() { // 向两个Client发送移动信息
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    public String mapToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[i].length; j++) {
                sb.append(g[i][j]);
            }
        }
        return sb.toString();
    }

    private void saveToDatabase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.stepsToString(),
                playerB.stepsToString(),
                mapToString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult() { // 向两个client发送结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; ++ i) {
            if (nextStep()) { // 是否获取了两条蛇的下一步操作
                judge();
                if ("playing".equals(status)) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
