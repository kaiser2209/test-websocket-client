package com.example.testwebsocketclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {

    public static class CreateJson {
        private JsonObject json;

        public static CreateJson getInstance() {
            return new CreateJson();
        }

        public CreateJson addProperty(String property, String value) {
            if(this.json == null) {
                json = new JsonObject();
            }

            json.addProperty(property, value);
            return this;
        }

        public CreateJson addProperty(String property, Number value) {
            if(this.json == null) {
                json = new JsonObject();
            }

            json.addProperty(property, value);
            return this;
        }

        public CreateJson addProperty(String property, Boolean value) {
            if(this.json == null) {
                json = new JsonObject();
            }

            json.addProperty(property, value);
            return this;
        }

        public CreateJson addProperty(String property, Character value) {
            if(this.json == null) {
                json = new JsonObject();
            }

            json.addProperty(property, value);
            return this;
        }

        public CreateJson add(String property, JsonElement value) {
            if(this.json == null) {
                json = new JsonObject();
            }

            json.add(property, value);
            return this;
        }

        public String build() {
            return this.json.toString();
        }

        public JsonObject getJson() {
            return this.json;
        }

    }
}
