package net.maxxqc.cserverupdater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class CurseForgeAPI
{
    private static final String CF_API_URL = "https://api.curseforge.com";

    private static final LinkedHashMap<Integer, Modpack> MODPACKS = new LinkedHashMap<>();

    public static List<Modpack> getAllModpacks(int page)
    {
        //TODO sortField isnt working?
        JsonArray response = get("/v1/mods/search?gameId=432&classId=4471&sortField=2&sortOrder=desc&index=" + page * 50);

        if (response == null) {
            return new ArrayList<>();
        }

        ArrayList<Modpack> modpacks = new ArrayList<>();

        for (JsonElement modpackElement : response) {
            JsonObject modpackJson = modpackElement.getAsJsonObject();

            int id = modpackJson.getAsJsonPrimitive("id").getAsInt();
            String name = modpackJson.getAsJsonPrimitive("name").getAsString();
            String summary = modpackJson.getAsJsonPrimitive("summary").getAsString();
            String thumbnailURL = modpackJson.getAsJsonObject("logo").getAsJsonPrimitive("thumbnailUrl").getAsString();

            long mainFileId = modpackJson.getAsJsonPrimitive("mainFileId").getAsLong();

            for (JsonElement latestFiles : modpackJson.getAsJsonArray("latestFiles")) {
                if (latestFiles.getAsJsonObject().getAsJsonPrimitive("id").getAsLong() == mainFileId) {
                    String fileName = latestFiles.getAsJsonObject().getAsJsonPrimitive("fileName").getAsString();
                    String version = fileName.contains("-") ? fileName.split("-")[1] : fileName;
                    version = version.replace(".zip", "");
                    Date releaseDate = Date.from(Instant.parse(latestFiles.getAsJsonObject().getAsJsonPrimitive("fileDate").getAsString()));

                    Modpack modpack = new Modpack(id, name, summary, thumbnailURL, version, releaseDate);
                    modpacks.add(modpack);

                    if (!MODPACKS.containsKey(id))
                    {
                        MODPACKS.put(id, modpack);
                    }

                    break;
                }
            }
        }

        return modpacks;
    }

    private static JsonArray get(String url)
    {
        HttpURLConnection connection = null;
        BufferedReader in = null;
        try
        {
            connection = (HttpURLConnection) new URL(CF_API_URL + url).openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("x-api-key", APIKey.CF_API_KEY);

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonElement jsonData = JsonParser.parseString(response.toString()).getAsJsonObject().get("data");
            JsonArray dataArray;
            if (jsonData.isJsonArray()) {
                dataArray = jsonData.getAsJsonArray();
            } else {
                dataArray = new JsonArray();
                dataArray.add(jsonData.getAsJsonObject());
            }

            return dataArray;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }

            if (in != null) {
                try
                {
                    in.close();
                }
                catch (IOException ignored) {}
            }
        }
    }
}