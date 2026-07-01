package com.onyxi7.betterarchery.items.bows;

public class CompositeBow extends CustomBow {
    
    public CompositeBow(String name) {
        super(name, 1024);
        this.pullBackMult = 0.5F;      // Muy rápido
        this.damageMult = 1.25F;       // Más daño
        this.arrowSpeedMult = 0.65F;   // Flechas más lentas
    }
}
