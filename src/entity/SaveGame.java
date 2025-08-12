package entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.GamePanel;

import java.io.*;

public class SaveGame {


    class SaveData{
        int highscore;
        float volume;
        SaveData(int highscore, float volume){
            this.highscore = highscore;
            this.volume = volume;
        }

    }

    Gson gson = new Gson();

    public void savegame(){
        SaveData saveData = new SaveData(GamePanel.highscore, GamePanel.volume);
        try (FileWriter fileWriter = new FileWriter("savegame.json")){
            gson.toJson(saveData, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getHighscore() throws IOException {
        File saveFile = new File("savegame.json");
        if (!saveFile.exists() || saveFile.length() == 0) {
            savegame();
            return GamePanel.highscore;
        }


            try (Reader reader = new FileReader("savegame.json")) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                if (jsonObject.has("highscore") && !jsonObject.get("highscore").isJsonNull()) {
                    return jsonObject.get("highscore").getAsInt();
                }
            } catch (IOException e) {
                savegame();
            }

        return GamePanel.highscore;
    }

}
