# Changelog
All notable changes to this project will be documented in this file.  

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## Unreleased (1.17.1-1.1.0)

## Added
- Almost all 1.17 vanilla blocks.

### Changed
- Ported to 1.17.1.
- Renamed grass_path_* to dirt_path_*. Warning, there is no data fixer, if you have some of these blocks placed in your world, they will be removed.

### Removed
- Patchouli integration. I felt it was useless.
- Secret advancements, another useless thing.

## 1.16.5-1.0.16

### Changed
- Dropped support for 1.16.2 and 1.16.3. Minimum version is now 1.16.4
- Internal code change (use of deferred registries and use of Mojang mappings)

### Fixed
- Grass Path Stairs turn back to Dirt Fence when a block is placed on top
- Grass Path Trapdoor turn back to Dirt Fence when a block is placed on top
- Grass Path Slab side texture bad placed

## 1.16.5-1.0.15

### Fixed
- Stairs with gravity doesn't connect with others

## 1.16.5-1.0.14

### Changed
- Port to 1.16.5

## 1.16.4-1.0.13

### Fixed
- Vine Stairs make the game crash
- Vine Stairs are replaced by a block on right click with this block
- Hopper Stairs catch right click
- Concrete Powder Fence have wrong collision shape
- TNT Stairs can be ignited and turn into an exploding tnt (I don't want them to explode)

## 1.16.4-1.0.12

## Changed
- Port to 1.16.4 (also work in 1.16.2 and 1.16.3)
- The in-game wiki with Patchouli is finished

### Fixed
- Mycelium and Grass fence turned back to Dirt Wall instead of Dirt Fence.
- dark_oak_log_stairs wasn't translated

## 1.16.3-1.0.11

I consider the mod finished. I don't think there will be new features in the future.  
Only port to newer versions and bug fixes.

### Added
- Sand and Gravel blocks are affected by gravity
- Nether Gold Ore Slab, Stairs, Fence, Wall and Trapdoor
- Soul Soil Slab, Stairs, Fence, Wall and Trapdoor
- Grass Path Slab, Stairs, Fence, Wall, Trapdoor
- Redstone Lamp block variants can now be powered like the vanilla version
- Netherrack block variants can be smelted to obtain nether brick
- Mycelium block variants spread on dirt block (and variants)

### Changed
- Minimum Forge version : 1.16.3-34.1.0
- log trapdoor can be opened with right-click

## 1.16.3-1.0.10
### Changed
- Port to 1.16.3
- Minimum Forge version : 1.16.3-34.0.1
It should also work in 1.16.2

## 1.16.2-1.0.9
### Changed
- Port to 1.16.2
- Minimum Forge version : 1.16.2-33.0.41
- (Optional Dependency) Minimum Patchouli version : 1.16-42

## 1.16.1-1.0.8
### Added
- Iron Ore Stairs & Wall blasting recipe
- Ore Trapdoor smelting and blasting recipes
- Concrete Powder Stairs, Slabs, Walls, Fences and Trapdoors have gravity and turn into Concrete upon touching water
- Log Trapdoor can be stripped with an axe
- Grass Block Trapdoor spread to dirt blocks
- Coarse Dirt Trapdoor can be tilled with a hoe
- Pumpkin blocks can be sheared to get Carved Pumpkin blocks
- Ore blocks drop xp
- Redstone Ore Slabs, Stairs and Fences light up like Redstone Ore

### Removed
- Ore Slab smelting and blasting recipes

## 1.16.1-1.0.7
### Added
- Trapdoor variant for most of the vanilla blocks. To not interfere with vanilla recipes, trapdoor recipes from this mod are like this :  
```
###
   
###
```
Where # is the block you want to convert in trapdoor.  
Rock based block have stonecutting recipe.
- Stairs, slabs, fences and walls variant for leaves block, vine and grass block use overlay/tintindex textures. The color change depending on the biome.
- Log, Wood, Stem and Hyphae can be stripped to get the stripped version of the block. Work with stairs, slabs, fences and walls.
- Secret advancements
- You can convert Coarse Dirt Blocks to Dirt Blocks with a Hoe.
- VanillaExtension Grass Blocks can spread to all Dirt Blocks. (Minecraft Grass Block can't spread on VanillaExtension Grass Block )

### Changed
- recipes for glass walls don't conflict with glass pane recipe anymore. Recipe for these walls are like the recipes for plank walls

## 1.16.1-1.0.6
### Added
- Patchouli guide book. (WIP)  
If you have the mod Patchouli in you modpack, a book which describe some recipes/loot\_tables from Vanilla Extension will be possible to craft (book+stick).  
Patchouli is not required to launch my mod. It is only required if you want the guide book.  
- Farmland Slab & Farmland Stairs.  
You can have them by tilling grass/dirt slab/stairs.  
Crops on Farmland Stairs are kinda glitchy, but I can't really do something for it.

### Changed
- better textures for Grass Block Slab, Crimson Nylium Slab, Warped Nylium Slab & Mycelium Slab. The side of the block is now aligned like normal block.
- recipes for plank walls (like oak\_plank\_wall) don't conflict with trapdoors anymore.
The recipes are like :  
```
 # 
###
###
```
Where `#` is a plank.  
And that give 7 walls.


## 1.16.1-1.0.5.2b
Crash at startup fixed.  
(Yeah I forget to reobf my mod)

## 1.16.1-1.0.5.2
### Changed
- loot\_tables for stairs, slabs, fences and walls variants for crimson\_nylium, warped\_nylium and gilded\_blackstone now match their origin block loot\_table

## 1.16.1-1.0.5.1
### Added
- license entry in mods.toml. The mod should work with forge 32.0.90/91/92

### Changed
- Fix malformed advancements for recipes:
  - quartz\_brick\_fence\_from\_quartz\_block\_fence\_stonecutting
  - polished\_basalt\_wall\_from\_basalt\_wall\_stonecutting
  - quartz\_brick\_wall\_from\_quartz\_block\_wall\_stonecutting
- Fix malformed recipes :
  - cracked\_nether\_brick\_fence\_from\_nether\_brick\_fence\_smelting
  - chiseled\_polished\_blackstone\_slab\_from\_blackstone\_slab\_stonecutting
  - chiseled\_polished\_blackstone\_slab\_from\_polished\_blackstone\_slab\_stonecutting
  - cracked\_polished\_blackstone\_brick\_slab\_from\_polished\_blackstone\_brick\_slab\_smelting
  - quartz\_brick\_wall\_from\_quartz\_block\_wall\_stonecutting

## 1.16.1-1.0.5
### Added
- port to 1.16
- stairs, slabs, fences and walls variant for most of 1.16 new blocks 

## 1.15.2-1.0.4
#### Added
- Walls
- Chiseled Sandstone Slab & Chiseled Sandstone Stairs
- missing recipes (stonecutting & smelting)
- blasting recipes
- missing advancements to unlock recipes

#### Changed
- better models for fences from blocks with different sides
