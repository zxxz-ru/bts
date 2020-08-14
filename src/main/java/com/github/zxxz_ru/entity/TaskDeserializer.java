package com.github.zxxz_ru.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskDeserializer extends JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<User> userList = new ArrayList<>();
        // ObjectNode node = ctxt.getNodeFactory().objectNode();
        JsonNode node = p.getCodec().readTree(p);
        int id = (node.get("id")).asInt();
        String theme = node.get("thema").asText();
        String priority = node.get("priority").asText();
        String taskType = node.get("taskType").asText();
        String description = node.get("description").asText();
        Task res = new Task(id, theme, priority, taskType, description);
        ArrayNode users = (ArrayNode) node.get("userList");
        p.setCurrentValue(users);
        Iterator<User> iter = p.readValuesAs(User.class);
        if (iter == null) {
            return res;
        }
        while (iter.hasNext()) {
            userList.add(iter.next());
        }
        res.setUserList(userList);
        return res;
    }
}