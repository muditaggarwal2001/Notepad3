package com.example.mudit.notepad.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Note> ITEMS = new ArrayList<Note>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Note> ITEM_MAP = new HashMap<String, Note>();



    /**
     * A dummy item representing a piece of content.
     */
    public static class Note {
        public final String id;
        private String content;
        private String details;
        private String picpath;

        public Note(String id, String content, String details, String picpath) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.picpath=picpath;
        }

        public String getDetails() {
            return details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
