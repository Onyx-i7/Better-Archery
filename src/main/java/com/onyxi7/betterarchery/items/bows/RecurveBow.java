package com.onyxi7.betterarchery.items.bows;

public class RecurveBow extends CustomBow {
    
    public RecurveBow(String name) {
        super(name, 256);
        this.pullBackMult = 0.9F;      // Un poco más rápido
        this.damageMult = 1.1F;        // Más daño
        this.arrowSpeedMult = 1.25F;   // Más velocidad
    }
}
