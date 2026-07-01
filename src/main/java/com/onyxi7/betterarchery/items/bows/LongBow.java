package com.onyxi7.betterarchery.items.bows;

public class LongBow extends CustomBow {
    
    public LongBow(String name) {
        super(name, 384);
        this.pullBackMult = 1.5F;      // Más lento
        this.damageMult = 1.0F;
        this.arrowSpeedMult = 2.0F;    // Flechas más rápidas
    }
}
