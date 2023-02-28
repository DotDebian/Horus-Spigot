package io.servertap.utils.storage;

import com.google.gson.reflect.TypeToken;
import io.servertap.PluginEntrypoint;
import io.servertap.utils.Cryptography;
import io.servertap.utils.GsonSingleton;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    static PlayerData instance;
    File playerDataFile;

    HashMap<String, String> playerPasswordMap = new HashMap<>();

    private PlayerData() {
        playerDataFile = new File(PluginEntrypoint.instance.getDataFolder().getPath(), "player_data.json");
    }

    public static PlayerData initialize() {
        if (instance != null) {
            PluginEntrypoint.instance.getLogger().severe("PlayersData has already been initialized.");
        } else {
            instance = new PlayerData();
            instance.load();
        }

        return instance;
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(playerDataFile);
            fileWriter.write(GsonSingleton.getInstance().toJson(playerPasswordMap));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br
                     = new BufferedReader(new FileReader(playerDataFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            playerPasswordMap = GsonSingleton.getInstance().fromJson(
                resultStringBuilder.toString(),
                new TypeToken<HashMap<String, String>>() {}.getType()
            );
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                return;
            }

            PluginEntrypoint.instance.getLogger().severe("Encountered an error while deserializing players data: " + e.getMessage());
        }
    }

    public boolean editPlayer(UUID player, String newPassword) {
        playerPasswordMap.put(player.toString(), Cryptography.encrypt(newPassword));
        return true;
    }

}
