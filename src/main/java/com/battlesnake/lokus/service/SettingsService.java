package com.battlesnake.lokus.service;

import com.battlesnake.codegen.models.SettingsSchema;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    public SettingsSchema getSettings() {
        return new SettingsSchema()
                .withApiversion("1")
                .withAuthor("") // TODO: Your Battlesnake Username
                .withColor("#32A852")
                .withHead("tiger-king")
                .withTail("coffee");
    }
}
