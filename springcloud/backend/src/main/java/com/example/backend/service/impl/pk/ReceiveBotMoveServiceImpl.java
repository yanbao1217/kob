package com.example.backend.service.impl.pk;

import com.example.backend.consumer.WebSocketServer;
import com.example.backend.consumer.utils.Game;
import com.example.backend.service.pk.ReceiveBotMoveService;

public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId + " " + direction + " ");
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getBotId().equals(userId)) {// 亲自出马
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)){
                    if (game.getPlayerB().getBotId().equals(-1)) // 亲自出马
                        game.setNextStepB(direction);
                }
            }
        }
        return "receive bot move success";
    }
}
