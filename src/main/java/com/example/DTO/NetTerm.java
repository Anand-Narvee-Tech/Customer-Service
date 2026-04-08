package com.example.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum NetTerm {

    NET_7(7, "1 Week"),
    NET_14(14, "2 Weeks"),
    NET_30(30, "1 Month"),
    NET_45(45, "45 Days"),
    NET_60(60, "60 Days"),
    NET_75(75, "75 Days"),
    NET_90(90, "90 Days"),
    NET_120(120, "120 Days");

    private final int days;
    private final String label;

    NetTerm(int days, String label) {
        this.days = days;
        this.label = label;
    }

    public int getDays() {
        return days;
    }

    public String getLabel() {
        return label;
    }

    // ✅ ACCEPT MULTIPLE INPUT FORMATS
    @JsonCreator
    public static NetTerm fromValue(Object value) {

        if (value == null) return null;

        String input = value.toString().trim();

        // 1️⃣ Match ENUM name (NET_30)
        for (NetTerm term : NetTerm.values()) {
            if (term.name().equalsIgnoreCase(input)) {
                return term;
            }
        }

        // 2️⃣ Match days (30)
        try {
            int days = Integer.parseInt(input);
            return Arrays.stream(NetTerm.values())
                    .filter(t -> t.days == days)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid NetTerm: " + input));
        } catch (NumberFormatException ignored) {}

        // 3️⃣ Match label ("1 Month")
        return Arrays.stream(NetTerm.values())
                .filter(t -> t.label.equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid NetTerm: " + input));
    }

    // ✅ CONTROL RESPONSE FORMAT
    @JsonValue
    public String toJson() {
        return this.name(); // OR return label if you want "1 Month"
    }
}