package com.onyxi7.betterarchery.items.bows;

public class YumiBow extends CustomBow {
    
    public YumiBow(String name) {
        super(name, 384);
        this.pullBackMult = 1.15F;     // Un poco más lento
        this.damageMult = 1.0F;
        this.arrowSpeedMult = 1.5F;    // Más velocidad
    }
}
