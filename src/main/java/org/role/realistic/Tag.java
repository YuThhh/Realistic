package org.role.realistic;

import net.kyori.adventure.text.format.NamedTextColor;

public class Tag {
    private final String name;
    private final NamedTextColor color;
    private int duration;
    private int amplifier;

    public Tag(String name, NamedTextColor color, int duration, int amplifier) {
        this.name = name;
        this.color = color;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public String getName() {return name;}
    public NamedTextColor getColor() {return color;}
    public int getDuration() {return duration;}
    public int getAmplifier() {return amplifier;}
    public void decreaseDuration(int ticks) { this.duration -= ticks; }
}
