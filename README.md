<div align="center">
  <img src="https://i.ibb.co/4whCmXw3/bbsquare.png" alt="bbsquare" border="0">
</div>
<h1 align="center">Better Archery</h1>

<div align="center">

![Version](https://img.shields.io/badge/Version-1.0-green.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.12.2-blue.svg)
![Forge](https://img.shields.io/badge/Forge-14.23.5.2847+-orange.svg)
![License](https://img.shields.io/badge/License-MIT-red.svg)
![Status](https://img.shields.io/badge/Status-beta-yellow.svg)

**A port of the original Better Archery mod by Zaggy1024 for Minecraft 1.12.2**

[Features](#-features) • [Installation](#-installation) • [Credits](#-credits)

</div>

---

## 📖 About the Project

This is a port and, in a sense, a "fork" of the **Better Archery** mod created by Zaggy1024. Since the original source code is not available, this project is a complete recreation based on the decompilation of the original mod's `.class` files.

### 🛠️ Tools Used
- **cfr-0.152** - Java bytecode decompiler
- **JD-GUI** - Decompiled code viewer

> **Important Note**: This is a recreation work. I do not have access to the original source code. The process was complex, especially when it came to understanding obfuscated functions such as `field_77779_bT`.

---

## ✨ Features

### 🏹 Custom Bows

Four unique bows with balanced stats and complete charging animation:

| Bow | Durability | Charging Speed | Damage | Arrow Speed | Range |
|------|-------------|-------------------|------|---------------------|---------|
| **Long Bow** | 384 | 1.5x (slow) | 1.0x | 2.0x | 2.0x |
| **Recurve Bow** | 256 | 0.9x | 1.1x | 1.25x | 1.25x |
| **Composite Bow** | 1024 | 0.5x (fast) | 1.25x | 0.65x | 1.0x |
| **Yumi Bow** | 384 | 1.15x | 1.0x | 1.5x | 1.5x |

Each bow features:
- 3-stage charging animation (0%, 65%, 90%)
- Custom textures with visible bowstring
- Unique stats that affect gameplay
- Full compatibility with vanilla enchantments

### 🎯 Special Arrows

Seven types of arrows with unique effects:

#### 🔥 Fire Arrow
- **Effect**: Sets the target and blocks on fire
- **Fire Duration**: 5 seconds (configurable)
- **Recipe**: 2 Blaze Powder + 1 Arrow = 4 Fire Arrows
- **Dispenser Compatible**: ✅ Yes

#### 🔦 Torch Arrow
- **Effect**: Places a torch where it lands
- **Best for**: Lighting up caves and dark areas
- **Recipe**: 1 Torch + 1 Arrow + 1 Stick = 4 Torch Arrows
- **Dispenser Compatible**: ✅ Yes

#### 💥 Impact Arrow
- **Effect**: Spawns a TNT explosion on impact
- **Explosion Radius**: 4.0 blocks (configurable)
- **Damage**: Uses vanilla explosion system (realistic)
- **Recipe**: 2 TNT + 1 Arrow = 4 Impact Arrows
- **Dispenser Compatible**: ✅ Yes

#### 🌀 Ender Arrow
- **Effect**: Teleports the player where the arrow lands
- **Similar to**: Ender Pearl but with a bow
- **Recipe**: 2 Ender Pearls + 1 Arrow = 4 Ender Arrows
- **Dispenser Compatible**: ✅ Yes (but doesn't teleport)

#### ⚡ Splitting Arrow
- **Effect**: Splits into 3 arrows on impact
- **Damage per split**: 50% of original damage
- **Number of splits**: 3 (configurable)
- **Recipe**: 1 Redstone + 1 Arrow + 1 Feather = 4 Splitting Arrows
- **Dispenser Compatible**: ✅ Yes

#### 🏹 Drill Arrow
- **Effect**: Pierces through blocks and destroys them
- **Max blocks**: 10 (configurable)
- **Power loss**: 0.2 per block (configurable)
- **Cannot destroy**: Bedrock, Obsidian, blocks with hardness ≥ 50
- **Power based on**: Bow charging time
- **Recipe**: 2 Iron Ingots + 1 Arrow + 1 Stick = 4 Drill Arrows
- **Dispenser Compatible**: ✅ Yes

#### 🧪 Potion Arrow
- **Effect**: Applies potion effects on impact
- **Compatible with**: All vanilla potions
- **Dynamic colors**: Based on potion type
- **Custom names**: "Poison Arrow", "Slowness Arrow", etc.
- **Recipe**: 1 Arrow + 1 Potion (any) = 4 Potion Arrows
- **Dispenser Compatible**: ✅ Yes (maintains effect)

### 🎒 Quiver System

#### Empty Quiver
- **Function**: Container to store arrows
- **Capacity**: 64 arrows
- **Usage**: Right-click to collect arrows from the ground or inventory
- **Equippable**: Chest, legs, main hand, off-hand

#### Quiver with Arrows
- **Function**: Full quiver that supplies arrows automatically
- **NBT System**: Saves arrow type and quantity
- **Extraction**: Sneak + right-click to remove arrows
- **Rendering**: Visible on player's back (configurable)
- **Compatibility**: Works with all mod bows

**Advanced quiver features**:
- ✅ Saves exact arrow type (including potion effects)
- ✅ Shows correct arrow name in tooltip
- ✅ Fires the correct arrow when using the bow
- ✅ Compatible with arrows from other mods

### 💀 Quiver Skeleton

A rare skeleton that spawns with a quiver and special arrows:

- **Spawn probability**: 3% (configurable)
- **Equipment**: 
  - Quiver with 10-30 random arrows
  - 30% chance to have a special bow
- **Drops**: Drops the quiver with its arrows when killed
- **Biomes**: Spawns in all biomes where skeletons appear
- **Configurable**: Can be completely disabled

### 🔧 Dispensers

All special arrows are compatible with dispensers:

- ✅ Fire Arrow - Fires fire arrows
- ✅ Torch Arrow - Fires torch arrows
- ✅ Impact Arrow - Fires explosive arrows
- ✅ Ender Arrow - Fires teleportation arrows
- ✅ Splitting Arrow - Fires splitting arrows
- ✅ Drill Arrow - Fires drill arrows
- ✅ Potion Arrow - Fires potion arrows (maintains effect)

**Note**: Arrows fired by dispensers cannot be picked up from the ground.

### ⚙️ Configuration System

Complete configuration file at `config/BetterArchery.cfg`:

#### General
- `renderQuiverOnBack` - Enable/disable quiver rendering
- `debugMode` - Show debug information in console

#### Arrows
- `enableFireArrow` - Enable/disable Fire Arrow
- `enableTorchArrow` - Enable/disable Torch Arrow
- `enableImpactArrow` - Enable/disable Impact Arrow
- `enableEnderArrow` - Enable/disable Ender Arrow
- `enableSplittingArrow` - Enable/disable Splitting Arrow
- `enableDrillArrow` - Enable/disable Drill Arrow
- `enablePotionArrow` - Enable/disable Potion Arrow
- `impactArrowExplosionRadius` - Explosion radius (1.0 - 10.0)
- `drillArrowMaxBlocks` - Maximum blocks to destroy (1 - 50)
- `drillArrowPowerLoss` - Power loss per block (0.05 - 1.0)
- `splittingArrowSplits` - Number of splits (1 - 10)
- `fireArrowDuration` - Fire duration in seconds (1 - 30)

#### Bows
- `longBowDurability` - Long Bow durability (1 - 5000)
- `recurveBowDurability` - Recurve Bow durability (1 - 5000)
- `compositeBowDurability` - Composite Bow durability (1 - 5000)
- `yumiBowDurability` - Yumi Bow durability (1 - 5000)
- `longBowDamageMult` - Damage multiplier (0.5 - 3.0)
- `recurveBowDamageMult` - Damage multiplier (0.5 - 3.0)
- `compositeBowDamageMult` - Damage multiplier (0.5 - 3.0)
- `yumiBowDamageMult` - Damage multiplier (0.5 - 3.0)

#### Mobs
- `enableQuiverSkeleton` - Enable/disable spawn
- `quiverSkeletonSpawnWeight` - Spawn probability (1 - 100)
- `minArrows` - Minimum arrows in quiver (1 - 64)
- `maxArrows` - Maximum arrows in quiver (1 - 64)
- `specialBowChance` - Special bow probability (0.0 - 1.0)

### 🌐 Compatibility

- ✅ **JEI/HEI** - Item descriptions in the item viewer

---

## 📥 Installation

### Requirements

- **Minecraft**: 1.12.2
- **Forge**: 14.23.5.2847 or higher
- **Java**: 8 or higher

### Installation Steps

1. **Download the mod**
   - Go to the [Releases](https://github.com/Onyx-i7/Better-Archery/releases) page
   - Download the file `BetterArchery-1.12.2-1.0.0.jar`

2. **Install Forge** (if you don't have it)
   - Download Forge from [files.minecraftforge.net](https://files.minecraftforge.net/)
   - Run the installer and select "Install Client"

3. **Place the mod**
   - Open your Minecraft folder (`.minecraft`)
   - Go to the `mods` folder
   - Place the mod's `.jar` file in this folder

4. **Start the game**
   - Open the Minecraft launcher
   - Select the Forge profile
   - Start the game

5. **Verify installation**
   - In the main menu, go to "Mods"
   - You should see "Better Archery" in the list

### Server Installation

1. Place the `.jar` file in the server's `mods` folder
2. Restart the server
3. Players need the mod installed on their clients

---

## 🎮 Usage

### Bows

Bows work like the vanilla bow but with different stats:

1. **Equip the bow** in your main hand
2. **Hold right-click** to charge
3. **Release** to fire

Each bow has a 3-stage charging animation that visually shows the power.

### Quiver

#### Fill the quiver

1. **From the ground**: Right-click near arrows on the ground
2. **From inventory**: Right-click with arrows in your inventory
3. **The quiver** will transform into "Quiver with Arrows"

#### Use the quiver

1. **Equip** the quiver in any inventory slot
2. **Equip a bow** in your main hand
3. **Fire** - The quiver will supply arrows automatically

#### Extract arrows

1. **Sneak** (Shift by default)
2. **Right-click** with the quiver in hand
3. Arrows will be transferred to your inventory

### Special Arrows

Each arrow has a specific use:

- **Fire Arrow**: Ideal for fighting mobs or setting structures on fire
- **Torch Arrow**: Perfect for exploring caves without manually placing torches
- **Impact Arrow**: Useful for clearing areas or causing group damage
- **Ender Arrow**: Excellent for quick mobility
- **Splitting Arrow**: Ideal for fighting groups of enemies
- **Drill Arrow**: Perfect for mining or creating tunnels
- **Potion Arrow**: Versatile, applies any potion effect

### Dispensers

1. **Place a dispenser**
2. **Insert special arrows** into the dispenser's inventory
3. **Activate with redstone** to fire

Arrows will maintain all their special effects.

---

## 📋 Crafting Recipes

### Quiver
```
LFL
L L
LFL

L = Leather
F = String
```

### Arrows

#### Fire Arrow
```
 B 
 A 
 B 

B = Blaze Powder
A = Arrow
```

#### Torch Arrow
```
 T 
 A 
 S 

T = Torch
A = Arrow
S = Stick
```

#### Impact Arrow
```
 T 
 A 
 T 

T = TNT
A = Arrow
```

#### Ender Arrow
```
 E 
 A 
 E 

E = Ender Pearl
A = Arrow
```

#### Splitting Arrow
```
 R 
 A 
 F 

R = Redstone
A = Arrow
F = Feather
```

#### Drill Arrow
```
 I 
IAI
 S 

I = Iron Ingot
A = Arrow
S = Stick
```

#### Potion Arrow
```
Shapeless: 1 Arrow + 1 Potion (any) = 4 Potion Arrows
```

### Bows

#### Long Bow
```
  S
 LS
S F

S = String
L = Log
F = Feather
```

#### Recurve Bow
```
 SL
S I
 SL

S = String
L = Stick
I = Iron Ingot
```

#### Composite Bow
```
ISI
S S
ISI

I = Iron Ingot
S = String
```

#### Yumi Bow
```
  B
 BS
B S

B = Stick
S = String
```

---

## 🐛 Bug Reporting

If you find a bug, please report it in the [Issues](https://github.com/Onyx-i7/Better-Archery/issues) section, as it helps me improve the mod which is in a very early phase of development.

**Please include the following information**:
- Mod version
- Forge version
- Detailed description of the problem
- Steps to reproduce the error
- Relevant logs (if possible)
- Screenshots or videos (if you want, not required)

---

## 🔮 Future Roadmap

### Long-term Ideas
- Combined arrows system
- New bows
- New features
- Optimization
- System for adding bows or arrows via files for modpacks

---

## 📄 License

This project is a port/recreation of the original **Better Archery** mod by Zaggy1024, but this port is under the MIT License.

- **Original mod**: [Better Bows - CurseForge](https://www.curseforge.com/minecraft/mc-mods/better-bows)
- **Original source code**: Not available
- **This port**: Complete recreation based on decompilation

All rights to the original concept belong to Zaggy1024.

---

## 🙏 Credits

### Original Author
- **Zaggy1024** - Creator of the original Better Archery mod

### Port to 1.12.2
- **Onyx-i7** - Port development, code recreation, new features

### Tools Used
- **cfr-0.152** - Bytecode decompiler
- **JD-GUI** - Decompiled code viewer
- **RetroFuturaGradle** - Build system

---

## Contact

- **GitHub**: [Onyx-i7](https://github.com/Onyx-i7)
- **Repository**: [Better-Archery](https://github.com/Onyx-i7/Better-Archery)
- **Issues**: [Report a bug](https://github.com/Onyx-i7/Better-Archery/issues)

---

<div align="center">

**Do you like the mod? Give it a star on GitHub! ⭐**

Ported by Onyx-i7

</div>
