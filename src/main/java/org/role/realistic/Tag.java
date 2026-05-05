package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;

public class Tag {
    private final String name;
    private final NamedTextColor color;
    private int duration;

    public Tag(String name, NamedTextColor color, int duration) {
        this.name = name;
        this.color = color;
        this.duration = duration;
    }

    public String getName() {return name;}
    public NamedTextColor getColor() {return color;}
    public int getDuration() {return duration;}
    public void decreaseDuration(int ticks) { this.duration -= ticks; }
}
