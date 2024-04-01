package com.guchiwo0608.rfidproject.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import impinj.ImpinjTagReader;
import sql.DatabaseDriver;

@RestController
@RequestMapping("/rfid_project")
public class RfidController {

    @GetMapping("/communication-data/{hostname}")
    public String getCommunicationData(@PathVariable("hostname") String hostname) {
        ImpinjTagReader.readTagsPeriodicTrigger(hostname, new short[] { 1, 2 });
        JsonArray json = new JsonArray();
        try {
            Gson gson = new Gson();
            json = gson.fromJson(Files.readString(Paths.get("src/main/java/com/temp_dir/temp.json")), JsonArray.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Path p = Paths.get("src/main/java/com/temp_dir/temp.json");
            Files.delete(p);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (json == null) {
            return (new JsonObject()).toString();
        }
        return json.toString();
    }

    @GetMapping("/database/{table_name}")
    public ArrayList<HashMap<String, Object>> getData(@PathVariable("table_name") String table_name) throws Exception {
        DatabaseDriver dbdriver = new DatabaseDriver(table_name);
        ResultSet rs = dbdriver.getData(table_name);
        ResultSetMetaData md = rs.getMetaData();
        int columnsCount = md.getColumnCount();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>();
            for (int i = 1; i < columnsCount; i++) {
                row.put(md.getColumnName(i), rs.getString(i));
            }
            list.add(row);
        }
        return list;
    }

}
